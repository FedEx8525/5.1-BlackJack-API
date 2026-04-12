package com.blackjack.api.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateGameRequest(

        @NotBlank(message = "The player's name is required")
        String playerName
) {}
