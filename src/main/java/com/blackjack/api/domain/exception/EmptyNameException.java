package com.blackjack.api.domain.exception;

public class EmptyNameException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "The name cannot be empty!";
    public EmptyNameException(String message) {
        super(message);
    }
    public EmptyNameException() {
        super(DEFAULT_MESSAGE);
    }
}
