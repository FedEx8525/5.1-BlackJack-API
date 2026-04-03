package com.blackjack.api.domain.valueobject;

import com.blackjack.api.domain.exception.EmptyDomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class PlayerId {

    private final String value;

    private PlayerId(String value) {
        if (value == null || value.isBlank()) {
            throw new EmptyDomainException("Player Id cannot be empty");
        }
        this.value = value;
    }

    public static PlayerId generate() {
        return new PlayerId(UUID.randomUUID().toString());
    }

    public static PlayerId from(String value) {
        return new PlayerId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
