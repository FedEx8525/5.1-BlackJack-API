package com.blackjack.api.infrastructure.adapter.in.rest.controller;

import com.blackjack.api.application.dto.command.UpdatePlayerNameCommand;
import com.blackjack.api.application.dto.query.GetRankingQuery;
import com.blackjack.api.application.dto.response.PlayerResponse;
import com.blackjack.api.application.dto.response.RankingResponse;
import com.blackjack.api.application.port.in.GetRankingUseCase;
import com.blackjack.api.application.port.in.UpdatePlayerNameUseCase;
import com.blackjack.api.infrastructure.adapter.in.rest.dto.request.UpdatePlayerNameRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping
@Tag(name = "Player", description = "Player management API")
public class PlayerController {

    private final GetRankingUseCase getRankingUseCase;
    private final UpdatePlayerNameUseCase updatePlayerNameUseCase;

    public PlayerController(
            GetRankingUseCase getRankingUseCase,
            UpdatePlayerNameUseCase updatePlayerNameUseCase) {
        this.getRankingUseCase = getRankingUseCase;
        this.updatePlayerNameUseCase = updatePlayerNameUseCase;
    }

    @GetMapping("/ranking")
    @Operation(
            summary = "Get ranking",
            description = "It retrieves the list of players ordered by win rate"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ranking successfully obtained",
                    content = @Content(schema = @Schema(implementation = RankingResponse.class))
            )
    })
    public Mono<RankingResponse> getRanking() {
        log.info("GET /ranking");

        GetRankingQuery query = new GetRankingQuery();

        return getRankingUseCase.execute(query)
                .doOnSuccess(ranking ->
                        log.info("Ranking obtained with {} players", ranking.ranking().size()));
    }

    @PutMapping("/play/{playerId}")
    @Operation(
            summary = "Change player name",
            description = "Update the name of an existing player"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Name successfully updated",
                    content = @Content(schema = @Schema(implementation = PlayerResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Player NOT found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ivalid data"
            )
    })
    public Mono<PlayerResponse> updatePlayerName(
            @Parameter(description = "Game Id")
            @PathVariable String playerId,
            @Valid @RequestBody UpdatePlayerNameRequest request) {

        log.info("PUT /player/{} - newName: {}", playerId, request.newName());

        UpdatePlayerNameCommand command = new UpdatePlayerNameCommand(playerId, request.newName());

        return updatePlayerNameUseCase.execute(command)
                .doOnSuccess(player ->
                        log.info("Updated player name: {}", playerId));
    }
}
