package com.blackjack.api.infrastructure.adapter.out.persistence.mongo;

import com.blackjack.api.application.port.out.GameRepository;
import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.valueobject.GameId;
import com.blackjack.api.domain.valueobject.PlayerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GameMongoAdapter implements GameRepository {

    private final SpringDataGameRepository springDataGameRepository;
    private final GameMapper mapper;

    public GameMongoAdapter(SpringDataGameRepository springDataGameRepository,
                            GameMapper mapper) {
        this.springDataGameRepository = springDataGameRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Game> save(Game game) {
        log.debug("Saving game in MongoDB: {}", game.getId());

        GameDocument document = mapper.toDocument(game);

        return springDataGameRepository.save(document)
                .map(mapper::toDomain)
                .doOnSuccess(g -> log.debug("Game saved: {}", g.getId()));
    }

    @Override
    public Mono<Game> findById(GameId gameId) {
        log.debug("Searching game with Id: {}", gameId.value());

        return springDataGameRepository.findById(gameId.value())
                .map(mapper::toDomain)
                .doOnSuccess(g -> log.debug("Game found: {}", g.getId()));
    }

    @Override
    public Flux<Game> findByPlayerId(PlayerId playerId) {
        log.debug("Searching for player games: {}", playerId);

        return springDataGameRepository.findByPlayerId(playerId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Game> findAll() {
        log.debug("Searching all games");

        return springDataGameRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(GameId gameId) {
        log.debug("Eliminating game: {}", gameId);

        return springDataGameRepository.deleteById(gameId.value())
                .doOnSuccess(v -> log.debug("Game deleted: {}", gameId));
    }

    @Override
    public Mono<Boolean> existsById(GameId gameId) {
        log.debug("Checking game existence: {}", gameId);

        return springDataGameRepository.existsById(gameId.value());
    }
}
