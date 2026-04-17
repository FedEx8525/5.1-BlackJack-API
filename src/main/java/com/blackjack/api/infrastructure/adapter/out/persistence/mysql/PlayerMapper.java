package com.blackjack.api.infrastructure.adapter.out.persistence.mysql;

import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerEntity toEntity(Player player) {
        return createEntity(player, true);
    }

    public PlayerEntity toUpdateEntity(Player player) {
        return createEntity(player, false);
    }

    public PlayerEntity createEntity(Player player, boolean isNew) {
        return PlayerEntity.builder()
                .id(player.getId().value())
                .name(player.getName())
                .balance(player.getBalance().getAmount())
                .gamesPlayed(player.getGamesPlayed())
                .gamesWon(player.getGamesWon())
                .gamesLost(player.getGamesLost())
                .isNew(isNew)
                .build();
    }

    public Player toDomain(PlayerEntity entity) {
        return Player.builder()
                .id(PlayerId.from(entity.getId()))
                .name(entity.getName())
                .balance(Money.of(entity.getBalance()))
                .gamesPlayed(entity.getGamesPlayed())
                .gamesWon(entity.getGamesWon())
                .gamesLost(entity.getGamesLost())
                .build();
    }
}
