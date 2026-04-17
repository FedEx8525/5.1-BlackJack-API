package com.blackjack.api.infrastructure.adapter.out.persistence.mongo;

import com.blackjack.api.domain.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
public class GameDocument {

    @Id
    private String id;

    private String playerId;
    private LocalDateTime createdAt;

    private List<String> playerHand;
    private List<String> dealerHand;

    private List<String> remainingCards;

    private double bet;
    private GameStatus status;
 }
