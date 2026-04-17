package com.blackjack.api.domain.model;

import com.blackjack.api.domain.enums.Rank;
import com.blackjack.api.domain.enums.Suit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CardTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create a card successfully with valid rank and suit")
        void shouldCreateCardSuccessfully() {
            Card card = Card.of(Rank.ACE, Suit.SPADES);

            assertThat(card.rank()).isEqualTo(Rank.ACE);
            assertThat(card.suit()).isEqualTo(Suit.SPADES);
        }

        @Test
        @DisplayName("Should throw NullPointerException when rank is null")
        void shouldThrowExceptionWhenRankIsNull() {
            assertThatThrownBy(() -> new Card(null, Suit.HEARTS))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("The rank cannot be null");
        }

        @Test
        @DisplayName("Should throw NullPointerException when suit is null")
        void shouldThrowExceptionWhenSuitIsNull() {
            assertThatThrownBy(() -> new Card(Rank.TEN, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("The suit cannot be null");
        }
    }

    @Nested
    @DisplayName("Logic & Value Tests")
    class LogicTests {

        @ParameterizedTest
        @CsvSource({
                "TWO, 2",
                "FIVE, 5",
                "TEN, 10",
                "JACK, 10",
                "QUEEN, 10",
                "KING, 10",
                "ACE, 11"
        })
        @DisplayName("Should return the correct blackjack value for each rank")
        void shouldReturnCorrectValue(Rank rank, int expectedValue) {
            Card card = Card.of(rank, Suit.DIAMONDS);
            assertThat(card.getValue()).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("Should identify if a card is an Ace")
        void shouldIdentifyAce() {
            Card ace = Card.of(Rank.ACE, Suit.CLUBS);
            Card king = Card.of(Rank.KING, Suit.CLUBS);

            assertThat(ace.isAce()).isTrue();
            assertThat(king.isAce()).isFalse();
        }
    }

    @Nested
    @DisplayName("Representation Tests")
    class RepresentationTests {

        @Test
        @DisplayName("Should return a correct string representation (RankSymbol + SuitSymbol)")
        void shouldReturnCorrectToString() {
            Card card = Card.of(Rank.ACE, Suit.SPADES);

            String expected = Rank.ACE.getSymbol() + Suit.SPADES.getSymbol();
            assertThat(card.toString()).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should respect equality between two identical cards")
        void shouldRespectEquality() {
            Card card1 = Card.of(Rank.QUEEN, Suit.HEARTS);
            Card card2 = Card.of(Rank.QUEEN, Suit.HEARTS);
            Card card3 = Card.of(Rank.KING, Suit.HEARTS);

            assertThat(card1).isEqualTo(card2);
            assertThat(card1).hasSameHashCodeAs(card2);
            assertThat(card1).isNotEqualTo(card3);
        }
    }
}
