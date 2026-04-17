package com.blackjack.api.domain.enums;

import lombok.Getter;

@Getter
public enum Suit {
    HEARTS("♥", "Hearts"),
    DIAMONDS("♦", "Diamonds"),
    CLUBS("♣", "Clubs"),
    SPADES("♠", "Spades");

    private final String symbol;
    private final String displayName;

    Suit(String symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }
}
