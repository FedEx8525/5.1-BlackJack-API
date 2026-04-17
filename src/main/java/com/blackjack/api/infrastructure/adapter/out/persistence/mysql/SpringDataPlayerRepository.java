package com.blackjack.api.infrastructure.adapter.out.persistence.mysql;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpringDataPlayerRepository extends ReactiveCrudRepository<PlayerEntity, String> {

    Mono<PlayerEntity> findByName(String name);

    Mono<Boolean> existsByName(String name);

    @Query("SELECT * FROM players ORDER BY (" +
    "CASE WHEN games_played = 0 THEN 0 " +
    "ELSE CAST(games_won AS DECIMAL) / games_played END) DESC")
    Flux<PlayerEntity> findAllOrderedByWinRate();
}
