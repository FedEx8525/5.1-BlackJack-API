package com.blackjack.api.application.port.in;

import com.blackjack.api.application.dto.query.GetGameQuery;
import com.blackjack.api.application.dto.response.GameResponse;
import reactor.core.publisher.Mono;

public interface GetGameUseCase {
    Mono<GameResponse> execute(GetGameQuery query);
}
