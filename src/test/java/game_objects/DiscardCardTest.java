package game_objects;

import gameobjects.Player;
import gameobjects.actions.DiscardCard;
import gameobjects.actions.TakeCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscardCardTest {
    private DiscardCard discardCard;
    @BeforeEach
    void setUp() {
        Player player = new Player("Aremju","blablabla",0,new ArrayList<>(),1,false);
        this.discardCard = new DiscardCard("discard","no cards to discard",2,new ArrayList<>(),player);
    }

    @Test
    void takeCardCreation() {
        DiscardCard otherDiscardCard = new DiscardCard("{\n" +
                "  \"type\":\"discard\",\n" +
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
        assertEquals(this.discardCard.getType(),otherDiscardCard.getType());
        assertEquals(this.discardCard.getExplanation(),otherDiscardCard.getExplanation());
        assertEquals(this.discardCard.getAmount(),otherDiscardCard.getAmount());
        assertEquals(this.discardCard.getCards(),otherDiscardCard.getCards());
        assertEquals(this.discardCard.getPlayer().getCardAmount(),otherDiscardCard.getPlayer().getCardAmount());
        assertEquals(this.discardCard.getPlayer().getUsername(),otherDiscardCard.getPlayer().getUsername());
        assertEquals(this.discardCard.getPlayer().getSocketId(),otherDiscardCard.getPlayer().getSocketId());
        assertEquals(this.discardCard.getPlayer().getCards(),otherDiscardCard.getPlayer().getCards());
        assertEquals(this.discardCard.getPlayer().getRanking(),otherDiscardCard.getPlayer().getRanking());
        assertEquals(this.discardCard.getPlayer().isDisqualified(),otherDiscardCard.getPlayer().isDisqualified());
    }

    @Test
    void takeCardJson() {
        String expectedJson = "{\"amount\":2,\"cards\":[],\"player\":{\"username\":\"Aremju\",\"socketId\":\"blablabla\",\"cardAmount\":0,\"cards\":[],\"ranking\":1,\"disqualified\":false},\"type\":\"discard\",\"explanation\":\"no cards to discard\"}";
        assertEquals(expectedJson,this.discardCard.toJSON());
    }
}
