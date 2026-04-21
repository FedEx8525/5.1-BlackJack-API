package com.blackjack.api.domain.exception.domain;

import com.blackjack.api.domain.exception.domain.DomainException;

public class InvalidCardException extends DomainException {
    public static final String ERROR_CODE = "INVALID_CARD_FORMAT";

    public InvalidCardException(String message) {
        super(message, ERROR_CODE);
    }
}
