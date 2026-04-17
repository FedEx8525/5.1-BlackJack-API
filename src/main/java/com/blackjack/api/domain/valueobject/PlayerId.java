package com.blackjack.api.domain.valueobject;

import com.blackjack.api.domain.exception.EmptyDomainException;

import java.util.Objects;
import java.util.UUID;

public record PlayerId(String value) {

    public PlayerId {
        Objects.requireNonNull(value, "The player Id cannot be null");
        if (value.isBlank()) {
            throw new EmptyDomainException("Player Id cannot be empty");
        }
    }

    public static PlayerId generate() {
        return new PlayerId(UUID.randomUUID().toString());
    }

    public static PlayerId from(String value) {
        return new PlayerId(value);
    }
}
