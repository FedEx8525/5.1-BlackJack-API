package com.blackjack.api.application.port.out;

import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.valueobject.GameId;
import com.blackjack.api.domain.valueobject.PlayerId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameRepository {
    Mono<Game> save(Game game);
    Mono<Game> findById(GameId gameId);
    Flux<Game> findByPlayerId(PlayerId playerId);
    Flux<Game> findAll();
    Mono<Void> deleteById(GameId gameId);
    Mono<Boolean> existsById(GameId gameId);
}
