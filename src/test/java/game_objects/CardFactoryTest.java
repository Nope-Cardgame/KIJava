package game_objects;

import gameobjects.cards.ActionCard;
import gameobjects.cards.Card;
import gameobjects.cards.CardFactory;
import gameobjects.cards.NumberCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardFactoryTest {
    @Test
    void actionCardFactory() {
        Card card = CardFactory.getCard("{\"type\":\"nominate\",\"colors\":[\"red\"],\"name\":\"red nominate\"}");
        assertTrue(card instanceof ActionCard);
    }

    @Test
    void numberCardFactory() {
        Card card = CardFactory.getCard("{\"type\":\"number\",\"value\":1,\"colors\":[\"red\",\"green\",\"blue\",\"yellow\"],\"name\":\"wildcard\"}");
        assertTrue(card instanceof NumberCard);
    }
}
