package com.blackjack.api.domain.exception;

public class NullCardException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "The card cannot be null!";
    public NullCardException(String message) {
        super(message);
    }
    public NullCardException() {
        super(DEFAULT_MESSAGE);
    }
}
