package com.blackjack.api.domain.valueobject;

import com.blackjack.api.domain.exception.EmptyDomainException;

import java.util.Objects;
import java.util.UUID;


public record GameId(String value) {

    public GameId {
        Objects.requireNonNull(value, "The game Id cannot be null");
        if (value.isBlank()) {
            throw new EmptyDomainException("Game Id cannot be empty");
        }
    }

    public static GameId generate() {
        return new GameId(UUID.randomUUID().toString());
    }

    public static GameId from(String value) {
        return new GameId(value);
    }
}
