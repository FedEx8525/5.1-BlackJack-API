package com.blackjack.api.domain.exception.domain;

import com.blackjack.api.domain.exception.domain.DomainException;

public class InvalidBetException extends DomainException {
    public static final String ERROR_CODE = "INVALID_BET_ERROR";

    public InvalidBetException(String message) {
        super(message, ERROR_CODE);
    }
}
