package com.blackjack.api.domain.model;


import com.blackjack.api.domain.enums.GameStatus;
import com.blackjack.api.domain.exception.NegativeDomainException;
import com.blackjack.api.domain.exception.NullDomainException;
import com.blackjack.api.domain.exception.ValidateGameException;
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
@Builder(toBuilder = true)
public class Game {

    @EqualsAndHashCode.Include
    private final GameId id;

    private final PlayerId playerId;

    @Builder.Default
    private  final LocalDateTime createdAt = LocalDateTime.now();

    private Hand playerHand;
    private Hand dealerHand;
    private Deck deck;

    @Builder.Default
    private Money bet = Money.zero();

    @Builder.Default
    private GameStatus status = GameStatus.IN_PROGRESS;

    public static Game create(PlayerId playerId, Deck deck) {
        if (playerId == null) {
            throw new NullDomainException("The playerId cannot be null!");
        }
        if (deck == null) {
            throw  new NullDomainException("The deck cannot be null");
        }

        Hand playerHand = Hand.empty();
        Hand dealerHand = Hand.empty();

        playerHand.addCard(deck.deal());
        dealerHand.addCard(deck.deal());
        playerHand.addCard(deck.deal());
        dealerHand.addCard(deck.deal());

        return Game.builder()
                .id(GameId.generate())
                .playerId(playerId)
                .playerHand(playerHand)
                .dealerHand(dealerHand)
                .deck(deck)
                .build();
    }

    public void placeBet(Money betAmount) {
        if(status != GameStatus.IN_PROGRESS) {
            throw new ValidateGameException("You cannot bet. The game is finish");
        }
        if(betAmount == null || betAmount.equals(Money.zero())) {
            throw new NegativeDomainException("The bet must be greater than zero");
        }
        this.bet = betAmount;
    }

    public void hit() {
        validateGameInProgress();
        playerHand.addCard(deck.deal());

        if (playerHand.isBusted()) {
            this.status = GameStatus.PLAYER_BUSTED;
        }
    }

    public void stand() {
        validateGameInProgress();

        while (dealerHand.calculateScore().getValue() < 17) {
            dealerHand.addCard(deck.deal());
        }

        determinateWinner();
    }

    public  void determinateWinner() {
        Score playerScore = playerHand.calculateScore();
        Score dealerScore = dealerHand.calculateScore();

        if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            this.status = GameStatus.PLAYER_BLACKJACK;
            return;
        }

        if (playerScore.isBusted()) {
            this.status = GameStatus.PLAYER_BUSTED;
            return;
        }

        if (dealerScore.isBusted() || playerScore.beats(dealerScore)) {
            this.status = GameStatus.PLAYER_WIN;
            return;
        }

        if (playerScore.beats(dealerScore)) {
            this.status = GameStatus.PLAYER_WIN;
        } else if (dealerScore.beats(playerScore)) {
            this.status = GameStatus.DEALER_WIN;
        } else {
            this.status = GameStatus.TIE;
        }
    }

    private void validateGameInProgress() {
        if (status != GameStatus.IN_PROGRESS) throw new ValidateGameException("Game Over");
        if (bet.equals(Money.zero())) throw new ValidateGameException("Bet cannot be zero");
    }

    public boolean isFinished() {
        return status != GameStatus.IN_PROGRESS;
    }

    public Money calculateWinnings() {
        return switch (status) {
            case PLAYER_BLACKJACK -> bet.multiply(2).add(bet.multiply(1));
            case PLAYER_WIN -> bet.multiply(2);
            case TIE -> bet;
            case DEALER_WIN, PLAYER_BUSTED -> Money.zero();
            case IN_PROGRESS -> throw new ValidateGameException("The game is not finish");
        };
    }
}
