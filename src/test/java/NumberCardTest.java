import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberCardTest {
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
