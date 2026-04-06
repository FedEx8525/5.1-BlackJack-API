package com.blackjack.api.application.dto.command;

import jakarta.validation.constraints.NotBlank;

public record CreateGameCommand(
        @NotBlank(message = "You must enter player name")
        String playerName
) {
}
