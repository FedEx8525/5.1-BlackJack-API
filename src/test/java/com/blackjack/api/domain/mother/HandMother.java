package com.blackjack.api.domain.mother;

import com.blackjack.api.domain.model.Hand;

import static com.blackjack.api.domain.mother.CardMother.*;

public class HandMother {

    public static Hand emptyHand() {
        return Hand.empty();
    }

    public static Hand blackjackHand() {
        Hand hand = Hand.empty();
        hand.addCard(aceOfSpades());
        hand.addCard(kingOfSpades());
        return hand;
    }

    public static Hand bustedHand() {
        Hand hand = Hand.empty();
        hand.addCard(tenOfSpades());
        hand.addCard(fiveOfSpades());
        hand.addCard((kingOfSpades()));
        return hand;
    }

    public static Hand twentyOneWithoutBlackjack() {
        Hand hand = Hand.empty();
        hand.addCard(sevenOfClubs());
        hand.addCard(sevenOfHearts());
        hand.addCard(sevenOfSpades());
        return hand;
    }

    public static Hand multipleAcesHand() {
        Hand hand = Hand.empty();
        hand.addCard(aceOfSpades());
        hand.addCard(aceOfHearts());
        hand.addCard(nineOfSpades());
        return hand;
    }

    public static Hand highValueHand() {
        Hand hand = Hand.empty();
        hand.addCard(tenOfSpades());
        hand.addCard(nineOfSpades());
        return hand;
    }

    public static Hand handWithValue(int value) {
        Hand hand = Hand.empty();

        if (value == 21) {
            return blackjackHand();
        } else if (value == 15) {
            hand.addCard(tenOfSpades());
            hand.addCard(fiveOfSpades());
        } else if (value == 10) {
            hand.addCard(tenOfSpades());
        }
        return hand;
    }

    public static Hand twoAces() {
        Hand hand = Hand.empty();
        hand.addCard(aceOfSpades());
        hand.addCard(aceOfClubs());
        return hand;
    }

    public static Hand threeAces() {
        Hand hand = Hand.empty();
        hand.addCard((aceOfClubs()));
        hand.addCard(aceOfHearts());
        hand.addCard(aceOfSpades());
        return hand;
    }

    public static Hand fourAces() {
        Hand hand = Hand.empty();
        hand.addCard((aceOfClubs()));
        hand.addCard(aceOfHearts());
        hand.addCard(aceOfSpades());
        hand.addCard(aceOfDiamonds());
        return hand;
    }

    public static Hand dealerMustHit() {
        Hand hand = Hand.empty();
        hand.addCard(tenOfSpades());
        hand.addCard(fiveOfSpades());
        return hand;
    }

    public static Hand dealerMustStand() {
        Hand hand = Hand.empty();
        hand.addCard(tenOfSpades());
        hand.addCard(sevenOfSpades());
        return hand;
    }


}
