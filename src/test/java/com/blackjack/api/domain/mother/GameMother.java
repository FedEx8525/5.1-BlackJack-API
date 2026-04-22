package com.blackjack.api.mother;

import com.blackjack.api.domain.enums.GameStatus;
import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.valueobject.GameId;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;

import static com.blackjack.api.mother.DeckMother.*;
import static com.blackjack.api.mother.HandMother.*;

public class GameMother {

    public static Game newGame() {
        return Game.create(
                PlayerId.generate(),
                standardDeck()
        );
    }

    public static Game newGameWithPlayerId(PlayerId playerId) {
        return Game.create(playerId, shuffleDeck());
    }

    public static Game gameInProgress() {
        Game game = Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(handWithValue(15))
                .dealerHand(handWithValue(10))
                .deck(shuffleDeck())
                .bet(Money.of(50.0))
                .status(GameStatus.IN_PROGRESS)
                .build();
        return game;
    }

    public static Game gameWithBet(double betAmount) {
        Game game = Game.create(PlayerId.generate(), shuffleDeck());
        game.placeBet(Money.of(betAmount));
        return game;
    }

    public static Game gamePlayerWon() {
        Game game = Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(handWithValue(20))
                .dealerHand(handWithValue(18))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.PLAYER_WIN)
                .build();
        return game;
    }

    public static Game gameDealerWon() {
        Game game = Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(handWithValue(18))
                .dealerHand(handWithValue(20))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.DEALER_WIN)
                .build();
        return game;
    }

    public static Game gameReadyForStand() {
        Game game = Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(handWithValue(19))
                .dealerHand(dealerMustHit())
                .deck(highCardsDeck())
                .bet(Money.of(50.0))
                .status(GameStatus.IN_PROGRESS)
                .build();
        return game;
    }

    public static Game completedGameTie() {
        return Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(handWithValue(20))
                .dealerHand(handWithValue(20))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.TIE)
                .build();
    }

    public static Game gamePlayerBusted() {
        Game game = Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(bustedHand())
                .dealerHand(handWithValue(18))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.PLAYER_BUSTED)
                .build();
        return game;
    }

    public static Game completedGamePlayerBlackjack() {
        return Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(blackjackHand())
                .dealerHand(handWithValue(20))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.PLAYER_BLACKJACK)
                .build();
    }

    public static Game completedGamePlayerWins() {
        return Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(handWithValue(20))
                .dealerHand(handWithValue(19))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.PLAYER_WIN)
                .build();
    }

    public static Game completedGameDealerWins() {
        return Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(handWithValue(18))
                .dealerHand(handWithValue(21))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.DEALER_WIN)
                .build();
    }

    public static Game completedGamePlayerBusted() {
        return Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(bustedHand())
                .dealerHand(handWithValue(18))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.PLAYER_BUSTED)
                .build();
    }

    public static Game gameTie() {
        Game game = Game.builder()
                .id(GameId.generate())
                .playerId(PlayerId.generate())
                .playerHand(handWithValue(19))
                .dealerHand(handWithValue(19))
                .deck(emptyDeck())
                .bet(Money.of(100.0))
                .status(GameStatus.TIE)
                .build();
        return game;
    }


}
