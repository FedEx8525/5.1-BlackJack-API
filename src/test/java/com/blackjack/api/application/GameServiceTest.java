package com.blackjack.api.application;

import com.blackjack.api.application.dto.command.CreateGameCommand;
import com.blackjack.api.application.dto.command.PlaceBetCommand;
import com.blackjack.api.application.dto.command.PlayCommand;
import com.blackjack.api.application.dto.query.GetGameQuery;
import com.blackjack.api.application.port.out.GameRepository;
import com.blackjack.api.application.port.out.PlayerRepository;
import com.blackjack.api.application.service.GameService;
import com.blackjack.api.domain.enums.GameStatus;
import com.blackjack.api.domain.enums.PlayAction;
import com.blackjack.api.domain.exception.GameNotFoundException;
import com.blackjack.api.domain.exception.InsufficientBalanceException;
import com.blackjack.api.domain.exception.InvalidBetException;
import com.blackjack.api.domain.exception.PlayerNotFoundException;
import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.service.GameDomainService;
import com.blackjack.api.domain.valueobject.GameId;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.blackjack.api.mother.GameMother.*;
import static com.blackjack.api.mother.PlayerMother.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GameService Use Cases Tests")
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameDomainService gameDomainService;


    private GameService gameService;

    private final double initialBalance = 1000.0;

    @BeforeEach
    void setup() {
        gameService = new GameService(
                gameRepository,
                playerRepository,
                gameDomainService,
                initialBalance
        );
    }

    @Nested
    @DisplayName("Create Game Use Case Tests")
    class CreateGameUseCase {

        @Test
        @DisplayName("Should create game for existing player")
        void shouldCreateGameForExistingPlayer() {

            CreateGameCommand command = new CreateGameCommand("John Doe");
            Player existingPlayer = defaultPlayer();
            Game newGame = newGameWithPlayerId(existingPlayer.getId());

            when(playerRepository.findByName("John Doe"))
                    .thenReturn(Mono.just(existingPlayer));
            when(gameDomainService.createNewGame(any(PlayerId.class)))
                    .thenReturn(newGame);
            when(gameRepository.save(any(Game.class)))
                    .thenReturn(Mono.just(newGame));

            StepVerifier.create(gameService.execute(command))
                    .expectNextMatches(response ->
                            response.playerId().equals(existingPlayer.getId().value()) &&
                                    response.status() == GameStatus.IN_PROGRESS
                    )
                    .verifyComplete();

            verify(playerRepository).findByName("John Doe");
            verify(gameDomainService).createNewGame(existingPlayer.getId());
            verify(gameRepository).save(any(Game.class));
            verify(playerRepository, never()).save(any(Player.class));
        }

        @Test
        @DisplayName("Should create game and new player when player doesn't exist")
        void shouldCreateGameAndNewPlayerWhenPlayerDoesNotExist() {

            CreateGameCommand command = new CreateGameCommand("Jane Doe");
            Player newPlayer = playerWithName("Jane Doe");
            Game newGame = newGameWithPlayerId(newPlayer.getId());

            when(playerRepository.findByName("Jane Doe"))
                    .thenReturn(Mono.empty());
            when(playerRepository.save(any(Player.class)))
                    .thenReturn(Mono.just(newPlayer));
            when(gameDomainService.createNewGame(any(PlayerId.class)))
                    .thenReturn(newGame);
            when(gameRepository.save(any(Game.class)))
                    .thenReturn(Mono.just(newGame));

            StepVerifier.create(gameService.execute(command))
                    .expectNextMatches(response ->
                            response.status() == GameStatus.IN_PROGRESS)
                    .verifyComplete();

            verify(playerRepository).findByName("Jane Doe");
            verify(playerRepository).save(any(Player.class));
            verify(gameDomainService).createNewGame(any(PlayerId.class));
            verify(gameRepository).save(any(Game.class));
        }
    }

    @Nested
    @DisplayName("Place Bet Use Case Tests")
    class PlaceBetUseCaseTests {

        @Test
        @DisplayName("Should place bet successfully")
        void shouldPlaceBetSuccessfully() {

            PlaceBetCommand command = new PlaceBetCommand("game-123", 50.0);
            Game game = gameInProgress();
            Player player = richPlayer();

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.just(game));
            when(playerRepository.findById(game.getPlayerId()))
                    .thenReturn(Mono.just(player));
            when(playerRepository.save(any(Player.class)))
                    .thenReturn(Mono.just(player));
            when(gameRepository.save(any(Game.class)))
                    .thenReturn(Mono.just(game));

            StepVerifier.create(gameService.execute(command))
                    .expectNextMatches(response ->
                            response.bet() == 50.0)
                    .verifyComplete();

            verify(gameDomainService).processBet(eq(game), eq(player), any(Money.class));
            verify(playerRepository).save(player);
            verify(gameRepository).save(game);
        }

        @Test
        @DisplayName("Should throw exception when game not found")
        void shouldThrowExceptionWhenGameNotFound() {

            PlaceBetCommand command = new PlaceBetCommand("non-existent", 50.0);

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.empty());

            StepVerifier.create(gameService.execute(command))
                    .expectError(GameNotFoundException.class)
                    .verify();
        }

        @Test
        @DisplayName("Should throw exception when player not found")
        void shouldThrowExceptionWhenPlayerNotFound() {

            PlaceBetCommand command = new PlaceBetCommand("game-123", 50.0);
            Game game = gameInProgress();

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.just(game));
            when(playerRepository.findById(game.getPlayerId()))
                    .thenReturn(Mono.empty());

            StepVerifier.create(gameService.execute(command))
                    .expectError(PlayerNotFoundException.class)
                    .verify();
        }

        @Test
        @DisplayName("Should throw exception when bet is invalid")
        void shouldThrowExceptionWhenBetIsInvalid() {

            PlaceBetCommand command = new PlaceBetCommand("game-123", 5.0);
            Game game = gameInProgress();
            Player player = richPlayer();

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.just(game));
            when(playerRepository.findById(game.getPlayerId()))
                    .thenReturn(Mono.just(player));
            doThrow(new InvalidBetException("Bet too low"))
                    .when(gameDomainService).processBet(any(), any(), any());

            StepVerifier.create(gameService.execute(command))
                    .expectError(InvalidBetException.class)
                    .verify();
        }

        @Test
        @DisplayName("Should throw exception when insufficient balance")
        void shouldThrowExceptionWhenInsufficientBalance() {

            PlaceBetCommand command = new PlaceBetCommand("game-123", 500.0);
            Game game = gameInProgress();
            Player player = poorPlayer();

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.just(game));
            when(playerRepository.findById(game.getPlayerId()))
                    .thenReturn(Mono.just(player));
            doThrow(new InsufficientBalanceException("Not enough money"))
                    .when(gameDomainService).processBet(any(), any(), any());

            StepVerifier.create(gameService.execute(command))
                    .expectError(InsufficientBalanceException.class)
                    .verify();
        }
    }

    @Nested
    @DisplayName("Play Game Use Case Tests")
    class PlayGameUseCaseTests {

        @Test
        @DisplayName("Should execute HIT action successfully")
        void shouldExecuteHitActionSuccessfully() {

            PlayCommand command = new PlayCommand("game-123", PlayAction.HIT);
            Game game = gameWithBet(50.0);
            Player player = defaultPlayer();

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.just(game));
            when(playerRepository.findById(game.getPlayerId()))
                    .thenReturn(Mono.just(player));
            when(playerRepository.save(any(Player.class)))
                    .thenReturn(Mono.just(player));
            when(gameRepository.save(any(Game.class)))
                    .thenReturn(Mono.just(game));

            StepVerifier.create(gameService.execute(command))
                    .expectNextMatches(response ->
                            response.gameId().equals(game.getId().value()))
                    .verifyComplete();

            verify(gameDomainService).executeAction(game, player, PlayAction.HIT);
        }

        @Test
        @DisplayName("Should execute STAND action and finish game")
        void shouldExecuteStandActionAndFinishGame() {

            PlayCommand command = new PlayCommand("game-123", PlayAction.STAND);
            Game game = gameWithBet(50.0);
            Player player = defaultPlayer();

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.just(game));
            when(playerRepository.findById(game.getPlayerId()))
                    .thenReturn(Mono.just(player));
            when(playerRepository.save(any(Player.class)))
                    .thenReturn(Mono.just(player));
            when(gameRepository.save(any(Game.class)))
                    .thenReturn(Mono.just(game));

            doAnswer(invocation -> {
                game.stand();
                return null;
            }).when(gameDomainService).executeAction(any(), any(), any());

            when(gameDomainService.resolveGame(any(), any()))
                    .thenReturn(Money.of(100.0));

            StepVerifier.create(gameService.execute(command))
                    .expectNextCount(1)
                    .verifyComplete();

            verify(gameDomainService).executeAction(game, player, PlayAction.STAND);
            verify(playerRepository, times(1)).save(player);
        }
    }

    @Nested
    @DisplayName("Get Game Use Case Tests")
    class GetGameUseCaseTests {

        @Test
        @DisplayName("Should get game by Id successfully")
        void shouldGetGameByIdSuccessfully() {

            GetGameQuery query = new GetGameQuery("game-123");
            Game game = gameInProgress();

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.just(game));

            StepVerifier.create(gameService.execute(query))
                    .expectNextMatches(response ->
                            response.gameId().equals(game.getId().value()))
                    .verifyComplete();

            verify(gameRepository).findById(any(GameId.class));
        }

        @Test
        @DisplayName("Should throw exception when game not found")
        void shouldThrowExceptionWhenGameNotFound() {

            GetGameQuery query = new GetGameQuery("non-existent");

            when(gameRepository.findById(any(GameId.class)))
                    .thenReturn(Mono.empty());

            StepVerifier.create(gameService.execute(query))
                    .expectError(GameNotFoundException.class)
                    .verify();
        }
    }

    @Nested
    @DisplayName("Delete Game Use Case Tests")
    class DeleteGameUseCaseTests {

        @Test
        @DisplayName("Should delete game successfully")
        void shouldDeleteGameSuccessfully() {
            String gameId = "game-123";

            when(gameRepository.existsById(any(GameId.class)))
                    .thenReturn(Mono.just(true));
            when(gameRepository.deleteById(any(GameId.class)))
                    .thenReturn(Mono.empty());

            StepVerifier.create(gameService.execute(gameId))
                    .verifyComplete();

            verify(gameRepository).existsById(any(GameId.class));
            verify(gameRepository).deleteById(any(GameId.class));
        }

        @Test
        @DisplayName("Should throw exception when game doesn't exist")
        void shouldThrowExceptionWhenGameDoesNotExist() {

            String gameId = "non-existent";

            when(gameRepository.existsById(any(GameId.class)))
                    .thenReturn(Mono.just(false));

            StepVerifier.create(gameService.execute(gameId))
                    .expectError(GameNotFoundException.class)
                    .verify();

            verify(gameRepository).existsById(any(GameId.class));
            verify(gameRepository, never()).deleteById(any());
        }
    }
}
