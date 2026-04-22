package com.blackjack.api.domain.model;

import com.blackjack.api.domain.enums.GameStatus;
import com.blackjack.api.domain.exception.domain.NegativeDomainException;
import com.blackjack.api.domain.exception.domain.NullDomainException;
import com.blackjack.api.domain.exception.domain.ValidateGameException;
import com.blackjack.api.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.blackjack.api.domain.mother.DeckMother.*;
import static com.blackjack.api.domain.mother.GameMother.*;
import static com.blackjack.api.domain.mother.PlayerMother.defaultPlayer;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create new game successfully")
        void shouldCreateNewGameSuccessfully() {
            Game game = newGame();
            assertNotNull(game);
            assertNotNull(game.getId());
            assertNotNull(game.getPlayerId());
            assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
            assertEquals(2, game.getPlayerHand().size());
            assertEquals(2, game.getDealerHand().size());
        }

        @Test
        @DisplayName("Should throw exception when PlayerId is null")
        void shouldThrowExceptionWhenPlayerIdIsNull() {
            assertThrows(NullDomainException.class, () ->
                    Game.create(null, Deck.createStandardDeck()));
        }

        @Test
        @DisplayName("Should throw exception when Deck is null")
        void shouldThrowExceptionWhenDeckIsNull() {
            assertThrows(NullDomainException.class, () ->
                    Game.create(defaultPlayer().getId(), null));
        }

        @Test
        @DisplayName("New game should have zero bet")
        void newGameShouldHaveZeroBet() {
            Game game = newGame();
            assertTrue(game.getBet().isZero()); // [CAMBIO]: Usamos .isZero()
        }

        @Test
        @DisplayName("New game should be in progress")
        void newGameShouldBeInProgress() {
            Game game = newGame();
            assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
            assertFalse(game.isFinished());
        }
    }

    @Nested
    @DisplayName("Place Bet tests")
    class PlaceBetTests {

        @Test
        @DisplayName("Should place bet successfully")
        void shouldPlaceBetSuccessfully() {
            Game game = newGame();
            Money bet = Money.of(100.0);
            game.placeBet(bet);
            assertEquals(bet, game.getBet());
        }

        @Test
        @DisplayName("Should throw exception when bet is null")
        void shouldThrowExceptionWhenBetIsNull() {
            Game game = newGame();
            assertThrows(NegativeDomainException.class, () -> game.placeBet(null));
        }

        @Test
        @DisplayName("Should throw exception when bet is zero")
        void shouldThrowExceptionWhenBetIsZero() {
            Game game = newGame();
            assertThrows(NegativeDomainException.class, () -> game.placeBet(Money.zero()));
        }

        @Test
        @DisplayName("Should throw exception when betting on finished game")
        void shouldThrowExceptionWhenBettingOnFinishedGame() {
            Game game = gamePlayerWon();
            assertThrows(ValidateGameException.class, () -> game.placeBet(Money.of(50)));
        }
    }

    @Nested
    @DisplayName("Hit Action Tests")
    class HitActionTests {

        @Test
        @DisplayName("Should add card to player hand when hitting")
        void shouldAddCardToPlayerHandWhenHitting() {
            Game game = gameWithBet(50.0);
            int initialSize = game.getPlayerHand().size();
            game.hit();
            assertEquals(initialSize + 1, game.getPlayerHand().size());
        }

        @Test
        @DisplayName("Should bust player when hitting causes over 21")
        void shouldBustPlayerWhenHittingCausesOver21() {
            Game game = Game.create(defaultPlayer().getId(), deckForPlayerBust());
            game.placeBet(Money.of(50.0));
            game.hit();
            assertEquals(GameStatus.PLAYER_BUSTED, game.getStatus());
            assertTrue(game.isFinished());
        }

        @Test
        @DisplayName("Should throw exception when hitting without bet")
        void shouldThrowExceptionWhenHitWithoutBet() {
            Game game = newGame(); // [CAMBIO]: Test nuevo necesario por la refactorización
            assertThrows(ValidateGameException.class, game::hit);
        }

        @Test
        @DisplayName("Should throw exception when hitting finished game")
        void shouldThrowExceptionWhenHitFinishedGame() {
            Game game = gamePlayerWon();
            assertThrows(ValidateGameException.class, game::hit);
        }
    }

    @Nested
    @DisplayName("Stand Action Tests")
    class StandActionTests {

        @Test
        @DisplayName("Dealer Should hit until 17 or more when player stands")
        void dealerShouldHitUntil17OrMore() {
            Game game = gameWithBet(50.0);
            game.stand();
            int dealerScore = game.getDealerHand().calculateScore().getValue();
            assertTrue(dealerScore >= 17 || game.getDealerHand().isBusted());
        }

        @Test
        @DisplayName("Should throw exception when standing on finished game")
        void shouldThrowExceptionWhenStandingOnFinishedGame() {
            Game game = gamePlayerWon();
            assertThrows(ValidateGameException.class, game::stand);
        }

        @Test
        @DisplayName("Should throw exception when standing without bet")
        void shouldThrowExceptionWhenStandingWithoutBet() {
            Game game = newGame(); // [CAMBIO]: Usamos un juego nuevo sin apuesta
            assertThrows(ValidateGameException.class, game::stand);
        }

        @Test
        @DisplayName("Game should finish after stand")
        void gameShouldFinishAfterStand() {
            Game game = gameWithBet(50.0);
            game.stand();
            assertTrue(game.isFinished());
            assertNotEquals(GameStatus.IN_PROGRESS, game.getStatus());
        }
    }

    @Nested
    @DisplayName("Winner Determination Tests")
    class WinnerDeterminationTests {

        @Test
        @DisplayName("Player should win with Blackjack when dealer doesn't have Blackjack")
        void playerShouldWinWithBlackjack() {
            Game game = Game.create(defaultPlayer().getId(), deckForPlayerBlackjack());
            assertEquals(GameStatus.PLAYER_BLACKJACK, game.getStatus());
        }

        @Test
        @DisplayName("Player should win when dealer busts")
        void playerShouldWinWhenDealerBusts() {
            Game game = Game.create(defaultPlayer().getId(), deckForDealerBust());
            game.placeBet(Money.of(50.0));
            game.stand();
            assertEquals(GameStatus.PLAYER_WIN, game.getStatus());
        }

        @Test
        @DisplayName("Should tie when both have same score")
        void shouldTieWhenBothHaveSameScore() {
            Game game = completedGameTie();
            assertEquals(GameStatus.TIE, game.getStatus());
        }
    }

    @Nested
    @DisplayName("Calculate Winnings Tests")
    class CalculateWinningsTests {

        @Test
        @DisplayName("Should return 2.5x bet for Blackjack")
        void shouldReturn2point5xBetForBlackjack() {
            Game game = completedGamePlayerBlackjack();
            // [CAMBIO]: Sincronizado con el multiplicador 2.5
            assertEquals(game.getBet().multiply(2.5), game.calculateWinnings());
        }

        @Test
        @DisplayName("Should return 2x bet for normal win")
        void shouldReturn2xBetForNormalWins() {
            Game game = completedGamePlayerWins();
            assertEquals(game.getBet().multiply(2), game.calculateWinnings());
        }

        @Test
        @DisplayName("Should return bet amount for tie")
        void shouldReturnBetAmountForTie() {
            Game game = completedGameTie();
            assertEquals(game.getBet(), game.calculateWinnings());
        }

        @Test
        @DisplayName("Should return zero for dealer Win")
        void shouldReturnZeroForDealerWin() {
            Game game = completedGameDealerWins();
            assertEquals(Money.zero(), game.calculateWinnings());
        }

        @Test
        @DisplayName("Should throw exception when calculating winnings for in-progress game")
        void shouldThrowExceptionWhenCalculatingWinningsForInProgressGame() {
            Game game = gameInProgress();
            assertThrows(ValidateGameException.class, game::calculateWinnings);
        }
    }

    @Nested
    @DisplayName("Game Status Tests")
    class GameStateTests {

        @Test
        @DisplayName("Game should be finished when player busts")
        void gameShouldBeFinishedWhenPlayerBusts() {
            Game game = gamePlayerBusted();
            assertTrue(game.isFinished());
        }

        @Test
        @DisplayName("Game should NOT be finished when in progress")
        void gameShouldNotBeFinishedWhenInProgress() {
            Game game = gameInProgress();
            assertFalse(game.isFinished());
        }
    }
}