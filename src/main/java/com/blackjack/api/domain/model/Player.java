package com.blackjack.api.domain.model;

import com.blackjack.api.domain.exception.EmptyDomainException;
import com.blackjack.api.domain.exception.NegativeDomainException;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public class Player {

    @EqualsAndHashCode.Include
    private final PlayerId id;

    private String name;
    private Money balance;

    @Builder.Default
    private int gamesPlayed = 0;

    @Builder.Default
    private int gamesWon = 0;

    @Builder.Default
    private int gamesLost = 0;

    public static  Player create(String name, Money initialBalance) {
        if (name == null || name.isBlank()) {
            throw new EmptyDomainException("The name cannot be empty!");
        }

        return Player.builder()
                .id(PlayerId.generate())
                .name(name)
                .balance(initialBalance)
                .build();
    }

    public void placeBet(Money betAmount) {
        if (balance.isLessThan(betAmount)) {
            throw new NegativeDomainException("Insufficient balance!");
        }
        this.balance = this.balance.subtract(betAmount);
    }

    public void win(Money winnings) {
        this.balance = balance.add(winnings);
        this.gamesWon++;
        this.gamesPlayed++;
    }

    public void lose() {
        this.gamesLost++;
        this.gamesPlayed++;
    }

    public void tie(Money betAmount) {
        this.balance = balance.add(betAmount);
        this.gamesPlayed++;
    }

    public boolean canBet(Money amount) {
        return balance.isGreaterThan(amount) || balance.equals(amount);
    }

    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (double) gamesWon / gamesPlayed;
    }
}
