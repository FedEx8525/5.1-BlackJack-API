package com.blackjack.api.domain.model;

import com.blackjack.api.domain.exception.NullDomainException;
import com.blackjack.api.domain.valueobject.Score;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Hand {

    private static final int BLACKJACK_VALUE = 21;
    private static final int BLACKJACK_CARDS = 2;
    private static final int ACE_CHANGE_VALUE = 10;
    private final List<Card> cards;

    private Hand(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public static Hand empty() {
        return new Hand(new ArrayList<>());
    }

    public void addCard(Card card) {
        if (card == null) {
            throw new NullDomainException("The card cannot be null!");
        }
        cards.add(card);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public Score calculateScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            score += card.getValue();
            if (card.isAce()) {
                aces++;
            }
        }

        while (score > BLACKJACK_VALUE && aces > 0) {
            score -= ACE_CHANGE_VALUE;
            aces--;
        }

        return Score.of(score);
    }

    public boolean isBlackjack() {
        return cards.size() == BLACKJACK_CARDS && calculateScore().isBlackjack();
    }

    public boolean isBusted() {
        return calculateScore().isBusted();
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                ", score=" + calculateScore() +
                "}";
    }

}
