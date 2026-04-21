package com.blackjack.api.domain.exception;

public class ValidateGameException extends DomainException {
    public static final String ERROR_CODE = "VALIDATE_GAME_ERROR";
    public ValidateGameException(String message) {
        super(message, ERROR_CODE);
    }
}
