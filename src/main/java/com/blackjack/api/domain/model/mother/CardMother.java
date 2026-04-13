package com.blackjack.api.domain.model.mother;

import com.blackjack.api.domain.enums.Rank;
import com.blackjack.api.domain.enums.Suit;
import com.blackjack.api.domain.model.Card;

import java.util.List;

public class CardMother {

    public static List<Card> blackjackHand() {
        return List.of(Card.of(Rank.ACE, Suit.SPADES), Card.of(Rank.KING, Suit.HEARTS));
    }

    public static List<Card> twentyOneWithoutBlackjack() {
        return List.of(Card.of(Rank.SEVEN, Suit.SPADES), Card.of(Rank.SEVEN, Suit.HEARTS), Card.of(Rank.SEVEN, Suit.CLUBS));
    }

    public static List<Card> bustedHand() {
        return List.of(Card.of(Rank.TEN, Suit.SPADES), Card.of(Rank.KING, Suit.SPADES), Card.of(Rank.FIVE, Suit.SPADES));
    }

    public static List<Card> lowValueHand() {
        return List.of(Card.of(Rank.FIVE, Suit.SPADES), Card.of(Rank.SIX, Suit.SPADES));
    }

    public static List<Card> highValueHand() {
        return List.of(Card.of(Rank.TEN, Suit.SPADES), Card.of(Rank.NINE, Suit.SPADES));
    }

    public static List<Card> multipleAcesHand() {
        return List.of(Card.of(Rank.ACE, Suit.SPADES), Card.of(Rank.ACE, Suit.CLUBS), Card.of(Rank.ACE, Suit.DIAMONDS));
    }

    public static List<Card> dealerMustHit() {
        return List.of(Card.of(Rank.TEN, Suit.SPADES), Card.of(Rank.FIVE, Suit.SPADES));
    }

    public static List<Card> dealerMustStand() {
        return List.of(Card.of(Rank.TEN, Suit.SPADES), Card.of(Rank.SEVEN, Suit.SPADES));
    }

    public static List<Card> dealerBlackjack() {
        return List.of(Card.of(Rank.ACE, Suit.SPADES), Card.of(Rank.QUEEN, Suit.SPADES));
    }

    public static List<Card> dealerBusted() {
        return List.of(Card.of(Rank.TEN, Suit.SPADES), Card.of(Rank.TEN, Suit.DIAMONDS), Card.of(Rank.FIVE, Suit.SPADES));
    }

    public static class PlayerBlackjackWins {
        public static List<Card> playerHand() {
            return blackjackHand();
        }

        public static List<Card> dealerHand() {
            return highValueHand();
        }
    }

    public static class BothBlackjack {
        public static List<Card> playerHand() {
            return blackjackHand();
        }

        public static List<Card> dealerHand() {
            return blackjackHand();
        }
    }

    public static class PlayerBusts {
        public static List<Card> playerHand() {
            return bustedHand();
        }

        public static List<Card> dealerHand() {
            return highValueHand();
        }
    }

    public static class DealerBusts {
        public static List<Card> playerHand() {
            return highValueHand();
        }

        public static List<Card> dealerHand() {
            return bustedHand();
        }
    }

    public static class PlayerWinsByPoints {
        public static List<Card> playerHand() {
            return highValueHand();
        }

        public static List<Card> dealerHand() {
            return lowValueHand();
        }
    }

    public static class DealerWinsByPoints {
        public static List<Card> playerHand() {
            return lowValueHand();
        }

        public static List<Card> dealerHand() {
            return highValueHand();
        }
    }

    public static class TieGame {
        public static List<Card> playerHand() {
            return highValueHand();
        }

        public static List<Card> dealerHand() {
            return highValueHand();
        }
    }
}
