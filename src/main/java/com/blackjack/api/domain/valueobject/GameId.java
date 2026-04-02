package com.blackjack.api.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class GameId {

    private final String value;

    private GameId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Game Id cannot be empty");
        }
        this.value = value;
    }

    public static GameId generate() {
        return new GameId(UUID.randomUUID().toString());
    }

    public static GameId from(String value) {
        return new GameId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
