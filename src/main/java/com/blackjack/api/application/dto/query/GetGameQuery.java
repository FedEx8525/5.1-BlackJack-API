package com.blackjack.api.application.dto.query;

import jakarta.validation.constraints.NotBlank;

public record GetGameQuery(
        @NotBlank(message = "The game ID is required" )
        String gameId
) {
}
