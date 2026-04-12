package com.blackjack.api.infrastructure.adapter.out.persistence.mysql;

import com.blackjack.api.application.port.out.PlayerRepository;
import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.valueobject.PlayerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PlayerMySQLAdapter implements PlayerRepository {

    private final SpringDataPlayerRepository springDataPlayerRepository;
    private final PlayerMapper mapper;

    public PlayerMySQLAdapter(SpringDataPlayerRepository springDataPlayerRepository,
                              PlayerMapper mapper) {
        this.springDataPlayerRepository = springDataPlayerRepository;
        this.mapper = mapper;
    }


    @Override
    public Mono<Player> save(Player player) {
        log.debug("Saving player in MySQL: {}", player.getId());

        PlayerEntity entity = mapper.toEntity(player);

        return springDataPlayerRepository.save(entity)
                .map(mapper::toDomain)
                .doOnSuccess(p -> log.debug("Player saved: {}", p.getId()));
    }

    @Override
    public Mono<Player> findById(PlayerId playerId) {
        log.debug("Searching player by Id: {}", playerId);

        return springDataPlayerRepository.findById(playerId.value())
                .map(mapper::toDomain)
                .doOnSuccess(p -> log.debug("Payer found: {}", p.getName()));
    }

    @Override
    public Mono<Player> findByName(String name) {
        log.debug("Searching player by name: {}", name);

        return springDataPlayerRepository.findByName(name)
                .map(mapper::toDomain)
                .doOnSuccess(p -> log.debug("Player found: {}", p.getName()));
    }

    @Override
    public Flux<Player> findAllOrderedByWinRate() {
        log.debug("Obtaining player rankings");

        return springDataPlayerRepository.findAllOrderedByWinRate()
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Player> findAll() {
        log.debug("Searching all players");

        return springDataPlayerRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(PlayerId playerId) {
        log.debug("Eliminating player: {}", playerId);

        return springDataPlayerRepository.deleteById(playerId.value())
                .doOnSuccess(v -> log.debug("Player deleted: {}", playerId));
    }

    @Override
    public Mono<Boolean> existsById(PlayerId playerId) {
        log.debug("Checking player existence: {}", playerId);

        return springDataPlayerRepository.existsById(playerId.value());
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        log.debug("Checking player existence by name: {}", name);

        return springDataPlayerRepository.existsByName(name);
    }
}
