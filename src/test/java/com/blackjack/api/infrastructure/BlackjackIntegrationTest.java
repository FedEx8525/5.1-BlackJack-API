package com.blackjack.api.infrastructure;

import com.blackjack.api.application.dto.response.GameResponse;
import com.blackjack.api.domain.enums.GameStatus;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.CreateGameRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Blackjack API Full Integration Test")
public class BlackjackIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should execute a full game flow: Create -> Bet -> Play")
    void fullGameFlowTest() {
        CreateGameRequest createGameRequest = new CreateGameRequest("Player Integration");

        GameResponse game = webTestClient.post()
                .uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createGameRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(GameResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(game).isNotNull();
        String gameId = game.gameId();
        assertThat(game.status()).isEqualTo(GameStatus.IN_PROGRESS);

        webTestClient.get()
                .uri("/game/{id}", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.gameId").isEqualTo(gameId)
                .jsonPath("$.playerId").exists()
                .jsonPath("$.status").isEqualTo("IN_PROGRESS")
                .jsonPath("$.playerScore").isNumber();
    }
}
