package com.blackjack.api.mother;

import com.blackjack.api.domain.model.Card;
import com.blackjack.api.domain.model.Deck;

import java.util.Stack;

import static com.blackjack.api.mother.CardMother.*;

public class DeckMother {

    public static Deck standardDeck() {
        return Deck.createStandardDeck();
    }

    public static Deck shuffleDeck() {
        Deck deck = Deck.createStandardDeck();
        deck.shuffle();
        return deck;
    }

    public static Deck emptyDeck() {
        return Deck.from(new Stack<>());
    }

    public static Deck deckForPlayerBlackjack() {
        Stack<Card> cards = new Stack<>();

        cards.push(sixOfSpades());
        cards.push(aceOfSpades());
        cards.push(tenOfSpades());
        cards.push(kingOfSpades());

        return Deck.from(cards);
    }


    public static Deck deckForPlayerBust() {
        Stack<Card> cards = new Stack<>();

        cards.push(nineOfSpades());

        cards.push(fiveOfSpades());
        cards.push(sixOfSpades());
        cards.push(tenOfSpades());
        cards.push(kingOfSpades());

        return Deck.from(cards);
    }

    public static Deck deckForDealerBust() {
        Stack<Card> cards = new Stack<>();

        cards.push(nineOfSpades());

        cards.push(fiveOfSpades());
        cards.push(sixOfSpades());
        cards.push(tenOfSpades());
        cards.push(kingOfSpades());

        return Deck.from(cards);
    }

    public static Deck highCardsDeck() {
        Stack<Card> cards = new Stack<>();

        cards.push(aceOfHearts());
        cards.push(aceOfDiamonds());
        cards.push(aceOfClubs());
        cards.push(kingOfSpades());
        cards.push(queenOfSpades());
        cards.push(jackOfSpades());
        cards.push(tenOfSpades());

        return Deck.from(cards);
    }

    private static void addCardsToStack(Stack<Card> cards) {
        cards.push(aceOfClubs());
        cards.push(aceOfHearts());
        cards.push(aceOfDiamonds());
        cards.push(fiveOfSpades());
        cards.push(sevenOfClubs());
        cards.push(sixOfSpades());
        cards.push(sevenOfHearts());
        cards.push(nineOfSpades());

    }
}
