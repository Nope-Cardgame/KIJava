package game_objects;

import gameobjects.Player;
import gameobjects.actions.Action;
import gameobjects.actions.DisqualifyPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisqualifyPlayerTest {
    private Action action;
    @BeforeEach
    void setUp() {
        Player player = new Player("Aremju","blablabla",0,new ArrayList<>(),1,false);
        this.action = new DisqualifyPlayer("disqualify","Got no response in 10s",player);
    }
    @Test
    void disqualifyCreation() {
        DisqualifyPlayer disqualifyPlayer = (DisqualifyPlayer) action;
        DisqualifyPlayer otherDisqualify = new DisqualifyPlayer("{\n" +
                "  \"type\":\"disqualify\",\n" +
                "  \"explanation\":\"Got no response in 10s\",\n" +
                "  \"player\": {\n" +
                "    \"username\": \"Aremju\",\n" +
                "    \"socketId\": \"blablabla\",\n" +
                "    \"cardAmount\": 0,\n"+
                "    \"cards\": [],\n" +
                "    \"ranking\": 1,\n" +
                "    \"disqualified\": false\n" +
                "  }\n" +
                "}");
        // check if everything is equal
        assertEquals(disqualifyPlayer.getType(),otherDisqualify.getType());
        assertEquals(disqualifyPlayer.getExplanation(),otherDisqualify.getExplanation());
        assertEquals(disqualifyPlayer.getPlayer().getUsername(),otherDisqualify.getPlayer().getUsername());
        assertEquals(disqualifyPlayer.getPlayer().getCards(),otherDisqualify.getPlayer().getCards());
        assertEquals(disqualifyPlayer.getPlayer().getCardAmount(),otherDisqualify.getPlayer().getCardAmount());
        assertEquals(disqualifyPlayer.getPlayer().isDisqualified(),otherDisqualify.getPlayer().isDisqualified());
        assertEquals(disqualifyPlayer.getPlayer().getSocketId(),otherDisqualify.getPlayer().getSocketId());
        assertEquals(disqualifyPlayer.getPlayer().getRanking(),otherDisqualify.getPlayer().getRanking());
    }

    @Test
    void disqualifyJson() {
        String expectedJson = "{\"player\":{\"username\":\"Aremju\",\"socketId\":\"blablabla\",\"cardAmount\":0,\"cards\":[],\"ranking\":1,\"disqualified\":false},\"type\":\"disqualify\",\"explanation\":\"Got no response in 10s\"}";
        assertEquals(expectedJson,action.toJSON());
    }
}
