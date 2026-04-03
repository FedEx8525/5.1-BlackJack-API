package com.blackjack.api.domain.enums;

import lombok.Getter;

@Getter
public enum Suit {
    HEARTS("Hearts"),
    DIAMONDS("Diamonds"),
    CLUBS("Clubs"),
    SPADES("Spades");

    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }
}
