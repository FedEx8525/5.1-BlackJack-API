package com.blackjack.api.domain.service;

import com.blackjack.api.domain.exception.InvalidBetException;
import com.blackjack.api.domain.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BetValidatorTest {

    private BetValidator betValidator;

    @BeforeEach
    void setup() {
        betValidator = new BetValidator(10.0, 500.0);
    }

    @Nested
    @DisplayName("Valid bet tests")
    class ValidBetTests {

        @Test
        @DisplayName("Should accept bet equal to minimum")
        void shouldAcceptBetEqualToMinimum() {
            Money minimumBet = Money.of(10.0);

            assertDoesNotThrow(() -> betValidator.validate(minimumBet));
        }

        @Test
        @DisplayName("Should accept bet equal to maximum")
        void shouldAcceptBetEqualToMaximum() {
            Money maximumBet = Money.of(500.0);

            assertDoesNotThrow(() -> betValidator.validate(maximumBet));
        }

        @Test
        @DisplayName("Should accept various valid bet amounts")
        void shouldAcceptVariousValidBetAmounts() {
            assertDoesNotThrow(() -> betValidator.validate(Money.of(50.0)));
            assertDoesNotThrow(() -> betValidator.validate(Money.of(100.0)));
            assertDoesNotThrow(() -> betValidator.validate(Money.of(250.0)));
        }
    }

    @Nested
    @DisplayName("Error Message Tests")
    class ErrorMessageTests {

        @Test
        @DisplayName("Error message should include minimum bet amount")
        void errorMessageShouldIncludeMinimumBetAmount() {
            Money bet = Money.of(5.0);

            InvalidBetException exception = assertThrows(InvalidBetException.class,
                    () -> betValidator.validate(bet));

            assertTrue(exception.getMessage().contains("10"));
        }

        @Test
        @DisplayName("Error message should include maximum bet amount")
        void errorMessageShouldIncludeMaximumBetAmount() {
            Money bet = Money.of(1000.0);

            InvalidBetException exception = assertThrows(InvalidBetException.class,
                    () -> betValidator.validate(bet));

            assertTrue(exception.getMessage().contains("500"));
        }

        @Test
        @DisplayName("Error message should include actual bet amount")
        void errorMessageShouldIncludeActualBetAmount() {
            Money bet = Money.of(5.0);

            InvalidBetException exception = assertThrows(InvalidBetException.class,
                    () -> betValidator.validate(bet));

            assertTrue(exception.getMessage().contains("5"));
        }
    }

    @Nested
    @DisplayName("Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should return configured minimum bet")
        void shouldReturnConfiguredMinimumBet() {
            Money minimumBet = betValidator.getMinBet();

            assertEquals(Money.of(10.0), minimumBet);
        }

        @Test
        @DisplayName("Should return configured maximum bet")
        void shouldReturnConfiguredMaximumBet() {
            Money maximumBet = betValidator.getMaxBet();

            assertEquals(Money.of(500.0), maximumBet);
        }

        @Test
        @DisplayName("Should work with custom configuration")
        void shouldWorkWithCustomConfiguration() {
            BetValidator customValidator = new BetValidator(20.0, 1000.0);

            assertEquals(Money.of(20.0), customValidator.getMinBet());
            assertEquals(Money.of(1000.0), customValidator.getMaxBet());
        }

        @Test
        @DisplayName("Custom configuration should validate correctly")
        void customConfigurationShouldValidateCorrectly() {
            BetValidator customValidator = new BetValidator(20.0, 100.0);

            assertDoesNotThrow(() -> customValidator.validate(Money.of(20.0)));
            assertDoesNotThrow(() -> customValidator.validate(Money.of(100.0)));
            assertThrows(InvalidBetException.class, () -> customValidator.validate(Money.of(15.0)));
            assertThrows(InvalidBetException.class, () -> customValidator.validate(Money.of(150.0)));
        }
    }
}
