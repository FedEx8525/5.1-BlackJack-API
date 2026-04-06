package com.blackjack.api.application.dto.response;

import java.util.List;

public record RankingResponse(
        List<PlayerRankingEntry> ranking
) {

    public record PlayerRankingEntry(
        int position,
        String playerId,
        String name,
        int gamesWon,
        int gamesPlayed,
        double winRate
    ){}
}
