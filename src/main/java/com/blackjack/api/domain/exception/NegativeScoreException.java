package com.blackjack.api.domain.exception;

public class NegativeScoreException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "The score cannot be negative!";
    public NegativeScoreException(String message) {
        super(message);
    }
    public NegativeScoreException() {
        super (DEFAULT_MESSAGE);
    }
}
