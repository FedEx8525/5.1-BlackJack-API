package com.blackjack.api.infrastructure.adapter.in.rest.dto.request;

import com.blackjack.api.domain.enums.PlayAction;
import jakarta.validation.constraints.NotNull;

public record PlayRequest(

        @NotNull(message = "Action is required")
        PlayAction action
) {}
