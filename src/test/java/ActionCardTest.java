import gameobjects.cards.ActionCard;
import gameobjects.cards.Card;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionCardTest {
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
}
