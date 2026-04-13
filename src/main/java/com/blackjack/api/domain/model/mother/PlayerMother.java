package com.blackjack.api.domain.model.mother;

import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;

public class PlayerMother {

    public static Player defaultPlayer() {
        return Player.create("John Doe", Money.of(1000.0));
    }

    public static Player playerWithBalance(double balance) {
        return Player.create("Jon Doe", Money.of(balance));
    }

    public static Player playerWithName(String name) {
        return Player.create(name, Money.of(1000.0));
    }

    public static Player playerWithNameAndBalance(String name, double balance) {
        return Player.create(name, Money.of(balance));
    }

    public static Player playerCanBetMinimum() {
        return Player.create("Min Bet", Money.of(10.0));
    }

    public static Player playerCanBetMaximum() {
        return Player.create("Max Bet", Money.of(500.0));
    }

    public static Player playerCannotBet() {
        return Player.create("Cannot bet", Money.of(5.0));
    }

    public static  Player rankedPlayer() {
        return Player.builder()
                .id(PlayerId.generate())
                .name("Ranked Player")
                .balance(Money.of(10000.0))
                .gamesPlayed(100)
                .gamesWon(90)
                .gamesLost(10)
                .build();
    }
}
