package com.blackjack.api.domain.service;

import com.blackjack.api.domain.exception.InvalidBetException;
import com.blackjack.api.domain.valueobject.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BetValidator {

    private final Money minBet;
    private final Money maxBet;

    public BetValidator(
            @Value("${blackjack.game.min-bet:10}") double minBet,
            @Value("${blackjack.game.max-bet:500}") double maxBet) {
        this.minBet = Money.of(minBet);
        this.maxBet = Money.of(maxBet);
    }

    public void validate(Money bet) {

        if (bet == null) {
            throw new InvalidBetException("The bet cannot be null");
        }

        if (bet.equals(Money.zero())) {
            throw new InvalidBetException("The bet must be greater than zero");
        }

        if (bet.isLessThan(minBet)) {
            throw  new InvalidBetException("The minimum bet is " + minBet + ", you bet: " + bet);
        }

        if (bet.isGreaterThan(maxBet)) {
            throw  new InvalidBetException("The maximum bet is " + maxBet + ", you bet: " + bet);
        }
    }

    public Money getMinBet() {
        return minBet;
    }

    public Money getMaxBet() {
        return maxBet;
    }
}
