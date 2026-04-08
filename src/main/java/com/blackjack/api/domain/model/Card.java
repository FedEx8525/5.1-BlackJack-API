package com.blackjack.api.domain.model;

import com.blackjack.api.domain.enums.Rank;
import com.blackjack.api.domain.enums.Suit;
import com.blackjack.api.domain.exception.NullDomainException;

import java.util.Objects;


public record Card(Rank rank, Suit suit) {

    public Card {
        Objects.requireNonNull(rank, "The rank cannot be null");
        Objects.requireNonNull(suit, "The suit cannot be null");
    }

    public static Card of(Rank rank, Suit suit) {
        return new Card(rank, suit);
    }

    public int getValue() {
        return rank.getValue();
    }

    public boolean isAce() {
        return rank == Rank.ACE;
    }
}
