package com.blackjack.api.domain.service;

import com.blackjack.api.domain.enums.PlayAction;
import com.blackjack.api.domain.exception.domain.*;
import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.blackjack.api.mother.GameMother.*;
import static com.blackjack.api.mother.PlayerMother.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GameDomainService Tests")
class GameDomainServiceTest {

    @Mock
    private BetValidator betValidator;

    @InjectMocks
    private GameDomainService gameDomainService;

    @Nested
    @DisplayName("Create New Game Tests")
    class CreateNewGameTests {

        @Test
        @DisplayName("Should create new game with valid player ID")
        void shouldCreateNewGameWithValidPlayerId() {
            Player player = defaultPlayer();

            Game game = gameDomainService.createNewGame(player.getId());

            assertNotNull(game);
            assertEquals(player.getId(), game.getPlayerId());
            assertEquals(2, game.getPlayerHand().size());
            assertEquals(2, game.getDealerHand().size());
            assertTrue(game.getDeck().hasCards());
        }

        @Test
        @DisplayName("Should throw exception when player ID is null")
        void shouldThrowExceptionWhenPlayerIdIsNull() {
            assertThrows(NullDomainException.class, () ->
                    gameDomainService.createNewGame(null));
        }

        @Test
        @DisplayName("Created game should have shuffled deck")
        void createdGameShouldHaveShuffledDeck() {
            Player player = defaultPlayer();

            Game game = gameDomainService.createNewGame(player.getId());

            assertTrue(game.getDeck().remainingCards() < 52);
            assertEquals(48, game.getDeck().remainingCards());
        }
    }

    @Nested
    @DisplayName("Process Bet Tests")
    class ProcessBetTests {

        @Test
        @DisplayName("Should process bet successfully with valid amount")
        void shouldProcessBetSuccessfullyWithValidAmount() {
            Game game = newGame();
            Player player = richPlayer();
            Money betAmount = Money.of(100.0);
            Money initialBalance = player.getBalance();

            doNothing().when(betValidator).validate(betAmount);

            gameDomainService.processBet(game, player, betAmount);

            verify(betValidator).validate(betAmount);
            assertEquals(betAmount, game.getBet());
            assertEquals(initialBalance.subtract(betAmount), player.getBalance());
        }

        @Test
        @DisplayName("Should throw exception when bet is invalid")
        void shouldThrowExceptionWhenBetIsInvalid() {
            Game game = newGame();
            Player player = richPlayer();
            Money invalidBet = Money.of(5.0);

            doThrow(new InvalidBetException("Invalid bet"))
                    .when(betValidator).validate(invalidBet);

            assertThrows(InvalidBetException.class, () ->
                    gameDomainService.processBet(game, player, invalidBet));
        }

        @Test
        @DisplayName("Should throw exception when player cannot afford bet")
        void shouldThrowExceptionWhenPlayerCannotAffordBet() {
            Game game = newGame();
            Player player = poorPlayer();
            Money betAmount = Money.of(100.0);

            doNothing().when(betValidator).validate(betAmount);

            assertThrows(InsufficientBalanceException.class, () ->
                    gameDomainService.processBet(game, player, betAmount));
        }

        @Test
        @DisplayName("Should deduct bet from player balance")
        void shouldDeductBetFromPlayerBalance() {
            Game game = newGame();
            Player player = playerWithBalance(1000.0);
            Money betAmount = Money.of(100.0);

            doNothing().when(betValidator).validate(betAmount);

            gameDomainService.processBet(game, player, betAmount);

            assertEquals(Money.of(900.0), player.getBalance());
        }

        @Test
        @DisplayName("Should set bet in game")
        void shouldSetBetInGame() {
            Game game = newGame();
            Player player = richPlayer();
            Money betAmount = Money.of(100.0);

            doNothing().when(betValidator).validate(betAmount);

            gameDomainService.processBet(game, player, betAmount);

            assertEquals(betAmount, game.getBet());
        }

        @Test
        @DisplayName("Should allow player to bet all their balance")
        void shouldAllowPlayerToBetAllTheirBalance() {
            Money balance = Money.of(100.0);
            Player player = playerWithBalance(100.0);
            Game game = newGame();

            doNothing().when(betValidator).validate(balance);

            assertDoesNotThrow(() ->
                    gameDomainService.processBet(game, player, balance));

            assertEquals(Money.zero(), player.getBalance());
        }
    }

    @Nested
    @DisplayName("Execute Action Tests")
    class ExecuteActionTests {

        @Test
        @DisplayName("Should execute HIT action")
        void shouldExecuteHitAction() {
            Game game = gameWithBet(50.0);
            Player player = defaultPlayer();
            int initialHandSize = game.getPlayerHand().size();

            gameDomainService.executeAction(game, player, PlayAction.HIT);

            assertEquals(initialHandSize + 1, game.getPlayerHand().size());
        }

        @Test
        @DisplayName("Should execute STAND action")
        void shouldExecuteStandAction() {
            Game game = gameWithBet(50.0);
            Player player = defaultPlayer();

            gameDomainService.executeAction(game, player, PlayAction.STAND);

            assertTrue(game.isFinished());
        }

        @Test
        @DisplayName("Should throw exception when executing action on finished game")
        void shouldThrowExceptionWhenExecutingActionOnFinishedGame() {
            Game game = gamePlayerWon();
            Player player = defaultPlayer();

            assertThrows(InvalidPlayException.class, () ->
                    gameDomainService.executeAction(game, player, PlayAction.HIT));
        }

        @Test
        @DisplayName("Should throw exception for unknown action")
        void shouldThrowExceptionForUnknownAction() {
            Game game = gameWithBet(50.0);
            Player player = defaultPlayer();

            assertThrows(InvalidPlayException.class, () ->
                    gameDomainService.executeAction(game, player, PlayAction.DOUBLE));
        }
    }

    @Nested
    @DisplayName("Resolve Game Tests")
    class ResolveGameTests {

        @Test
        @DisplayName("Should throw exception when resolving unfinished game")
        void shouldThrowExceptionWhenResolvingUnfinishedGame() {
            Game game = gameInProgress();
            Player player = defaultPlayer();

            assertThrows(ValidateGameException.class, () ->
                    gameDomainService.resolveGame(game, player));
        }

        @Test
        @DisplayName("Should resolve player win correctly")
        void shouldResolvePlayerWinCorrectly() {
            Game game = completedGamePlayerWins();
            Player player = defaultPlayer();
            Money initialBalance = player.getBalance();

            Money winnings = gameDomainService.resolveGame(game, player);

            assertEquals(game.getBet().multiply(2), winnings);
            assertEquals(initialBalance.add(winnings), player.getBalance());
        }
    }

}
