package com.blackjack.api.domain.exception;

public class EmptyDomainException extends DomainException {
    public static final String ERROR_CODE = "EMPTY_DOMAIN_ERROR";
    public EmptyDomainException(String message) {
        super(message, ERROR_CODE);
    }
}
