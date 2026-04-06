package com.blackjack.api.application.dto.response;

import com.blackjack.api.domain.model.Card;

public record CardResponse(
        String rank,
        String suit,
        String display
) {

    public static CardResponse from(Card card) {
        return new CardResponse(
                card.getRank().name(),
                card.getSuit().name(),
                card.toString()
        );
    }
}
