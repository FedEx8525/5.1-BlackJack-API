package com.blackjack.api.application.port.in;

import reactor.core.publisher.Mono;

public interface DeleteGameUseCase {
    Mono<Void> execute(String gameId);
}
