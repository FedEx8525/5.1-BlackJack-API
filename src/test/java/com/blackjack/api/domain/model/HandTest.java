package com.blackjack.api.domain.model;

import com.blackjack.api.domain.exception.NullDomainException;
import com.blackjack.api.domain.model.mother.HandMother;
import com.blackjack.api.domain.valueobject.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.blackjack.api.domain.model.mother.CardMother.*;
import static com.blackjack.api.domain.model.mother.CardMother.queenOfSpades;
import static com.blackjack.api.domain.model.mother.HandMother.emptyHand;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {
        @Test
        @DisplayName("Should create empty hand")
        void shouldCreateEmptyHand() {
            Hand hand = emptyHand();

            assertNotNull(hand);
            assertTrue(hand.isEmpty());
            assertEquals(0,hand.size());
        }
    }

    @Test
    @DisplayName("Should add card to empty hand")
    void shouldAddCardToEmptyHand() {
        Hand hand = emptyHand();

        hand.addCard(aceOfSpades());

        assertEquals(1, hand.size());
        assertFalse(hand.isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when adding null card")
    void shouldThrowExceptionWhenAddingNullCard() {
        Hand hand = emptyHand();

        assertThrows(NullDomainException.class, () -> hand.addCard(null));
    }

    @Nested
    @DisplayName("Score Calculation Tests")
    class ScoreCalculationTests {

        @Test
        @DisplayName("Should calculate score for single card")
        void shouldCalculateScoreForSingleCard() {
            Hand hand = emptyHand();
            hand.addCard(kingOfSpades());

            Score score = hand.calculateScore();

            assertEquals(10, score.getValue());
        }

        @Test
        @DisplayName("Should calculate Blackjack correctly")
        void shouldCalculateBlackjackCorrectly() {
            Hand hand = HandMother.blackjackHand();

            Score score = hand.calculateScore();

            assertEquals(21, score.getValue());
            assertTrue(hand.isBlackjack());
        }

        @Test
        @DisplayName("Should calculate 21 without Blackjack")
        void shouldCalculate21WithoutBlackjack() {
            Hand hand = HandMother.twentyOneWithoutBlackjack();

            Score score = hand.calculateScore();

            assertEquals(21, score.getValue());
            assertFalse(hand.isBlackjack());
        }

        @Test
        @DisplayName("Should handle Ace as 11 when possible")
        void shouldHandleAceAs11WhenPossible() {
            Hand hand = emptyHand();
            hand.addCard(aceOfSpades());
            hand.addCard(sixOfSpades());

            Score score = hand.calculateScore();

            assertEquals(17, score.getValue());
        }

        @Test
        @DisplayName("Should handle Ace as 1 when 11 would bust")
        void shouldHandleAceAs1When11WouldBust() {
            Hand hand = HandMother.twentyOneWithoutBlackjack();

            Score score = hand.calculateScore();

            assertEquals(20, score.getValue());
        }

        @Test
        @DisplayName("Should handle multiple Aces correctly")
        void shouldHandleMultipleAcesCorrectly() {
            Hand hand = HandMother.multipleAcesHand();

            Score score = hand.calculateScore();

            assertEquals(21, score.getValue());
        }

        @Test
        @DisplayName("Should detect busted hand")
        void shouldDetectBustedHand() {
            Hand hand = HandMother.bustedHand();

            Score score = hand.calculateScore();

            assertTrue(hand.isBusted());
            assertTrue(hand.calculateScore().isBusted());
        }
    }

    @Nested
    @DisplayName("Blackjack Detection Tests")
    class BlackjackDetectionTests {

        @Test
        @DisplayName("Should detect natural Blackjack with Ace and King")
        void shouldDetectBlackjackWithAceAndKing() {
            Hand hand = HandMother.blackjackHand();

            assertTrue(hand.isBlackjack());
        }

        @Test@DisplayName("Should detect natural Blackjack with Ace and Queen")
        void shouldDetectedBlackjackWithAceAndQueen() {
            Hand hand = emptyHand();
            hand.addCard(aceOfSpades());
            hand.addCard(queenOfSpades());

            assertTrue(hand.isBlackjack());
        }

        @Test@DisplayName("Should detect natural Blackjack with Ace and Jack")
        void shouldDetectedBlackjackWithAceAndJack() {
            Hand hand = emptyHand();
            hand.addCard(aceOfSpades());
            hand.addCard(jackOfSpades());

            assertTrue(hand.isBlackjack());
        }

        @Test@DisplayName("Should detect natural Blackjack with Ace and Ten")
        void shouldDetectedBlackjackWithAceAndTen() {
            Hand hand = emptyHand();
            hand.addCard(aceOfSpades());
            hand.addCard(tenOfSpades());

            assertTrue(hand.isBlackjack());
        }

        @Test
        @DisplayName("Should NOT detect Blackjack with 3 cards that total score is 21")
        void shouldNotDetectedBlackjackWith3Cards() {
            Hand hand = HandMother.twentyOneWithoutBlackjack();

            assertFalse(hand.isBlackjack());
            assertEquals(21, hand.calculateScore().getValue());
        }

        @Test
        @DisplayName("Should NOT detect Blackjack with less than 21")
        void shouldNotDetectedBlackjackWithLessThan21() {
            Hand hand = HandMother.highValueHand();

            assertFalse(hand.isBlackjack());
        }
    }

    @Nested
    @DisplayName("Bust Detection Tests")
    class BusteDetectionTests {

        @Test
        @DisplayName("Should detect bust when over 21")
        void shouldDetectedBustWhenOver21() {
            Hand hand = HandMother.bustedHand();

            assertTrue(hand.isBusted());
        }

        @Test
        @DisplayName("Should NOT detect bust with exactly 21")
        void shouldNotDetectedBustWith21() {
            Hand hand = HandMother.blackjackHand();

            assertFalse(hand.isBusted());
            assertEquals(21, hand.calculateScore().getValue());
        }

        @Test
        @DisplayName("Should NOT detected bust with less than 21")
        void shouldNotDetectedBustWithLessThan21() {
            Hand hand = HandMother.highValueHand();

            assertFalse(hand.isBusted());
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {
        @Test
        @DisplayName("getCards should return unmodifiable list ")
        void getCardsShouldReturnUnmodifiableList() {
            Hand hand = HandMother.blackjackHand();

            assertThrows(UnsupportedOperationException.class, () -> hand.getCards().add(queenOfSpades())
            );
        }

        @Test
        @DisplayName("Original hand should not change when cards list is retrieved")
        void originalHandShouldNotChangeWhenCardsListRetrieved() {
            Hand hand = emptyHand();
            hand.addCard(aceOfSpades());

            var cards = hand.getCards();
            int originalSize = hand.size();

            assertThrows(UnsupportedOperationException.class, () -> cards.add(kingOfSpades())
            );

            assertEquals(originalSize, hand.size());

        }
    }


}
