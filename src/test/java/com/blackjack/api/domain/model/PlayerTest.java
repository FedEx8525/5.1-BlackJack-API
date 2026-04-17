package com.blackjack.api.domain.model;

import com.blackjack.api.domain.exception.EmptyDomainException;
import com.blackjack.api.domain.exception.NegativeDomainException;
import com.blackjack.api.domain.exception.NullDomainException;
import com.blackjack.api.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.blackjack.api.domain.model.mother.PlayerMother.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {


    Money oneHundred = Money.of(100.0);
    Money fifty = Money.of(50.0);
    Money ten = Money.of(10.0);
    Money balance = Money.of(1000.0);


    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create player with valid name and balance")
        void shouldCreatePlayerWithValidNaNameAndBalance() {
            Player player = defaultPlayer();

            assertNotNull(player);
            assertNotNull(player.getId());
            assertEquals("John Doe", player.getName());
            assertEquals(balance, player.getBalance());
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {

            assertThrows(EmptyDomainException.class, () -> Player.create(null, balance));
        }

        @Test
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank() {

            assertThrows(EmptyDomainException.class, () -> Player.create("", balance));
        }

        @Test
        @DisplayName("Should throw exception whe name is empty")
        void shouldThrowExceptionWhenNameIsEmpty() {

            assertThrows(EmptyDomainException.class, () -> Player.create("   ", balance));
        }

        @Test
        @DisplayName("New player should have zero games played")
        void newPlayerShouldHaveZeroGamesPlayed() {
            Player player = newPlayer();

            assertEquals(0, player.getGamesPlayed());
            assertEquals(0, player.getGamesWon());
            assertEquals(0, player.getGamesLost());
        }

        @Test
        @DisplayName("New player should have zero win rate")
        void newPlayerShouldHaveZeroWinRate() {
            Player player = newPlayer();

            assertEquals(0, player.getWinRate());
        }
    }

    @Nested
    @DisplayName("Update Name Tests")
    class UpdateNameTests {

        @Test
        @DisplayName("Should update player name successfully")
        void shouldUpdatePlayerNameSuccessfully() {
            Player player = defaultPlayer();
            String newName = "Jane Doe";

            player.updateName(newName);

            assertEquals(newName, player.getName());
        }

        @Test
        @DisplayName("Should throw exception when new name is null")
        void shouldThrowExceptionWhenNewNameIsNull() {
            Player player = defaultPlayer();

            assertThrows(NullDomainException.class, () -> player.updateName(null));
        }

        @Test
        @DisplayName("Should throw exception when new name is blank")
        void shouldThrowExceptionWhenNewNameIsBlank() {
            Player player = defaultPlayer();

            assertThrows(NullDomainException.class, () -> player.updateName("   "));
        }
    }

    @Nested
    @DisplayName("Place Bet Tests")
    class PlaceBetTests {

        @Test
        @DisplayName("Should beduct bet amount from balance")
        void shouldDeductBetAmountFromBalance() {
            Player player = playerWithBalance(100.0);
            var initialBalance = player.getBalance();

            player.placeBet(fifty);

            assertEquals(initialBalance.subtract(fifty), player.getBalance());
        }

        @Test
        @DisplayName("Should throw exception when bet exceeds balance")
        void shouldThrowExceptionWhenBetExceedsBalance() {
            Player player = poorPlayer();

            assertThrows(NegativeDomainException.class, () -> player.placeBet(oneHundred));
        }

        @Test
        @DisplayName("Should allow bet equal to balance")
        void shouldAllowBetEqualToBalance() {
            Player player = playerWithBalance(100.0);

            assertDoesNotThrow(() -> player.placeBet(oneHundred));
            assertEquals(Money.zero(), player.getBalance());
        }
    }

    @Nested
    @DisplayName("Win Tests")
    class WinTests {

        @Test
        @DisplayName("Should add winnings to balance")
        void shouldAddWinningsToBalance() {
            Player player = playerWithBalance(100.0);
            var initialBalance = player.getBalance();

            player.win(oneHundred);

            assertEquals(initialBalance.add(oneHundred), player.getBalance());
        }

        @Test
        @DisplayName("Should increment games won")
        void shouldIncrementGamesWon() {
            Player player = newPlayer();
            int initialWins = player.getGamesWon();

            player.win(oneHundred);

            assertEquals(initialWins + 1, player.getGamesWon());
        }

        @Test
        @DisplayName("Should increment games played")
        void shouldIncrementGamesPlayed() {
            Player player = newPlayer();
            int initialGames = player.getGamesPlayed();

            player.win(oneHundred);

            assertEquals(initialGames + 1, player.getGamesPlayed());
        }

        @Test
        @DisplayName("Should update win rate correctly")
        void shouldUpdateWinRateCorrectly() {
            Player player = newPlayer();

            player.win(oneHundred);
            player.lose();
            player.win(fifty);

            assertEquals(3, player.getGamesPlayed());
            assertEquals(2, player.getGamesWon());
            assertEquals(1, player.getGamesLost());
            assertEquals(2.0 / 3.0, player.getWinRate(), 0.001);
        }

        @Nested
        @DisplayName("Lose Tests")
        class LoseTests {

            @Test
            @DisplayName("Should increment games lost")
            void shouldIncrementGamesLost() {
                Player player = newPlayer();
                int initialLosses = player.getGamesLost();

                player.lose();

                assertEquals(initialLosses + 1, player.getGamesLost());
            }

            @Test
            @DisplayName("Should increment games played")
            void shouldIncrementGamesPlayed() {
                Player player = newPlayer();
                int initialGames = player.getGamesPlayed();

                player.lose();

                assertEquals(initialGames + 1, player.getGamesPlayed());
            }

            @Test
            @DisplayName("Should NOT affect balance")
            void shouldNotAffectBalance() {
                Player player = playerWithBalance(100.0);
                var initialBalance = player.getBalance();

                player.lose();

                assertEquals(initialBalance, player.getBalance());
            }
        }

        @Nested
        @DisplayName("Tie Tests")
        class TieTests {

            @Test
            @DisplayName("Should return bet amount to balance")
            void shouldReturnBetAmountToBalance() {
                Player player = playerWithBalance(100.0);
                var initialBalance = player.getBalance();

                player.tie(fifty);

                assertEquals(initialBalance.add(fifty), player.getBalance());
            }

            @Test
            @DisplayName("Should increment games played")
            void shouldIncrementGamesPlayed() {
                Player player = newPlayer();
                int initialGames = player.getGamesPlayed();

                player.tie(fifty);

                assertEquals(initialGames + 1, player.getGamesPlayed());
            }

            @Test
            @DisplayName("Should NOT increment games won or lost")
            void shouldNotIncrementWinsOrLosses() {
                Player player = newPlayer();

                player.tie(fifty);

                assertEquals(0, player.getGamesWon());
                assertEquals(0, player.getGamesLost());
            }
        }

        @Nested
        @DisplayName("Can Bet Tests")
        class CanBetTests {

            @Test
            @DisplayName("Should return true when balance exceeds bet")
            void shouldReturnTrueWhenBalanceExceedsBet() {
                Player player = richPlayer();

                assertTrue(player.canBet(oneHundred));
            }

            @Test
            @DisplayName("Should return true when balance equals bet")
            void shouldReturnTrueWhenBalanceEqualsBet() {
                Player player = playerWithBalance(100.0);

                assertTrue(player.canBet(oneHundred));
            }

            @Test
            @DisplayName("Should return false when balance is less than bet")
            void shouldReturnFalseWhenBalanceLessThanBet() {
                Player player = poorPlayer();

                assertFalse(player.canBet(oneHundred));
            }

            @Test
            @DisplayName("Broke player should not be able to bet")
            void brokePlayerShouldNotBeAbleToBet() {
                Player player = brokePlayer();

                assertFalse(player.canBet(ten));
            }
        }

        @Nested
        @DisplayName("Win Rate Calculation Tests")
        class WinRateCalculationTests {

            @Test
            @DisplayName("Should calculate win rate correctly")
            void shouldCalculateWinRateCorrectly() {
                Player player = topRankedPlayer();

                assertEquals(0.90, player.getWinRate(), 0.001);
            }

            @Test
            @DisplayName("New player should have 0% win rate")
            void newPlayerShouldHave0PercentWinRate() {
                Player player = newPlayer();

                assertEquals(0.0, player.getWinRate());
            }

            @Test
            @DisplayName("Should calculate correct win rate after multiple games")
            void shouldCalculateCorrectWinRateAfterMultipleGames() {
                Player player = newPlayer();

                player.win(oneHundred);
                player.win(oneHundred);
                player.lose();
                player.win(oneHundred);
                player.lose();

                assertEquals(0.6, player.getWinRate(), 0.001);
            }
        }

        @Nested
        @DisplayName("Statistics Tests")
        class StatisticsTests {

            @Test
            @DisplayName("Winning player should have positive balance growth")
            void winningPlayerShouldHavePositiveBalanceGrowth() {
                Player player = topRankedPlayer();

                assertTrue(player.getGamesWon() > player.getGamesLost());
                assertTrue(player.getWinRate() > 0.5);
            }

            @Test
            @DisplayName("Losing player should have negative win rate")
            void losingPlayerShouldHaveNegativeWinRate() {
                Player player = bottomRankedPlayer();

                assertTrue(player.getGamesLost() > player.getGamesWon());
                assertTrue(player.getWinRate() < 0.5);
            }

            @Test
            @DisplayName("Games played should equal wins plus losses")
            void gamesPlayedShouldEqualWinsPlusLosses() {
                Player player = topRankedPlayer();

                assertEquals(player.getGamesPlayed(),
                        player.getGamesWon() + player.getGamesLost());
            }
        }
    }
}
