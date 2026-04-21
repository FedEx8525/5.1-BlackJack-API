package com.blackjack.api.domain.exception;

import com.blackjack.api.domain.valueobject.GameId;

public class GameNotFoundException extends ApplicationException {
    public static final String ERROR_CODE = "GAME_NOT_FOUND";

    public GameNotFoundException(GameId gameId) {
        super("Game with ID: " + gameId + " not found", ERROR_CODE);
    }
    public GameNotFoundException(String message) {
        super(message, ERROR_CODE);
    }
}
