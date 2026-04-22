package com.blackjack.api.application.service;

import com.blackjack.api.application.dto.command.UpdatePlayerNameCommand;
import com.blackjack.api.application.dto.query.GetRankingQuery;
import com.blackjack.api.application.dto.response.PlayerResponse;
import com.blackjack.api.application.dto.response.RankingResponse;
import com.blackjack.api.application.port.in.GetRankingUseCase;
import com.blackjack.api.application.port.in.UpdatePlayerNameUseCase;
import com.blackjack.api.application.port.out.PlayerRepository;
import com.blackjack.api.domain.exception.application.PlayerNotFoundException;
import com.blackjack.api.domain.valueobject.PlayerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class PlayerService implements
        GetRankingUseCase,
        UpdatePlayerNameUseCase {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    @Override
    @Transactional
    public Mono<RankingResponse> execute(GetRankingQuery query) {
        log.info("Getting player ranking list");

        AtomicInteger position = new AtomicInteger(1);

        return playerRepository.findAllOrderedByWinRate()
                .map(player -> new RankingResponse.PlayerRankingEntry(
                        position.getAndIncrement(),
                        player.getId().value(),
                        player.getName(),
                        player.getGamesWon(),
                        player.getGamesPlayed(),
                        player.getWinRate()
                ))
                .collectList()
                .map(RankingResponse::new)
                .doOnSuccess(ranking ->
                        log.info("Ranking obtained with {} players", ranking.ranking().size()));
    }

    @Override
    @Transactional
    public Mono<PlayerResponse> execute(UpdatePlayerNameCommand command) {
        log.info("Updating player {} name  to {}", command.playerId(), command.newName());

        PlayerId playerId = PlayerId.from(command.playerId());

        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException(playerId)))
                .flatMap(player -> {
                    player.updateName(command.newName());

                    return playerRepository.save(player);
                })
                .doOnSuccess(player -> log.info("Name successfully updated"))
                .map(PlayerResponse::from);

    }

}
