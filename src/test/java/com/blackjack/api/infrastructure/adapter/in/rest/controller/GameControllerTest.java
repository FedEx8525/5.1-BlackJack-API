package com.blackjack.api.infrastructure.adapter.in.rest.controller;

import com.blackjack.api.application.dto.response.GameResponse;
import com.blackjack.api.application.port.in.*;
import com.blackjack.api.domain.enums.GameStatus;
import com.blackjack.api.domain.enums.PlayAction;
import com.blackjack.api.domain.exception.application.GameNotFoundException;
import com.blackjack.api.domain.exception.domain.InvalidBetException;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.CreateGameRequest;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.PlaceBetRequest;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.PlayRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@WebFluxTest(GameController.class)
@DisplayName("GameController REST API Tests")
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateGameUseCase createGameUseCase;

    @MockitoBean
    private PlaceBetUseCase placeBetUseCase;

    @MockitoBean
    private PlayGameUseCase playGameUseCase;

    @MockitoBean
    private GetGameUseCase getGameUseCase;

    @MockitoBean
    private DeleteGameUseCase deleteGameUseCase;

    @Nested
    @DisplayName("POST /game/new - Create Game")
    class CreateGameTests {

        @Test
        @DisplayName("Should create game successfully")
        void shouldCreateGameSuccessfully() {

            CreateGameRequest request = new CreateGameRequest("John Doe");
            GameResponse response = createGameResponse("game-123", "player-123", 0.0);

            when(createGameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.post()
                    .uri("/game/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody()
                    .jsonPath("$.gameId").isEqualTo("game-123")
                    .jsonPath("$.playerId").isEqualTo("player-123")
                    .jsonPath("$.status").isEqualTo("IN_PROGRESS");

            verify(createGameUseCase).execute(any());
        }

        @Test
        @DisplayName("Should return 400 when player name is blank")
        void shouldReturn400WhenPlayerNameIsBlank() {

            CreateGameRequest request = new CreateGameRequest("");

            webTestClient.post()
                    .uri("/game/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(createGameUseCase, never()).execute(any());
        }
    }

    @Nested
    @DisplayName("GET /game/{id} - Get Game")
    class GetGameTests {

        @Test
        @DisplayName("Should get game by Id successfully")
        void shouldGetGameByIdSuccessfully() {

            String gameId = "game-123";
            GameResponse response = createGameResponse(gameId, "player-123",0.0);

            when(getGameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.get()
                    .uri("/game/{id}", gameId)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.gameId").isEqualTo(gameId)
                    .jsonPath("$.playerId").isEqualTo("player-123");

            verify(getGameUseCase).execute(any());
        }

        @Test
        @DisplayName("Should return 404 when game not found")
        void shouldReturn404WhenGameNotFound() {

            String gameId = "non-existent";

            when(getGameUseCase.execute(any()))
                    .thenReturn(Mono.error(new GameNotFoundException(gameId)));

            webTestClient.get()
                    .uri("/game/{id}", gameId)
                    .exchange()
                    .expectStatus().isNotFound();

            verify(getGameUseCase).execute(any());
        }
    }

    @Nested
    @DisplayName("POST /game/{id}/bet - Place Bet")
    class PlaceBetTests {

        @Test
        @DisplayName("Should place bet successfully")
        void shouldPlaceBetSuccessfully() {

            String gameId = "game-123";
            PlaceBetRequest request = new PlaceBetRequest(50.0);
            GameResponse response = createGameResponse(gameId, "player-123", 50.0);

            when(placeBetUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.post()
                    .uri("/game/{id}/bet", gameId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.gameId").isEqualTo(gameId)
                    .jsonPath("$.bet").isEqualTo(50.0);

            verify(placeBetUseCase).execute(any());
        }

        @Test
        @DisplayName("Should return 400 when bet amount is negative")
        void shouldReturn400WhenBetAmountIsNegative() {

            String gameId = "game-123";
            PlaceBetRequest request = new PlaceBetRequest(-10.0);

            webTestClient.post()
                    .uri("/game/{id}/bet", gameId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(placeBetUseCase, never()).execute(any());
        }

        @Test
        @DisplayName("Should return 400 when bet is invalid")
        void shouldReturn400WhenBetIsInvalid() {

            String gameId = "game-123";
            PlaceBetRequest request = new PlaceBetRequest(5.0);

            when(placeBetUseCase.execute(any()))
                    .thenReturn(Mono.error(new InvalidBetException("Bet too low")));

            webTestClient.post()
                    .uri("/game/{id}/bet", gameId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(placeBetUseCase).execute(any());
        }

        @Test
        @DisplayName("Should return 404 when game not found")
        void shouldReturn404WhenGameNotFound() {

            String gameId = "non-existent";
            PlaceBetRequest request = new PlaceBetRequest(50.0);

            when(placeBetUseCase.execute(any()))
                    .thenReturn(Mono.error(new GameNotFoundException(gameId)));

            webTestClient.post()
                    .uri("/game/{id}/bet", gameId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isNotFound();

            verify(placeBetUseCase).execute(any());
        }
    }

    @Nested
    @DisplayName("POST /game/{id}/play - Play Action")
    class PlayActionTests {

        @Test
        @DisplayName("Should execute HIT action successfully")
        void shouldExecuteHitActionSuccessfully() {

            String gameId = "game-123";
            PlayRequest request = new PlayRequest(PlayAction.HIT);
            GameResponse response = createGameResponse(gameId, "player-123", 0.0);

            when(playGameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.post()
                    .uri("/game/{id}/play", gameId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.gameId").isEqualTo(gameId);

            verify(playGameUseCase).execute(any());
        }

        @Test
        @DisplayName("Should execute STAND action successfully")
        void shouldExecuteStandActionSuccessfully() {

            String gameId = "game-123";
            PlayRequest request = new PlayRequest(PlayAction.STAND);
            GameResponse response = createGameResponse(gameId, "player-123", 0.0);

            when(playGameUseCase.execute(any()))
                    .thenReturn(Mono.just(response));

            webTestClient.post()
                    .uri("/game/{id}/play", gameId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk();

            verify(playGameUseCase).execute(any());
        }

        @Test
        @DisplayName("Should return 400 when action is null")
        void shouldReturn400WhenActionIsNull() {

            String gameId = "game-123";

            webTestClient.post()
                    .uri("/game/{id}/play", gameId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{}")
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(playGameUseCase, never()).execute(any());
        }

        @Test
        @DisplayName("Should return 404 when game not found")
        void shouldReturn404WhenGameNotFound() {

            String gameId = "non-existent";
            PlayRequest request = new PlayRequest(PlayAction.HIT);

            when(playGameUseCase.execute(any()))
                    .thenReturn(Mono.error(new GameNotFoundException(gameId)));

            webTestClient.post()
                    .uri("/game/{id}/play", gameId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isNotFound();

            verify(playGameUseCase).execute(any());
        }
    }

    @Nested
    @DisplayName("DELETE /game/{id}/delete - Delete Game")
    class DeleteGameTests {

        @Test
        @DisplayName("Should delete game successfully")
        void shouldDeleteGameSuccessfully() {

            String gameId = "game-123";

            when(deleteGameUseCase.execute(gameId))
                    .thenReturn(Mono.empty());

            webTestClient.delete()
                    .uri("/game/{id}/delete", gameId)
                    .exchange()
                    .expectStatus().isNoContent();

            verify(deleteGameUseCase).execute(gameId);
        }

        @Test
        @DisplayName("Should return 404 when game not found")
        void shouldReturn404WhenGameNotFound() {

            String gameId = "non-existent";

            when(deleteGameUseCase.execute(gameId))
                    .thenReturn(Mono.error(new GameNotFoundException(gameId)));

            webTestClient.delete()
                    .uri("/game/{id}/delete", gameId)
                    .exchange()
                    .expectStatus().isNotFound();

            verify(deleteGameUseCase).execute(gameId);

        }
    }

    private GameResponse createGameResponse(String gameId, String playerId, double bet) {
        return new GameResponse(
                gameId,
                playerId,
                List.of(),
                List.of(),
                0,
                0,
                bet,
                GameStatus.IN_PROGRESS,
                LocalDateTime.now(),
                48
        );
    }

}
