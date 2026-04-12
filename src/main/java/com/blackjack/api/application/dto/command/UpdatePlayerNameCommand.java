package com.blackjack.api.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePlayerNameCommand(
        @NotNull(message = "Player ID cannot be null")
        String playerId,

        @NotBlank(message = "The new name cannot be null")
        String newName
) {
}
