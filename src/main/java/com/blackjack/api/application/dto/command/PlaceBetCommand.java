package com.blackjack.api.application.dto.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlaceBetCommand(
        @NotNull(message = "Game ID cannot be null")
        String gameId,

        @Positive(message = "The bet must be greater than zero" )
        double betAmount

) {
}
