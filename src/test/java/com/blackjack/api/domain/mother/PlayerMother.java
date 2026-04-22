package com.blackjack.api.domain.mother;

import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;

public class PlayerMother {

    public static Player defaultPlayer() {
        return Player.create("John Doe", Money.of(1000.0));
    }

    public static Player newPlayer() {
        return Player.builder()
                .id(PlayerId.generate())
                .name("New Player")
                .balance(Money.of(1000.0))
                .gamesPlayed(0)
                .gamesWon(0)
                .gamesLost(0)
                .build();
    }

    public static Player richPlayer() {
        return Player.create("Rich Player", Money.of(5000.0));
    }

    public static Player poorPlayer() {
        return Player.create("Poor Player", Money.of(10.0));
    }

    public static Player playerWithBalance(double balance) {
        return Player.create("Jon Doe", Money.of(balance));
    }

    public static Player playerWithName(String name) {
        return Player.create(name, Money.of(1000.0));
    }

    public static Player brokePlayer() {
        return Player.create("Broke Player", Money.of(0.0));
    }

    public static Player topRankedPlayer() {
        return Player.builder()
                .id(PlayerId.generate())
                .name("Top Player")
                .balance(Money.of(10000.0))
                .gamesPlayed(100)
                .gamesWon(90)
                .gamesLost(10)
                .build();
    }

    public static Player middleRankedPlayer() {
        return Player.builder()
                .id(PlayerId.generate())
                .name("Middle Player")
                .balance(Money.of(1000.0))
                .gamesPlayed(100)
                .gamesWon(50)
                .gamesLost(50)
                .build();
    }

    public static Player bottomRankedPlayer() {
        return Player.builder()
                .id(PlayerId.generate())
                .name("Bottom Player")
                .balance(Money.of(100.0))
                .gamesPlayed(100)
                .gamesWon(10)
                .gamesLost(90)
                .build();
    }

    public static Player playerWithId(String id) {
        return Player.builder()
                .id(PlayerId.from(id))
                .name("Player " + id)
                .balance(Money.of(1000.0))
                .gamesPlayed(0)
                .gamesWon(0)
                .gamesLost(0)
                .build();
    }

    public static Player playerWithIdAndName(String id, String name) {
        return Player.builder()
                .id(PlayerId.from(id))
                .name(name)
                .balance(Money.of(1000.0))
                .gamesPlayed(0)
                .gamesWon(0)
                .gamesLost(0)
                .build();
    }
}
