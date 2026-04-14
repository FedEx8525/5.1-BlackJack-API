package com.blackjack.api.domain.model.mother;

import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.valueobject.PlayerId;

import static com.blackjack.api.domain.model.mother.DeckMother.standardDeck;

public class GameMother {

    public static Game newGame() {
        return Game.create(
                PlayerId.generate(),
                standardDeck()
        );
    }
}
