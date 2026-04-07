package com.blackjack.api.application.port.in;

import com.blackjack.api.application.dto.command.PlayCommand;
import com.blackjack.api.application.dto.response.GameResponse;
import reactor.core.publisher.Mono;

public interface PlayGameUseCase {
    Mono<GameResponse> execute(PlayCommand command);
}
