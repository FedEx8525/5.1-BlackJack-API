package com.blackjack.api.application.port.in;

import com.blackjack.api.application.dto.query.GetRankingQuery;
import com.blackjack.api.application.dto.response.RankingResponse;
import reactor.core.publisher.Mono;

public interface GetRankingUseCase {
    Mono<RankingResponse> execute(GetRankingQuery query);
}
