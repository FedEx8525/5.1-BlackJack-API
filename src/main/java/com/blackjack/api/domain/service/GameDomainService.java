package com.blackjack.api.domain.service;

import com.blackjack.api.domain.enums.PlayAction;
import com.blackjack.api.domain.exception.InsufficientBalanceException;
import com.blackjack.api.domain.exception.InvalidPlayException;
import com.blackjack.api.domain.exception.NullDomainException;
import com.blackjack.api.domain.exception.ValidateGameException;
import com.blackjack.api.domain.model.Deck;
import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;
import org.springframework.stereotype.Service;

@Service
public class GameDomainService {

    private final BetValidator betValidator;

    public GameDomainService(BetValidator betValidator) {
        this.betValidator = betValidator;
    }

    public Game createNewGame(PlayerId playerId) {
        if (playerId == null) {
            throw new NullDomainException("PlayerId cannot be null");
        }
        Deck deck = Deck.createStandardDeck();
        deck.shuffle();

        return Game.create(playerId, deck);
    }

    public void processBet(Game game, Player player, Money betAmount) {

        betValidator.validate(betAmount);

        if (!player.canBet(betAmount)) {
            throw new InsufficientBalanceException("Insufficient balance. You cannot bet " + betAmount + "€. " +
                    "You have just " + player.getBalance() + "€");
        }

        player.placeBet(betAmount);

        game.placeBet(betAmount);
    }

    public void executeAction(Game game, Player player, PlayAction action) {
        if (game.isFinished()) {
            throw new InvalidPlayException("The game is finish");
        }

        switch (action) {
            case HIT -> game.hit();
            case STAND -> game.stand();
            default -> throw new InvalidPlayException("Unknowing action: " + action);
        }
    }

    public Money resolveGame(Game game, Player player) {
        if (!game.isFinished()) {
            throw new ValidateGameException("The game still in progress. You cannot resolve it.");
        }
        Money winnings = game.calculateWinnings();

        switch (game.getStatus()) {
            case PLAYER_WIN, PLAYER_BLACKJACK -> player.win(winnings);
            case DEALER_WIN, PLAYER_BUSTED -> player.lose();
            case TIE -> player.tie(game.getBet());
        }

        return winnings;
    }
}
