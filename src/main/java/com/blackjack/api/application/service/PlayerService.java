package com.blackjack.api.application.service;

import com.blackjack.api.application.dto.query.GetRankingQuery;
import com.blackjack.api.application.dto.response.RankingResponse;
import com.blackjack.api.application.port.in.GetRankingUseCase;
import com.blackjack.api.application.port.out.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class PlayerService implements
        GetRankingUseCase {

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
                        player.getId().getValue(),
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

}
