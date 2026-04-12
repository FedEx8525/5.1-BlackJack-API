package com.blackjack.api.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePlayerNameRequest(

        @NotBlank(message = "New name is required")
        String newName
) {}
