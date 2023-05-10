import gameobjects.Player;
import gameobjects.actions.SayNope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SayNopeTest {
    SayNope sayNope;
    @BeforeEach
    void setUp() {
        Player player = new Player("Aremju","blablabla",0,new ArrayList<>(),1,false);
        this.sayNope = new SayNope("nope","no cards to discard",player);
    }
    @Test
    void sayNopeCreation() {
        SayNope otherSayNope = new SayNope("{\n" +
                "  \"type\": \"nope\",\n" +
                "  \"explanation\": \"no cards to discard\",\n" +
                "  \"player\": {\n" +
                "    \"username\": \"Aremju\",\n" +
                "    \"socketId\": \"blablabla\",\n" +
                "    \"cardAmount\": 0,\n" +
                "    \"cards\": [],\n" +
                "    \"ranking\": 1,\n" +
                "    \"disqualified\": false\n" +
                "  }\n" +
                "}");
        assertEquals(this.sayNope.getExplanation(),otherSayNope.getExplanation());
        assertEquals(this.sayNope.getType(),otherSayNope.getType());
        assertEquals(this.sayNope.getPlayer().getUsername(),otherSayNope.getPlayer().getUsername());
        assertEquals(this.sayNope.getPlayer().getSocketId(),otherSayNope.getPlayer().getSocketId());
        assertEquals(this.sayNope.getPlayer().getCardAmount(),otherSayNope.getPlayer().getCardAmount());
        assertEquals(this.sayNope.getPlayer().getCards(),otherSayNope.getPlayer().getCards());
        assertEquals(this.sayNope.getPlayer().getRanking(),otherSayNope.getPlayer().getRanking());
        assertEquals(this.sayNope.getPlayer().isDisqualified(),otherSayNope.getPlayer().isDisqualified());
    }

    @Test
    void sayNopeJSON() {
        String expectedJson = "{\"player\":{\"username\":\"Aremju\",\"socketId\":\"blablabla\",\"cardAmount\":0,\"cards\":[],\"ranking\":1,\"disqualified\":false},\"type\":\"nope\",\"explanation\":\"no cards to discard\"}";
        assertEquals(expectedJson,sayNope.toJSON());
    }
}
