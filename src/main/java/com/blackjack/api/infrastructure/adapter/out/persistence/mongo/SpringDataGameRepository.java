package com.blackjack.api.infrastructure.adapter.out.persistence.mongo;

import com.blackjack.api.domain.enums.GameStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface SpringDataGameRepository  extends ReactiveMongoRepository<GameDocument, String> {

    Flux<GameDocument> findByPlayerId(String playerId);

    Flux<GameDocument> findByStatus(GameStatus status);
}
