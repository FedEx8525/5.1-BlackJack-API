package com.blackjack.api.application.port.out;

import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.valueobject.PlayerId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository {

    Mono<Player> save(Player player);
    Mono<Player> findById(PlayerId playerId);
    Mono<Player> findByName(String name);
    Flux<Player> findAllOrderedByWinRate();
    Flux<Player> findAll();
    Mono<Void> deleteById(PlayerId playerId);
    Mono<Boolean> existsById(PlayerId playerId);
    Mono<Boolean> existsByName(String name);
}
