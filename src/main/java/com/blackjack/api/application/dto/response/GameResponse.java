package com.blackjack.api.application.dto.response;

import com.blackjack.api.domain.enums.GameStatus;
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
       if (game == null) return null;
       
       List<CardResponse> playerCards = (game.getPlayerHand() != null && game.getPlayerHand().getCards() != null)
               ? game.getPlayerHand().getCards().stream().map(CardResponse::from).toList()
               : List.of();

       List<CardResponse> dealerCards = (game.getDealerHand() != null && game.getDealerHand().getCards() != null)
               ? game.getDealerHand().getCards().stream().map(CardResponse::from).toList()
               : List.of();

       return new GameResponse(
               game.getId() != null ? game.getId().value() : "N/A",
               game.getPlayerId() != null ? game.getPlayerId().value() : "N/A",
               playerCards,
               dealerCards,
               game.getPlayerHand() != null ? game.getPlayerHand().calculateScore().getValue() : 0,
               game.getDealerHand() != null ? game.getDealerHand().calculateScore().getValue() : 0,
               game.getBet() != null ? game.getBet().getAmount().doubleValue() : 0.0,
               game.getStatus(),
               game.getCreatedAt() != null ? game.getCreatedAt() : LocalDateTime.now(),
               game.getDeck() != null ? game.getDeck().remainingCards() : 0
       );
   }
}
