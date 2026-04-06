package com.blackjack.api.application.dto.response;

import com.blackjack.api.domain.model.Player;

public record PlayerResponse(
        String playerId,
        String name,
        double balance,
        int gamesPlayed,
        int gameWon,
        int gameLost,
        double winRate
) {

    public static PlayerResponse from(Player player) {
        return new PlayerResponse(player.getId().getValue(),
        player.getName(),
        player.getBalance().getAmount().doubleValue(),
        player.getGamesPlayed(),
        player.getGamesWon(),
        player.getGamesLost(),
        player.getWinRate());
    }
}
