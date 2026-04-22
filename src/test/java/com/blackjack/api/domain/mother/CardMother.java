package com.blackjack.api.mother;

import com.blackjack.api.domain.enums.Rank;
import com.blackjack.api.domain.enums.Suit;
import com.blackjack.api.domain.model.Card;

import java.util.List;

public class CardMother {

    public static Card aceOfSpades() {
        return Card.of(Rank.ACE, Suit.SPADES);
    }

    public static Card aceOfHearts() {
        return Card.of(Rank.ACE, Suit.HEARTS);
    }

    public static Card aceOfClubs() {
        return Card.of(Rank.ACE, Suit.CLUBS);
    }

    public static Card aceOfDiamonds() {
        return Card.of(Rank.ACE, Suit.DIAMONDS);
    }

    public static Card kingOfSpades() {
        return Card.of(Rank.KING, Suit.SPADES);
    }

    public static Card queenOfSpades() {
        return Card.of(Rank.QUEEN, Suit.SPADES);
    }

    public static Card jackOfSpades() {
        return Card.of(Rank.JACK, Suit.SPADES);
    }

    public static Card tenOfSpades() {
        return Card.of(Rank.TEN, Suit.SPADES);
    }

    public static Card nineOfSpades() {
        return Card.of(Rank.NINE, Suit.SPADES);
    }

    public static Card sevenOfSpades() {
        return Card.of(Rank.SEVEN, Suit.SPADES);
    }

    public static Card sevenOfHearts() {
        return Card.of(Rank.SEVEN, Suit.HEARTS);
    }

    public static Card sevenOfClubs() {
        return Card.of(Rank.SEVEN, Suit.CLUBS);
    }

    public static Card sixOfSpades() {
        return Card.of(Rank.SIX, Suit.SPADES);
    }

    public static Card fiveOfSpades() {
        return Card.of(Rank.FIVE, Suit.SPADES);
    }

}
