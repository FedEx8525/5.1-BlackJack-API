package com.blackjack.api.domain.exception.domain;

import com.blackjack.api.domain.exception.domain.DomainException;

public class InsufficientBalanceException extends DomainException {
    public static final String ERROR_CODE = "INSUFFICIENT_BALANCE_ERROR";

    public InsufficientBalanceException(String message) {
        super(message, ERROR_CODE);
    }
}
