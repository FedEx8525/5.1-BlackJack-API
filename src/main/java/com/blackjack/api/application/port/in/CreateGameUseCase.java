package com.blackjack.api.application.port.in;

import com.blackjack.api.application.dto.command.CreateGameCommand;
import com.blackjack.api.application.dto.response.GameResponse;
import reactor.core.publisher.Mono;

public interface CreateGameUseCase {
    Mono<GameResponse> execute(CreateGameCommand command);
}
