package com.blackjack.api.infrastructure.adapter.out.persistence.mongo;

import com.blackjack.api.domain.enums.Rank;
import com.blackjack.api.domain.enums.Suit;
import com.blackjack.api.domain.exception.InvalidCardException;
import com.blackjack.api.domain.model.Card;
import com.blackjack.api.domain.model.Deck;
import com.blackjack.api.domain.model.Game;
import com.blackjack.api.domain.model.Hand;
import com.blackjack.api.domain.valueobject.GameId;
import com.blackjack.api.domain.valueobject.Money;
import com.blackjack.api.domain.valueobject.PlayerId;
import org.bson.types.Symbol;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GameMapper {
    private static final Pattern CARD_PATTERN =
            Pattern.compile("(10|[A2-9JQK])(♠|♥|♦|♣)");

    public GameDocument toDocument(Game game) {
        return GameDocument.builder()
                .id(game.getId().value())
                .playerId(game.getPlayerId().value())
                .createdAt(game.getCreatedAt())
                .playerHand(cardsToStrings(game.getPlayerHand()))
                .dealerHand(cardsToStrings(game.getDealerHand()))
                .remainingCards(deckToStrings(game.getDeck()))
                .bet(game.getBet().getAmount().doubleValue())
                .status(game.getStatus())
                .build();
    }

    public Game toDomain(GameDocument doc) {

        return Game.builder()
                .id(GameId.from(doc.getId()))
                .playerId(PlayerId.from(doc.getPlayerId()))
                .createdAt(doc.getCreatedAt())
                .playerHand(stringsToHand(doc.getPlayerHand()))
                .dealerHand(stringsToHand(doc.getDealerHand()))
                .deck(stringsToDeck(doc.getRemainingCards()))
                .bet(Money.of(BigDecimal.valueOf(doc.getBet())))
                .status(doc.getStatus())
                .build();
    }

    private List<String> cardsToStrings(Hand hand) {
        return hand.getCards().stream()
                .map(Card::toString)
                .toList();
    }

    private List<String> deckToStrings(Deck deck) {
        return new ArrayList<>(deck.getCards()).stream()
                .map(Card::toString)
                .toList();
    }

    private Hand stringsToHand(List<String> cardStrings) {
        Hand hand = Hand.empty();
        cardStrings.stream()
                .map(this::stringToCard)
                .forEach(hand::addCard);
        return hand;
    }

    private Deck stringsToDeck(List<String> cardStrings) {
        Stack<Card> cards = new Stack<>();
        cardStrings.stream()
                .map(this::stringToCard)
                .forEach(cards::push);

        return Deck.from(cards);
    }

    private Card stringToCard(String cardStr) {
        Matcher matcher = CARD_PATTERN.matcher(cardStr);
        if (!matcher.matches()) {
            throw new InvalidCardException("Invalid Card: " + cardStr);
        }
        String rankStr = matcher.group(1);
        String suitStr = matcher.group(2);

        Rank rank = parseRank(rankStr);
        Suit suit = parseSuit(suitStr);

        return Card.of(rank, suit);
    }

    private Rank parseRank(String symbol) {
        return switch (symbol) {
            case "A" -> Rank.ACE;
            case "2" -> Rank.TWO;
            case "3" -> Rank.THREE;
            case "4" -> Rank.FOUR;
            case "5" -> Rank.FIVE;
            case "6" -> Rank.SIX;
            case "7" -> Rank.SEVEN;
            case "8" -> Rank.EIGHT;
            case "9" -> Rank.NINE;
            case "10" -> Rank.TEN;
            case "J" -> Rank.JACK;
            case "Q" -> Rank.QUEEN;
            case "K" -> Rank.KING;
            default -> throw new InvalidCardException("Invalid Rack: " + symbol);
        };
    }

    private Suit parseSuit(String symbol) {
        return switch (symbol) {
            case "♠" -> Suit.SPADES;
            case "♥" -> Suit.HEARTS;
            case "♦" -> Suit.DIAMONDS;
            case "♣" -> Suit.CLUBS;
            default -> throw new InvalidCardException("Invalid Suit: " + symbol);
        };
    }
}
