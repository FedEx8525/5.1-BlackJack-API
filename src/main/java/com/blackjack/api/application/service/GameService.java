package com.blackjack.api.application.service;

import com.blackjack.api.application.dto.command.CreateGameCommand;
import com.blackjack.api.application.dto.command.PlaceBetCommand;
import com.blackjack.api.application.dto.command.PlayCommand;
import com.blackjack.api.application.dto.query.GetGameQuery;
import com.blackjack.api.application.dto.response.GameResponse;
import com.blackjack.api.application.port.in.*;
import com.blackjack.api.application.port.out.GameRepository;
import com.blackjack.api.application.port.out.PlayerRepository;
import com.blackjack.api.domain.exception.application.GameNotFoundException;
import com.blackjack.api.domain.exception.application.PlayerNotFoundException;
import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.model.Player;
import com.blackjack.api.domain.service.GameDomainService;
import com.blackjack.api.domain.valueobject.GameId;
import com.blackjack.api.domain.valueobject.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GameService implements
        CreateGameUseCase,
        DeleteGameUseCase,
        GetGameUseCase,
        PlaceBetUseCase,
        PlayGameUseCase {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameDomainService gameDomainService;
    private final double initialBalance;

    public GameService(
            GameRepository gameRepository,
            PlayerRepository playerRepository,
            GameDomainService gameDomainService,
            @org.springframework.beans.factory.annotation.Value("${blackjack.game.initial-balance:1000}")
            double initialBalance
    ) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.gameDomainService = gameDomainService;
        this.initialBalance = initialBalance;
    }

    @Override
    @Transactional
    public Mono<GameResponse> execute(CreateGameCommand command) {
        log.info("Creating a new game for player " + command.playerName());

        return findOrCreatePlayer(command.playerName())
                .flatMap(player -> {
                    Game game = gameDomainService.createNewGame(player.getId());

                    return gameRepository.save(game);
                })

                .doOnNext(game -> log.info("Game created successfully with ID: {}", game.getId()))
                .map(GameResponse::from)
                .onErrorResume(e -> {
                    log.error("Error creating game for {}: {}", command.playerName(), e.getMessage());
                    return Mono.error(e);
                });
    }

    private Mono<Player> findOrCreatePlayer(String playerName) {
        return playerRepository.findByName(playerName)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Player not found. Creating new player: {}", playerName);
                    Player newPlayer = Player.create(
                            playerName,
                            Money.of(initialBalance)
                    );

                    return playerRepository.save(newPlayer);
                }));
    }

    @Override
    public Mono<GameResponse> execute(PlaceBetCommand command) {
        log.info("Processing bet of {} in game {}", command.betAmount(), command.gameId());

        GameId gameId = GameId.from(command.gameId());
        Money betAmount = Money.of(command.betAmount());

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(game -> playerRepository.findById(game.getPlayerId())
                        .switchIfEmpty(Mono.error(
                                new PlayerNotFoundException(game.getPlayerId())
                        ))
                        .flatMap(player -> {
                            gameDomainService.processBet(game, player, betAmount);

                            return playerRepository.save(player)
                                    .then(gameRepository.save(game));
                        })
                )
                .doOnSuccess(game -> log.info("Process bet: {}", betAmount))
                .map(GameResponse::from);
    }

    @Override
    @Transactional
    public Mono<GameResponse> execute(PlayCommand command) {
        log.info("Performing action {} in game {}", command.action(), command.gameId());
        GameId gameId = GameId.from(command.gameId());

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(game -> playerRepository.findById(game.getPlayerId())
                        .switchIfEmpty(Mono.error(new PlayerNotFoundException(game.getPlayerId())
                        ))
                        .flatMap(player -> {
                            gameDomainService.executeAction(game, player, command.action());

                            if (game.isFinished()) {
                                Money winnings = gameDomainService.resolveGame(game, player);
                                log.info("Game over. Status: {}, Winnings: {}", game.getStatus(), winnings);

                                return playerRepository.save(player)
                                        .then(gameRepository.save(game));
                            } else {
                                return playerRepository.save(player)
                                        .then(gameRepository.save(game));
                            }
                        })
                )
                .map(GameResponse::from);
    }

    @Override
    @Transactional
    public Mono<GameResponse> execute(GetGameQuery query) {
        log.info("Getting game details: {}", query.gameId());

        GameId gameId = GameId.from(query.gameId());

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .map(GameResponse::from);
    }

    @Override
    @Transactional
    public Mono<Void> execute(String gameId) {
        log.info("Deleting game: {}", gameId);

        GameId id = GameId.from(gameId);

        return gameRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new GameNotFoundException(id));
                    }
                    return gameRepository.deleteById(id);
                })
                .doOnSuccess(v -> log.info("Game removed: {}", gameId));
    }

}

