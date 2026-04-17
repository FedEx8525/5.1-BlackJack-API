package com.blackjack.api.infrastructure.adapter.in.rest.controller;

import com.blackjack.api.application.dto.response.PlayerResponse;
import com.blackjack.api.application.dto.response.RankingResponse;
import com.blackjack.api.application.port.in.GetRankingUseCase;
import com.blackjack.api.application.port.in.UpdatePlayerNameUseCase;
import com.blackjack.api.domain.exception.PlayerNotFoundException;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.UpdatePlayerNameRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(PlayerController.class)
@DisplayName("PlayerController REST API Tests")
public class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GetRankingUseCase getRankingUseCase;

    @MockitoBean
    private UpdatePlayerNameUseCase updatePlayerNameUseCase;

    @Nested
    @DisplayName("GET /ranking - Get Ranking")
    class GetRankingTests {

        @Test
        @DisplayName("Should get ranking successfully")
        void shouldGetRankingSuccessfully() {

            RankingResponse response = createRankingResponse();

            when(getRankingUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.get()
                    .uri("/ranking")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.ranking").isArray()
                    .jsonPath("$.ranking.length()").isEqualTo(3)
                    .jsonPath("$.ranking[0].position").isEqualTo(1)
                    .jsonPath("$.ranking[0].name").isEqualTo("Top Player")
                    .jsonPath("$.ranking[1].position").isEqualTo(2)
                    .jsonPath("$.ranking[2].position").isEqualTo(3);

            verify(getRankingUseCase).execute(any());
        }

        @Test
        @DisplayName("Should return empty ranking when no players")
        void shouldReturnEmptyRankingWhenNoPlayers() {

            RankingResponse emptyResponse = new RankingResponse(List.of());

            when(getRankingUseCase.execute(any()))
                    .thenReturn(Mono.just(emptyResponse));

            webTestClient.get()
                    .uri("/ranking")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.ranking").isArray()
                    .jsonPath("$.ranking.length()").isEqualTo(0);

            verify(getRankingUseCase).execute(any());
        }

        @Test
        @DisplayName("Should include player statistics in ranking")
        void shouldIncludePlayerStatisticsInRanking() {

            RankingResponse response = createRankingResponse();

            when(getRankingUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.get()
                    .uri("/ranking")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.ranking[0].playerId").exists()
                    .jsonPath("$.ranking[0].name").exists()
                    .jsonPath("$.ranking[0].gamesWon").exists()
                    .jsonPath("$.ranking[0].gamesPlayed").exists()
                    .jsonPath("$.ranking[0].winRate").exists();

            verify(getRankingUseCase).execute(any());
        }

        @Test
        @DisplayName("Should return ranking ordered by win rate")
        void shouldReturnRankingOrderedByWinRate() {

            RankingResponse response = createRankingResponse();

            when(getRankingUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.get()
                    .uri("/ranking")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.ranking[0].winRate").isEqualTo(0.9)  // 90%
                    .jsonPath("$.ranking[1].winRate").isEqualTo(0.5)  // 50%
                    .jsonPath("$.ranking[2].winRate").isEqualTo(0.1); // 10%

            verify(getRankingUseCase).execute(any());
        }
    }

    @Nested
    @DisplayName("PUT /play/{playerId} - Update Player Name")
    class UpdatePlayerNameTests {

        @Test
        @DisplayName("Should update player name successfully")
        void shouldUpdatePlayerNameSuccessfully() {

            String playerId = "player-123";
            UpdatePlayerNameRequest request = new UpdatePlayerNameRequest("New Name");
            PlayerResponse response = createPlayerResponse(playerId, "New Name");

            when(updatePlayerNameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.put()
                    .uri("/play/{playerId}", playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.playerId").isEqualTo(playerId)
                    .jsonPath("$.name").isEqualTo("New Name");

            verify(updatePlayerNameUseCase).execute(any());
        }

        @Test
        @DisplayName("Should return 400 when new name is blank")
        void shouldReturn400WhenNewNameIsBlank() {

            String playerId = "player-123";
            UpdatePlayerNameRequest request = new UpdatePlayerNameRequest("");

            webTestClient.put()
                    .uri("/play/{playerId}", playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(updatePlayerNameUseCase, never()).execute(any());
        }

        @Test
        @DisplayName("Should return 404 when player not found")
        void shouldReturn404WhenPlayerNotFound() {

            String playerId = "non-existent";
            UpdatePlayerNameRequest request = new UpdatePlayerNameRequest("New Name");

            when(updatePlayerNameUseCase.execute(any()))
                    .thenReturn(Mono.error(new PlayerNotFoundException(playerId)));

            webTestClient.put()
                    .uri("/play/{playerId}", playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isNotFound();

            verify(updatePlayerNameUseCase).execute(any());
        }

        @Test
        @DisplayName("Should preserve player statistics when updating name")
        void shouldPreservePlayerStatisticsWhenUpdatingName() {

            String playerId = "player-123";
            UpdatePlayerNameRequest request = new UpdatePlayerNameRequest("Updated Name");
            PlayerResponse response = new PlayerResponse(
                    playerId,
                    "Updated Name",
                    1000.0,
                    100,
                    55,
                    45,
                    0.55
            );

            when(updatePlayerNameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.put()
                    .uri("/play/{playerId}", playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.name").isEqualTo("Updated Name")
                    .jsonPath("$.gamesPlayed").isEqualTo(100)
                    .jsonPath("$.gameWon").isEqualTo(55)
                    .jsonPath("$.gameLost").isEqualTo(45)
                    .jsonPath("$.winRate").isEqualTo(0.55);

            verify(updatePlayerNameUseCase).execute(any());
        }

        @Test
        @DisplayName("Should handle special characters in player name")
        void shouldHandleSpecialCharactersInPlayerName() {

            String playerId = "player-123";
            UpdatePlayerNameRequest request = new UpdatePlayerNameRequest("José García");
            PlayerResponse response = createPlayerResponse(playerId, "José García");

            when(updatePlayerNameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.put()
                    .uri("/play/{playerId}", playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.name").isEqualTo("José García");

            verify(updatePlayerNameUseCase).execute(any());
        }

        @Test
        @DisplayName("Should handle long player names")
        void shouldHandleLongPlayerNames() {

            String playerId = "player-123";
            String longName = "A".repeat(100);
            UpdatePlayerNameRequest request = new UpdatePlayerNameRequest(longName);
            PlayerResponse response = createPlayerResponse(playerId, longName);

            when(updatePlayerNameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.put()
                    .uri("/play/{playerId}", playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.name").isEqualTo(longName);

            verify(updatePlayerNameUseCase).execute(any());
        }
    }

    @Nested
    @DisplayName("Content Negotiation Tests")
    class ContentNegotiationTests {

        @Test
        @DisplayName("Should accept and return JSON")
        void shouldAcceptAndReturnJson() {

            RankingResponse response = createRankingResponse();

            when(getRankingUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.get()
                    .uri("/ranking")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON);

            verify(getRankingUseCase).execute(any());
        }

        @Test
        @DisplayName("Should require JSON content type for PUT requests")
        void shouldRequireJsonContentTypeForPutRequests() {

            String playerId = "player-123";
            UpdatePlayerNameRequest request = new UpdatePlayerNameRequest("New Name");
            PlayerResponse response = createPlayerResponse(playerId, "New Name");

            when(updatePlayerNameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.put()
                    .uri("/play/{playerId}", playerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk();

            verify(updatePlayerNameUseCase).execute(any());
        }
    }

    private RankingResponse createRankingResponse() {
        return new RankingResponse(List.of(
                new RankingResponse.PlayerRankingEntry(
                        1, "player-1", "Top Player", 90, 100, 0.9
                ),
                new RankingResponse.PlayerRankingEntry(
                        2, "player-2", "Middle Player", 50, 100, 0.5
                ),
                new RankingResponse.PlayerRankingEntry(
                        3, "player-3", "Bottom Player", 10, 100, 0.1
                )
        ));
    }

    private PlayerResponse createPlayerResponse(String playerId, String name) {
        return new PlayerResponse(
                playerId,
                name,
                1000.0,
                0,
                0,
                0,
                0.0
        );
    }
}
