package com.blackjack.api.domain.exception;

public class EmptyDeckException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "There is no cards to deal!";
    public EmptyDeckException(String message) {
        super(message);
    }
    public EmptyDeckException() {
        super(DEFAULT_MESSAGE);
    }
}
