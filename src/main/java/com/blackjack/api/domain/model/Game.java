package com.blackjack.api.domain.model;


import com.blackjack.api.domain.enums.GameStatus;
import com.blackjack.api.domain.exception.domain.NegativeDomainException;
import com.blackjack.api.domain.exception.domain.NullDomainException;
import com.blackjack.api.domain.exception.domain.ValidateGameException;
import com.blackjack.api.domain.valueobject.GameId;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;
import com.blackjack.api.domain.valueobject.Score;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Game {

    @EqualsAndHashCode.Include
    private final GameId id;
    private final PlayerId playerId;
    private final LocalDateTime createdAt;
    private final Hand playerHand;
    private final Hand dealerHand;
    private final Deck deck;
    private Money bet;
    private GameStatus status;

    @Builder(toBuilder = true)
    private Game(GameId id,
                 PlayerId playerId,
                 LocalDateTime createdAt,
                 Hand playerHand,
                 Hand dealerHand,
                 Deck deck,
                 Money bet,
                 GameStatus status) {
        this.id = id;
        this.playerId = playerId;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.playerHand = playerHand;
        this.dealerHand = dealerHand;
        this.deck = deck;
        this.bet = bet != null ? bet : Money.zero();
        this.status = status != null ? status : GameStatus.IN_PROGRESS;
    }


    public static Game create(PlayerId playerId, Deck deck) {
        validateInitialCreation(playerId, deck);

        Hand playerHand = Hand.empty();
        Hand dealerHand = Hand.empty();
        dealInitialCards(playerHand, dealerHand, deck);

        return Game.builder()
                .id(GameId.generate())
                .playerId(playerId)
                .playerHand(playerHand)
                .dealerHand(dealerHand)
                .deck(deck)
                .status(evaluateInitialStatus(playerHand, dealerHand))
                .build();
    }

    public void placeBet(Money betAmount) {
        ensureGameIsInProgress();

        if (betAmount == null || betAmount.isZeroOrLess()) {
            throw new NegativeDomainException("The bet must be greater than zero");
        }
        this.bet = betAmount;
    }

    public void hit() {
        ensureGameIsInProgress();
        ensureBetIsPlaced();

        playerHand.addCard(deck.deal());

        if (playerHand.isBusted()) {
            this.status = GameStatus.PLAYER_BUSTED;
        }
    }

    public void stand() {
        ensureGameIsInProgress();
        ensureBetIsPlaced();

        playDealerTurn();
        this.status = determineFinalStatus();
    }

    public boolean isFinished() {
        return status != GameStatus.IN_PROGRESS;
    }

    public Money calculateWinnings() {
        if (!isFinished()) {
            throw new ValidateGameException("The game is not finished");
        }

        return switch (status) {
            case PLAYER_BLACKJACK -> bet.multiply(2.5);
            case PLAYER_WIN -> bet.multiply(2.0);
            case TIE -> bet;
            default -> Money.zero();
        };
    }

    private void playDealerTurn() {
        while (dealerHand.calculateScore().getValue() < 17) {
            dealerHand.addCard((deck.deal()));
        }
    }

    private GameStatus determineFinalStatus() {
        if (dealerHand.isBusted()) {
            return GameStatus.PLAYER_WIN;
        }

        Score playerScore = playerHand.calculateScore();
        Score dealerScore = dealerHand.calculateScore();

        if (playerScore.beats(dealerScore)) return GameStatus.PLAYER_WIN;
        if (dealerScore.beats(playerScore)) return GameStatus.DEALER_WIN;
        return GameStatus.TIE;
    }

    private static void dealInitialCards(Hand playerHand, Hand dealerHand, Deck deck) {
        playerHand.addCard(deck.deal());
        dealerHand.addCard(deck.deal());
        playerHand.addCard(deck.deal());
        dealerHand.addCard(deck.deal());
    }

    private static GameStatus evaluateInitialStatus(Hand playerHand, Hand dealerHand) {
        if (playerHand.isBlackjack() && dealerHand.isBlackjack()) return GameStatus.TIE;
        if (playerHand.isBlackjack()) return GameStatus.PLAYER_BLACKJACK;
        if (dealerHand.isBlackjack()) return GameStatus.DEALER_WIN;
        return GameStatus.IN_PROGRESS;
    }

    private void ensureGameIsInProgress() {
        if (isFinished()) {
            throw new ValidateGameException("Action not allowed. The game is already finished.");
        }
    }

    private void ensureBetIsPlaced() {
        if (bet.isZero()) {
            throw new ValidateGameException("A bet must be placed before playing.");
        }
    }

    private static void validateInitialCreation(PlayerId playerId, Deck deck) {
        if (playerId == null) throw new NullDomainException("The player cannot be null.");
        if (deck == null) throw  new NullDomainException("The deck cannot be null.");
    }


}