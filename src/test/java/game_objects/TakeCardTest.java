package game_objects;

import gameobjects.Player;
import gameobjects.actions.TakeCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TakeCardTest {
    private TakeCard takeCard;
    @BeforeEach
    void setUp() {
        Player player = new Player("Aremju","blablabla",0,new ArrayList<>(),1,false);
        this.takeCard = new TakeCard("take","no cards to discard",2,new ArrayList<>(),player);
    }

    @Test
    void takeCardCreation() {
        TakeCard otherTakeCard = new TakeCard("{\n" +
                "  \"type\":\"take\",\n" +
                "  \"explanation\":\"no cards to discard\",\n" +
                "  \"amount\":2,\n" +
                "  \"cards\":[],\n" +
                "  \"player\":{\n" +
                "    \"username\":\"Aremju\",\n" +
                "    \"socketId\":\"blablabla\",\n" +
                "    \"cardAmount\":0,\n" +
                "    \"cards\":[],\n" +
                "    \"ranking\":1,\n" +
                "    \"disqualified\":false\n" +
                "  }\n" +
                "}");
        assertEquals(this.takeCard.getType(),otherTakeCard.getType());
        assertEquals(this.takeCard.getExplanation(),otherTakeCard.getExplanation());
        assertEquals(this.takeCard.getAmount(),otherTakeCard.getAmount());
        assertEquals(this.takeCard.getCards(),otherTakeCard.getCards());
        assertEquals(this.takeCard.getPlayer().getCardAmount(),otherTakeCard.getPlayer().getCardAmount());
        assertEquals(this.takeCard.getPlayer().getUsername(),otherTakeCard.getPlayer().getUsername());
        assertEquals(this.takeCard.getPlayer().getSocketId(),otherTakeCard.getPlayer().getSocketId());
        assertEquals(this.takeCard.getPlayer().getCards(),otherTakeCard.getPlayer().getCards());
        assertEquals(this.takeCard.getPlayer().getRanking(),otherTakeCard.getPlayer().getRanking());
        assertEquals(this.takeCard.getPlayer().isDisqualified(),otherTakeCard.getPlayer().isDisqualified());
    }

    @Test
    void takeCardJson() {
        String expectedJson = "{\"amount\":2,\"cards\":[],\"player\":{\"username\":\"Aremju\",\"socketId\":\"blablabla\",\"cardAmount\":0,\"cards\":[],\"ranking\":1,\"disqualified\":false},\"type\":\"take\",\"explanation\":\"no cards to discard\"}";
        assertEquals(expectedJson,takeCard.toJSON());
    }
}
