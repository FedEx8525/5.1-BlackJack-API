package com.blackjack.api.infrastructure.adapter.in.rest.controller;

import com.blackjack.api.application.dto.command.CreateGameCommand;
import com.blackjack.api.application.dto.command.PlaceBetCommand;
import com.blackjack.api.application.dto.command.PlayCommand;
import com.blackjack.api.application.dto.query.GetGameQuery;
import com.blackjack.api.application.dto.response.GameResponse;
import com.blackjack.api.application.port.in.*;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.CreateGameRequest;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.PlaceBetRequest;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.PlayRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/game")
@Tag(name = "Game", description = "Blackjack game management API")
public class GameController {

    private final CreateGameUseCase createGameUseCase;
    private final PlaceBetUseCase placeBetUseCase;
    private final PlayGameUseCase playGameUseCase;
    private final GetGameUseCase getGameUseCase;
    private final DeleteGameUseCase deleteGameUseCase;


    public GameController(CreateGameUseCase createGameUseCase,
                          PlaceBetUseCase placeBetUseCase,
                          PlayGameUseCase playGameUseCase,
                          GetGameUseCase getGameUseCase,
                          DeleteGameUseCase deleteGameUseCase) {
        this.createGameUseCase = createGameUseCase;
        this.placeBetUseCase = placeBetUseCase;
        this.playGameUseCase = playGameUseCase;
        this.getGameUseCase = getGameUseCase;
        this.deleteGameUseCase = deleteGameUseCase;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary= "Create new game",
            description = "Create a new Blackjack game. If the player does not exist, it will be created automatically"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Game successfully created",
                    content = @Content(schema = @Schema(implementation = GameResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data"
            )
    })
    public Mono<GameResponse> createGame(
            @Valid @RequestBody CreateGameRequest request) {
        log.info("POST /game/new - playerName: {}", request.playerName());

        CreateGameCommand command = new CreateGameCommand(request.playerName());

        return createGameUseCase.execute(command)
                .doOnSuccess(response -> log.info("Game created: {}", response.gameId())
                );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get game details",
            description = "Get all game details by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Game found",
                    content = @Content(schema = @Schema(implementation = GameResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game NOT found"
            )
    })
    public Mono<GameResponse> getGame(
            @Parameter(description = "Game Id")
            @PathVariable String id) {

        log.info("GET /game/{}", id);

        GetGameQuery query = new GetGameQuery(id);

        return getGameUseCase.execute(query);
    }

    @PostMapping("/{id}/bet")
    @Operation(
            summary = "Place a bet",
            description = "Set your bet for the game"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Bet successfully placed"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid bet or insufficient balance"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game NOT found"
            )
    })
    public Mono<GameResponse> placeBet(
            @Parameter(description = "Game Id")
            @PathVariable String id,
            @Valid @RequestBody PlaceBetRequest request) {
        log.info("POST /game/{}/bet - amount: {}", id, request.betAmount());

        PlaceBetCommand command = new PlaceBetCommand(id, request.betAmount());

        return placeBetUseCase.execute(command);
    }

    @PostMapping("/{id}/play")
    @Operation(
            summary = "Execute action",
            description = "Perform an action in the game (HIT, STAND, DOUBLE)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Action successfully executed"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid action"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game NOT found"
            )
    })
    public Mono<GameResponse> play(
            @Parameter(description = "Game id")
            @PathVariable String id,
            @Valid @RequestBody PlayRequest request) {
        log.info("POST /game/{}/play - action: {}", id, request.action());

        PlayCommand command = new PlayCommand(id, request.action());

        return playGameUseCase.execute(command);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete game",
            description = "Delete an existing game"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Game successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game NOT found"
            )
    })
    public Mono<Void> deleteGame(
            @Parameter(description = "Game Id")
            @PathVariable String id) {
        log.info("DELETE /game/{}/delete", id);

        return deleteGameUseCase.execute(id)
                .doOnSuccess(v -> log.info("Game deleted: {}", id));
    }
}
