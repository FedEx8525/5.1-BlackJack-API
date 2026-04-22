package com.blackjack.api.domain.exception.domain;

import com.blackjack.api.domain.exception.domain.DomainException;

public class InvalidPlayException extends DomainException {
    public static final String ERROR_CODE = "INVALID_PLAYER_ERROR";

    public InvalidPlayException(String message) {
        super(message, ERROR_CODE);
    }
}
