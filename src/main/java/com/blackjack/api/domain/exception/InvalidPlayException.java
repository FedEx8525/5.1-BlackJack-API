package com.blackjack.api.domain.exception;

public class InvalidPlayException extends RuntimeException {
    public InvalidPlayException(String message) {
        super(message);
    }
    public InvalidPlayException(String message, Throwable cause) {
        super(message, cause);
    }
}
