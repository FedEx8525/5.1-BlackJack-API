package com.blackjack.api.domain.valueobject;

import com.blackjack.api.domain.exception.NegativeScoreException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Score {
    private final int value;

    private Score(int value) {
        if (value < 0) {
            throw new NegativeScoreException();
        }
        this.value = value;
    }

    public static Score of(int value) {
        return new Score(value);
    }

    public static Score zero() {
        return new Score(0);
    }

    public boolean isBusted() {
        return value > 21;
    }

    public boolean isBlackJack() {
        return value == 21;
    }

    public boolean beats(Score other) {
        if (this.isBusted()) return false;
        if (other.isBusted()) return true;
        return this.value > other.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
