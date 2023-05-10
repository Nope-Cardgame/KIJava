import gameobjects.cards.ActionCard;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {
    @Test
    void actionCardCreation() {
        List<String> color = new ArrayList<>();
        color.add("red");
        Card actionCard = new ActionCard("nominate", color, "red nominate");
        // Necessary step to check whether polymorphism works or not
        ActionCard staticActionCard = (ActionCard) actionCard;
        ActionCard anotherActionCard = new ActionCard("{\"type\":\"nominate\",\"color\":[\"red\"],\"name\":\"red nominate\"}");
        // Check if values are equal
        assertEquals(staticActionCard.getCardType(),anotherActionCard.getCardType());
        assertEquals(staticActionCard.getName(),anotherActionCard.getName());
        assertEquals(staticActionCard.getColors(),anotherActionCard.getColors());
    }

    @Test
    void actionCardToJSON() {
        List<String> color = new ArrayList<>();
        color.add("red");
        Card actionCard = new ActionCard("nominate", color, "red nominate");
        String expectedJSON = "{\"color\":[\"red\"],\"type\":\"nominate\",\"name\":\"red nominate\"}";
        // sequence of json dictionaries doesn't matter
        assertEquals(expectedJSON,actionCard.toJSON());
    }

    @Test
    void numberCardCreation() {
        List<String> color = new ArrayList<>();
        color.add("red");
        color.add("green");
        color.add("blue");
        color.add("yellow");
        Card card = new NumberCard("number",1,color,"wildcard");
        // necessary to test whether polymorphism works or not
        NumberCard numberCard = (NumberCard) card;
        NumberCard otherNumberCard = new NumberCard("{\"type\":\"number\",\"value\":1," +
                "\"color\":[\"red\",\"green\",\"blue\",\"yellow\"],\"name\":\"wildcard\"}");
        assertEquals(numberCard.getCardType(), otherNumberCard.getCardType());
        assertEquals(numberCard.getValue(), otherNumberCard.getValue());
        assertEquals(numberCard.getColor(), otherNumberCard.getColor());
        assertEquals(numberCard.getName(), otherNumberCard.getName());
    }

    @Test
    void numberCardToJSON() {
        List<String> color = new ArrayList<>();
        color.add("red");
        color.add("green");
        color.add("blue");
        color.add("yellow");
        Card card = new NumberCard("number",1,color,"wildcard");
        // sequence of json dictionaries doesn't matter
        String expectedJson = "{\"value\":1,\"color\":[\"red\",\"green\",\"blue\",\"yellow\"],\"type\":\"number\",\"name\":\"wildcard\"}";
        assertEquals(expectedJson,card.toJSON());
    }
}
