package com.blackjack.api.application.dto.command;

import com.blackjack.api.domain.enums.PlayAction;
import jakarta.validation.constraints.NotNull;

public record PlayCommand(
        @NotNull(message = "The game ID cannot be null")
        String gameId,

        @NotNull(message = "Action is mandatory")
        PlayAction action
) {

}
