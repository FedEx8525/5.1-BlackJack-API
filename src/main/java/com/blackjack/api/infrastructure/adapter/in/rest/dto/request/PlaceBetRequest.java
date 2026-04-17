package com.blackjack.api.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.Positive;

public record PlaceBetRequest(

        @Positive(message = "The bet cannot be negative")
        double betAmount
) {}
