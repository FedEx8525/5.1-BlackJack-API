package com.blackjack.api.infrastructure.adapter.out.persistence.mysql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("players")
public class PlayerEntity implements Persistable<String> {

    @Id
    private String id;

    @Column("name")
    private String name;

    @Column("balance")
    private BigDecimal balance;

    @Column("games_played")
    private Integer gamesPlayed;

    @Column("games_won")
    private Integer gamesWon;

    @Column("games_lost")
    private Integer gamesLost;

    @Transient
    @Builder.Default
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }
}
