package com.blackjack.api.domain.exception;

import com.blackjack.api.domain.valueobject.PlayerId;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(PlayerId playerId) {
        super("Player with ID: " + playerId + " not found");
    }
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
