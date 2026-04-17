package com.blackjack.api.domain.service;

import com.blackjack.api.domain.model.Hand;
import com.blackjack.api.domain.valueobject.Score;

public class ScoringService {

    public int compare(Hand hand1, Hand hand2) {
        Score score1 = hand1.calculateScore();
        Score score2 = hand2.calculateScore();

        if (score1.isBusted()) {
            return -1;
        }

        if (score2.isBusted()) {
            return 1;
        }

        if (score1.beats(score2)) {
            return 1;
        } else if (score2.beats(score1)) {
            return -1;
        } else {
            return 0;
        }
    }

    public boolean hasNaturalBlackjack(Hand hand) {
        return hand.isBlackjack();
    }

    public double getPlayoutMultiplier(boolean isBlackjack, boolean isWin, boolean isTie) {
        if (isTie) {
            return 1.0;
        }

        if (!isWin) {
            return 0.0;
        }

        if (isBlackjack) {
            return 2.5;
        }

        return 2.0;
    }
}
