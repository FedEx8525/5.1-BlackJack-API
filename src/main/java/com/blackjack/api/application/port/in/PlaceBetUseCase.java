package com.blackjack.api.application.port.in;

import com.blackjack.api.application.dto.command.PlaceBetCommand;
import com.blackjack.api.application.dto.response.GameResponse;
import reactor.core.publisher.Mono;

public interface PlaceBetUseCase {
    Mono<GameResponse> execute(PlaceBetCommand command);
}
