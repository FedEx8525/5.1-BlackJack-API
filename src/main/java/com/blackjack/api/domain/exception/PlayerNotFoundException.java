package com.blackjack.api.domain.exception;

import com.blackjack.api.domain.valueobject.PlayerId;

public class PlayerNotFoundException extends ApplicationException {
    private static final String ERROR_CODE = "PLAYER_NOT_FOUND";

    public PlayerNotFoundException(PlayerId playerId) {
        super("Player with ID: " + playerId + " not found", ERROR_CODE);
    }

    public PlayerNotFoundException(String message) {
        super(message, ERROR_CODE);
    }
}
