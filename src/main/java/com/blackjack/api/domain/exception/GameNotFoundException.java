package com.blackjack.api.domain.exception;

import com.blackjack.api.domain.valueobject.GameId;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(GameId gameId) {
        super("Game with ID: " + gameId + " not found");
    }
    public GameNotFoundException(String message) {
        super(message);
    }
}
