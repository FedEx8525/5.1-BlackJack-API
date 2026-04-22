package com.blackjack.api.domain.valueobject;

import com.blackjack.api.domain.exception.domain.NegativeDomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
public class Money {

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeDomainException("The amount cannot be negative!");
        }
        this.amount = amount;
    }

    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public Money multiply(double multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)));
    }

    public boolean isZero() { return this.amount.compareTo(BigDecimal.ZERO) == 0; }

    public boolean isZeroOrLess() { return this.amount.compareTo(BigDecimal.ZERO) <= 0; }

    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        return this.amount.compareTo(other.amount) < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount != null && money.amount != null && amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.stripTrailingZeros().hashCode() : 0;
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
