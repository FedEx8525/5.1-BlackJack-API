package com.blackjack.api.domain.exception;

public class InvalidBetException extends RuntimeException {
    public InvalidBetException(String message) {
        super(message);
    }
    public InvalidBetException(String message, Throwable cause) {
        super(message, cause);
    }
}
