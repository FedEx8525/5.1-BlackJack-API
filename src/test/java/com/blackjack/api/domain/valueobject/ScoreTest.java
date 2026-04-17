package com.blackjack.api.domain.valueobject;

import com.blackjack.api.domain.valueobject.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create score with valid value")
        void shouldCreateScoreWithValidValue() {
            Score score = Score.of(15);

            assertNotNull(score);
            assertEquals(15, score.getValue());
        }

        @Test
        @DisplayName("Should create zero score")
        void shouldCreateZeroScore() {
            Score score = Score.zero();

            assertNotNull(score);
            assertEquals(0, score.getValue());
        }

        @Test
        @DisplayName("Should create score with value 21")
        void shouldCreateScoreWithValue21() {
            Score score = Score.of(21);

            assertEquals(21, score.getValue());
            assertTrue(score.isBlackjack());
        }

        @Test@DisplayName("Should create score over 21")
        void shouldCreateScoreOver21() {
            Score score = Score.of(25);

            assertEquals(25, score.getValue());
            assertTrue(score.isBusted());
        }
    }

    @Nested
    @DisplayName("Blackjack Detection Tests")
    class BlackjackDetectionTests {

        @Test
        @DisplayName("Should detect Blackjack when score is 21")
        void shouldDetectBlackjackWhenScoreIs21() {
            Score score = Score.of(21);

            assertTrue(score.isBlackjack());
        }

        @Test
        @DisplayName("Should NOT detect Blackjack when score is more than 21")
        void shouldNotDetectBlackjackWhenScoreIsMoreThan21() {
            Score score = Score.of(22);

            assertFalse(score.isBlackjack());
        }

        @Test
        @DisplayName("Should NOT detect Blackjack when score is less than 21")
        void shouldNotDetectBlackjackWhenScoreIsLessThan21() {
            Score score = Score.of(20);

            assertFalse(score.isBlackjack());
        }
    }


    @Nested
    @DisplayName("Bust Detection Tests")

    class BustDetectionTests {

        @Test
        @DisplayName("Should detect bust when score is over 21")
        void shouldDetectBustWhenScoreIsOver21() {
            Score score = Score.of(22);

            assertTrue(score.isBusted());
        }

        @Test
        @DisplayName("Should NOT detect bust when score is 21")
        void shouldNotDetectBustWhenScoreIs21() {
            Score score = Score.of(21);

            assertFalse(score.isBusted());
        }

        @Test
        @DisplayName("Should NOT detect bust when score is less than 21")
        void shouldNotDetectBustWhenScoreIsLessThan21() {
            Score score = Score.of(20);

            assertFalse(score.isBusted());
        }
    }

    @Nested
    @DisplayName("Comparison Tests")
    class ComparisonTests {

        @Test
        @DisplayName("Higher score should beat lower score")
        void higherScoreShouldBeatLowerScore() {
            Score higherScore = Score.of(20);
            Score lowerScore = Score.of(18);

            assertTrue(higherScore.beats(lowerScore));
            assertFalse(lowerScore.beats(higherScore));
        }

        @Test
        @DisplayName("Equal scores should NOT beat each other")
        void equalsScoreShouldNotBeatEachOther() {
            Score score1 = Score.of(19);
            Score score2 = Score.of(19);

            assertFalse(score1.beats(score2));
            assertFalse(score2.beats(score1));
        }

        @Test
        @DisplayName("Busted score should NOT beat any score")
        void bustedScoreShouldNotBeatAnyScore() {
            Score bustedScore = Score.of(25);
            Score validScore = Score.of(16);

            assertFalse(bustedScore.beats(validScore));
        }

        @Test
        @DisplayName("Valid score should beat busted score")
        void validScoreShouldBeatBustedScore() {
            Score validScore = Score.of(15);
            Score bustedScore = Score.of(25);

            assertTrue(validScore.beats(bustedScore));
        }

        @Test
        @DisplayName("Blackjack should beat any other valid score")
        void blackjackShouldBeatAnyOtherValidScore() {
            Score blackjackScore = Score.of(21);
            Score validScore = Score.of(20);

            assertTrue(blackjackScore.beats(validScore));
            assertFalse(validScore.beats(blackjackScore));
        }

        @Test
        @DisplayName("Both busted scores should NOT beat each other")
        void bothBustedScoresShouldNotBeatEachOther() {
            Score bustedScore1 = Score.of(25);
            Score bustedScore2 = Score.of(26);

            assertFalse(bustedScore1.beats(bustedScore2));
            assertFalse(bustedScore2.beats(bustedScore1));
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Scores with same value should be equal")
        void scoresWithSameValueShouldBeEqual() {
            Score score1 = Score.of(20);
            Score score2 = Score.of(20);

            assertEquals(score1, score2);
        }

        @Test
        @DisplayName("Scores with different values should NOT be equal")
        void scoresWithDifferentValuesShouldNotBeEqual() {
            Score score1 = Score.of(18);
            Score score2 = Score.of(19);

            assertNotEquals(score1, score2);
        }


    }



}
