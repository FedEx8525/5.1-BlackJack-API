package com.blackjack.api.domain.model.mother;

import com.blackjack.api.domain.model.Card;
import com.blackjack.api.domain.model.Deck;

import java.util.Stack;

import static com.blackjack.api.domain.model.mother.CardMother.*;

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

        cards.push(kingOfSpades());
        cards.push(tenOfSpades());
        cards.push(aceOfSpades());
        cards.push(sixOfSpades());

        addCardsToStack(cards);
        return Deck.from(cards);
    }

    public static Deck deckForDealerBlackjack() {
        Stack<Card> cards = new Stack<>();

        cards.push(kingOfSpades());
        cards.push(tenOfSpades());
        cards.push(sixOfSpades());
        cards.push(fiveOfSpades());

        addCardsToStack(cards);
        return Deck.from(cards);
    }

    public static Deck deckForPlayerBust() {
        Stack<Card> cards = new Stack<>();

        cards.push(kingOfSpades());
        cards.push(tenOfSpades());
        cards.push(sixOfSpades());
        cards.push(fiveOfSpades());

        cards.push(nineOfSpades());

        addCardsToStack(cards);
        return Deck.from(cards);
    }

    public static Deck deckForDealerBust() {
        Stack<Card> cards = new Stack<>();

        cards.push(kingOfSpades());
        cards.push(tenOfSpades());
        cards.push(sixOfSpades());
        cards.push(fiveOfSpades());

        cards.push(nineOfSpades());

        addCardsToStack(cards);
        return Deck.from(cards);
    }

    private static void addCardsToStack(Stack<Card> cards) {
        cards.push(nineOfSpades());
        cards.push(aceOfClubs());
        cards.push(fiveOfSpades());
        cards.push(sevenOfClubs());
        cards.push(sixOfSpades());
        cards.push(sevenOfHearts());
        cards.push(aceOfDiamonds());
        cards.push(aceOfHearts());
    }
}
