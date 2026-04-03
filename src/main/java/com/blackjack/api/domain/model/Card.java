package com.blackjack.api.domain.model;

import com.blackjack.api.domain.enums.Rank;
import com.blackjack.api.domain.enums.Suit;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Card {

    private final Rank rank;
    private final Suit suit;

    private Card(Rank rank, Suit suit) {
        if (rank == null || suit == null) {
            throw new IllegalArgumentException(("Rank and Suit cannot be null" ));
        }
        this.rank = rank;
        this.suit = suit;
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

    public String toString() {
        return rank.getSymbol() + suit.getSymbol();
    }
}
