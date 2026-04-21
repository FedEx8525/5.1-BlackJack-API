package com.blackjack.api.domain.exception.domain;

import com.blackjack.api.domain.exception.domain.DomainException;

public class NegativeDomainException extends DomainException {
    public static final String ERROR_CODE = "NEGATIVE_DOMAIN_ERROR";
    public NegativeDomainException(String message) {
        super(message, ERROR_CODE);
    }
}
