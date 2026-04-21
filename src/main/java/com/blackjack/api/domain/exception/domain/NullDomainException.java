package com.blackjack.api.domain.exception.domain;

import com.blackjack.api.domain.exception.domain.DomainException;

public class NullDomainException extends DomainException {
    public static final String ERROR_CODE = "NULL_DOMAIN_ERROR";
    public NullDomainException(String message) {
        super(message, ERROR_CODE);
    }
}
