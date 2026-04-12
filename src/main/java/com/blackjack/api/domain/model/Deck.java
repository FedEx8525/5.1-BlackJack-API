package com.blackjack.api.domain.model;

import com.blackjack.api.domain.enums.Rank;
import com.blackjack.api.domain.enums.Suit;
import com.blackjack.api.domain.exception.EmptyDomainException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

@Getter
public class Deck {

    private final Stack<Card> cards;

    private Deck(Stack<Card> cards) {
        this.cards = cards;
    }

    public static Deck createStandardDeck() {
        List<Card> cardList = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cardList.add(Card.of(rank, suit));
            }
        }

        Stack<Card> cardStack = new Stack<>();
        cardStack.addAll(cardList);

        return new Deck(cardStack);
    }

    public void shuffle() {
        List<Card> tempList = new ArrayList<>(cards);
        Collections.shuffle(tempList);
        cards.clear();
        cards.addAll(tempList);
    }

    public Card deal() {
        if (cards.isEmpty()) {
            throw new EmptyDomainException("There is no cards to deal!");
        }
        return cards.pop();
    }

    public boolean hasCards() {
        return !cards.isEmpty();
    }

    public int remainingCards() {
        return cards.size();
    }

    public static Deck from(Stack<Card> cards) {
        return new Deck(cards);
    }
}
