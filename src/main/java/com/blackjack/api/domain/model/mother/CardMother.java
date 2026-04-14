package com.blackjack.api.domain.model.mother;

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

    public static List<Card> blackjackHand() {
        return List.of(aceOfSpades(), kingOfSpades());
    }

    public static List<Card> bustedHand() {
        return List.of(tenOfSpades(), kingOfSpades(), fiveOfSpades());
    }

    public static List<Card> lowValueHand() {
        return List.of(tenOfSpades(), sixOfSpades());
    }

    public static List<Card> highValueHand() {
        return List.of(tenOfSpades(), nineOfSpades());
    }

    public static List<Card> dealerMustHit() {
        return List.of(tenOfSpades(), fiveOfSpades());
    }

    public static List<Card> dealerMustStand() {
        return List.of(tenOfSpades(), sevenOfSpades());
    }

    public static List<Card> dealerBlackjack() {
        return List.of(aceOfSpades(), kingOfSpades());
    }

    public static List<Card> dealerBusted() {
        return List.of(tenOfSpades(), fiveOfSpades(), kingOfSpades());
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
