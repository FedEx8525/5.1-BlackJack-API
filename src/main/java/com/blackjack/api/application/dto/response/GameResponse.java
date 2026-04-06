package com.blackjack.api.application.dto.response;

import com.blackjack.api.domain.enums.GameStatus;
import com.blackjack.api.domain.model.Card;
import com.blackjack.api.domain.model.Game;

import java.time.LocalDateTime;
import java.util.List;

public record GameResponse(
        String gameId,
        String playerId,
        List<CardResponse> playerHand,
        List<CardResponse> dealerHand,
        int playerScore,
        int dealerScore,
        double bet,
        GameStatus status,
        LocalDateTime createdAt,
        int remainingCards
) {

    public static GameResponse from(Game game) {

        List<CardResponse> playerCards = game.getPlayerHand().getCards().stream()
                        .map(CardResponse::from)
                                .toList();

        List<CardResponse> dealerCards = game.getDealerHand().getCards().stream()
                        .map(CardResponse::from)
                                .toList();

        return new GameResponse(
                game.getId().getValue(),
                game.getPlayerId().getValue(),
                playerCards,
                dealerCards,
                game.getPlayerHand().calculateScore().getValue(),
                game.getDealerHand().calculateScore().getValue(),
                game.getBet().getAmount().doubleValue(),
                game.getStatus(),
                game.getCreatedAt(),
                game.getDeck().remainingCards());

    }
}
