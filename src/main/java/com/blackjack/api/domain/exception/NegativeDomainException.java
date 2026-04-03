package com.blackjack.api.domain.exception;

public class NegativeAmountException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "The amount cannot be negative!";
    public NegativeAmountException(String message) {
        super(message);
    }
    public NegativeAmountException() {
        super(DEFAULT_MESSAGE);
    }
}
