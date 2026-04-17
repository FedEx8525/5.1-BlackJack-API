package com.blackjack.api.application;

import com.blackjack.api.application.dto.command.UpdatePlayerNameCommand;
import com.blackjack.api.application.dto.query.GetRankingQuery;
import com.blackjack.api.application.dto.response.RankingResponse;
import com.blackjack.api.application.port.out.PlayerRepository;
import com.blackjack.api.application.service.PlayerService;
import com.blackjack.api.domain.exception.PlayerNotFoundException;
import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.valueobject.PlayerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.blackjack.api.mother.PlayerMother.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlayerService Use Cases Tests")
public class PlayServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Nested
    @DisplayName("Get Ranking Use Case Tests")
    class GetRankingUseCaseTests {

        @Test
        @DisplayName("Should get ranking with all players ordered by win rate")
        void shouldGetRankingWithAllPlayersOrderedByWinRate() {

            GetRankingQuery query = new GetRankingQuery();

            Player topPlayer = topRankedPlayer();
            Player middlePlayer = middleRankedPlayer();
            Player bottomPlayer = bottomRankedPlayer();

            when(playerRepository.findAllOrderedByWinRate())
                    .thenReturn(Flux.just(topPlayer, middlePlayer, bottomPlayer));

            StepVerifier.create(playerService.execute(query))
                    .expectNextMatches(ranking -> {
                        var players = ranking.ranking();
                        return players.size() == 3 &&
                                players.get(0).position() == 1 &&
                                players.get(0).winRate() == topPlayer.getWinRate() &&
                                players.get(1).position() == 2 &&
                                players.get(1).winRate() == middlePlayer.getWinRate() &&
                                players.get(2).position() == 3 &&
                                players.get(2).winRate() == bottomPlayer.getWinRate();
                    })
                    .verifyComplete();

            verify(playerRepository).findAllOrderedByWinRate();
        }

        @Test
        @DisplayName("Should return empty ranking when no players exist")
        void shouldReturnEmptyRankingWhenNoPlayersExist() {

            GetRankingQuery query = new GetRankingQuery();

            when(playerRepository.findAllOrderedByWinRate())
                    .thenReturn(Flux.empty());

            StepVerifier.create(playerService.execute(query))
                    .expectNextMatches(ranking ->
                            ranking.ranking().isEmpty())
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should assign correct positions in ranking")
        void shouldAssignCorrectPositionsInRanking() {

            GetRankingQuery query = new GetRankingQuery();
            Player player1 = topRankedPlayer();
            Player player2 = middleRankedPlayer();
            Player player3 = bottomRankedPlayer();

            when(playerRepository.findAllOrderedByWinRate())
                    .thenReturn(Flux.just(player1, player2, player3));

            StepVerifier.create(playerService.execute(query))
                    .expectNextMatches(ranking -> {
                        var players = ranking.ranking();
                        return players.get(0).position() == 1 &&
                                players.get(1).position() == 2 &&
                                players.get(2).position() == 3;
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should include all player statistics in ranking")
        void shouldIncludeAllPlayerStatisticsInRanking() {

            GetRankingQuery query = new GetRankingQuery();
            Player player = topRankedPlayer();

            when(playerRepository.findAllOrderedByWinRate())
                    .thenReturn(Flux.just(player));

            StepVerifier.create(playerService.execute(query))
                    .expectNextMatches(ranking -> {
                        RankingResponse.PlayerRankingEntry entry = ranking.ranking().get(0);
                        return entry.playerId().equals(player.getId().value()) &&
                                entry.name().equals(player.getName()) &&
                                entry.gamesWon() == player.getGamesWon() &&
                                entry.gamesPlayed() == player.getGamesPlayed() &&
                                entry.winRate() == player.getWinRate();
                    })
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should handle ranking with single player")
        void shouldHandleRankingWithSinglePlayer() {

            GetRankingQuery query = new GetRankingQuery();
            Player player = defaultPlayer();

            when(playerRepository.findAllOrderedByWinRate())
                    .thenReturn(Flux.just(player));

            StepVerifier.create(playerService.execute(query))
                    .expectNextMatches(ranking ->
                            ranking.ranking().get(0).position() == 1)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Update Player Name Use Case Tests")
    class UpdatePlayerNameUseCaseTests {

        @Test
        @DisplayName("Should update player name successfully")
        void shouldUpdatePlayerNameSuccessfully() {

            String playerId = "player-123";
            String newName = "Jane Doe";
            UpdatePlayerNameCommand command = new UpdatePlayerNameCommand(playerId, newName);

            Player player = playerWithId(playerId);

            when(playerRepository.findById(any(PlayerId.class)))
                    .thenReturn(Mono.just(player));
            when(playerRepository.save(any(Player.class)))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

            StepVerifier.create(playerService.execute(command))
                    .expectNextMatches(response ->
                            response.playerId().equals(playerId) &&
                                    response.name().equals(newName))
                    .verifyComplete();

            verify(playerRepository).findById(any(PlayerId.class));
            verify(playerRepository).save(any(Player.class));
        }

        @Test
        @DisplayName("Should throw exception when player not found")
        void shouldThrowExceptionWhenPlayerNotFound() {

            String playerId = "non-existent";
            UpdatePlayerNameCommand command = new UpdatePlayerNameCommand(playerId, "New Name");

            when(playerRepository.findById(any(PlayerId.class)))
                    .thenReturn(Mono.empty());

            StepVerifier.create(playerService.execute(command))
                    .expectError(PlayerNotFoundException.class)
                    .verify();

            verify(playerRepository).findById(any(PlayerId.class));
            verify(playerRepository, never()).save(any(Player.class));
        }

        @Test
        @DisplayName("Should preserve player statistics when updating name")
        void shouldPreservePlayerStatisticsWhenUpdatingName() {

            String playerId = "player-123";
            String newName = "Updated Name";
            UpdatePlayerNameCommand command = new UpdatePlayerNameCommand(playerId, newName);

            Player player = topRankedPlayer();
            int originalGamesPlayed = player.getGamesPlayed();
            int originalGamesWon = player.getGamesWon();

            when(playerRepository.findById(any(PlayerId.class)))
                    .thenReturn(Mono.just(player));
            when(playerRepository.save(any(Player.class)))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

            StepVerifier.create(playerService.execute(command))
                    .expectNextMatches(response ->
                            response.gamesPlayed() == originalGamesPlayed &&
                                    response.gameWon() == originalGamesWon)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should handle multiple name updates for same player")
        void shouldHandleMultipleNameUpdatesForSamePlayer() {

            String playerId = "player-123";
            Player player = playerWithId(playerId);

            when(playerRepository.findById(any(PlayerId.class)))
                    .thenReturn(Mono.just(player));
            when(playerRepository.save(any(Player.class)))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

            UpdatePlayerNameCommand command1 = new UpdatePlayerNameCommand(playerId, "First Name");
            StepVerifier.create(playerService.execute(command1))
                    .expectNextMatches(response ->
                            response.name().equals("First Name"))
                    .verifyComplete();

            UpdatePlayerNameCommand command2 = new UpdatePlayerNameCommand(playerId, "Second Name");
            StepVerifier.create(playerService.execute(command2))
                    .expectNextMatches(response ->
                            response.name().equals("Second Name"))
                    .verifyComplete();

            verify(playerRepository, times(2)).findById(any(PlayerId.class));
            verify(playerRepository, times(2)).save(any(Player.class));
        }
    }

    @Nested
    @DisplayName("Integration Scenarios Tests")
    class IntegrationScenariosTests {

        @Test
        @DisplayName("Should reflect name change in ranking")
        void shouldReflectNameChangeInRanking() {

            String playerId = "player123";
            String newName = "Champion Player";
            Player player = playerWithIdAndName(playerId, "Old Name");

            when(playerRepository.findById(any(PlayerId.class)))
                    .thenReturn(Mono.just(player));
            when(playerRepository.save(any(Player.class)))
                    .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

            UpdatePlayerNameCommand command = new UpdatePlayerNameCommand(playerId, newName);

            StepVerifier.create(playerService.execute(command))
                    .expectNextCount(1)
                    .verifyComplete();

            when(playerRepository.findAllOrderedByWinRate())
                    .thenReturn(Flux.just(player));

            GetRankingQuery query = new GetRankingQuery();

            StepVerifier.create(playerService.execute(query))
                    .expectNextMatches(ranking ->
                            ranking.ranking().get(0).name().equals(newName))
                    .verifyComplete();
        }
    }

}
