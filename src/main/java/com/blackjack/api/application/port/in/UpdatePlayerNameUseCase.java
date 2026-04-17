package com.blackjack.api.application.port.in;

import com.blackjack.api.application.dto.command.UpdatePlayerNameCommand;
import com.blackjack.api.application.dto.response.PlayerResponse;
import reactor.core.publisher.Mono;

public interface UpdatePlayerNameUseCase {
    Mono<PlayerResponse> execute(UpdatePlayerNameCommand command);
}
