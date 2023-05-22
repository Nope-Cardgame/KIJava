package game_objects;

import gameobjects.Player;
import gameobjects.actions.NominateCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NominateCardTest {
    private NominateCard nominateCard;
    @BeforeEach
    void setUp() {
        Player player = new Player("Aremju","blablabla",0,new ArrayList<>(), 1,false);
        Player nominatedPlayer = new Player("otherAremju","blablablu",0,new ArrayList<>(), 1,false);
        this.nominateCard = new NominateCard("nominate","random pick",1,new ArrayList<>(),player,nominatedPlayer,"yellow",3);
    }
    @Test
    void nominateCardCreation() {
        NominateCard otherNominateCard = new NominateCard("{\n" +
                "  \"type\": \"nominate\",\n" +
                "  \"explanation\": \"random pick\",\n" +
                "  \"amount\": 1,\n" +
                "  \"cards\": [],\n" +
                "  \"player\": {\n" +
                "    \"username\": \"Aremju\",\n" +
                "    \"socketId\": \"blablabla\",\n" +
                "    \"cardAmount\": 0,\n" +
                "    \"cards\": [],\n" +
                "    \"ranking\": 1,\n" +
                "    \"disqualified\": false\n" +
                "  },\n" +
                "  \"nominatedPlayer\": {\n" +
                "    \"username\": \"otherAremju\",\n" +
                "    \"socketId\": \"blablablu\",\n" +
                "    \"cardAmount\": 0,\n" +
                "    \"cards\": [],\n" +
                "    \"ranking\": 1,\n" +
                "    \"disqualified\": false\n" +
                "  },\n" +
                "  \"nominatedColor\": \"yellow\",\n" +
                "  \"nominatedAmount\": 3\n" +
                "}");
        assertEquals(this.nominateCard.getType(),otherNominateCard.getType());
        assertEquals(this.nominateCard.getExplanation(),otherNominateCard.getExplanation());
        assertEquals(this.nominateCard.getAmount(),otherNominateCard.getAmount());
        assertEquals(this.nominateCard.getNominatedColor(),otherNominateCard.getNominatedColor());
        assertEquals(this.nominateCard.getCards(),otherNominateCard.getCards());
        assertEquals(this.nominateCard.getNominatedAmount(),otherNominateCard.getNominatedAmount());
        // Normal Player
        assertEquals(this.nominateCard.getPlayer().getUsername(),otherNominateCard.getPlayer().getUsername());
        assertEquals(this.nominateCard.getPlayer().getSocketId(),otherNominateCard.getPlayer().getSocketId());
        assertEquals(this.nominateCard.getPlayer().getCardAmount(),otherNominateCard.getPlayer().getCardAmount());
        assertEquals(this.nominateCard.getPlayer().getCards(),otherNominateCard.getPlayer().getCards());
        assertEquals(this.nominateCard.getPlayer().getRanking(),otherNominateCard.getPlayer().getRanking());
        assertEquals(this.nominateCard.getPlayer().isDisqualified(),otherNominateCard.getPlayer().isDisqualified());
        // nominated Player
        assertEquals(this.nominateCard.getNominatedPlayer().getUsername(),otherNominateCard.getNominatedPlayer().getUsername());
        assertEquals(this.nominateCard.getNominatedPlayer().getSocketId(),otherNominateCard.getNominatedPlayer().getSocketId());
        assertEquals(this.nominateCard.getNominatedPlayer().getCardAmount(),otherNominateCard.getNominatedPlayer().getCardAmount());
        assertEquals(this.nominateCard.getNominatedPlayer().getCards(),otherNominateCard.getNominatedPlayer().getCards());
        assertEquals(this.nominateCard.getNominatedPlayer().getRanking(),otherNominateCard.getNominatedPlayer().getRanking());
        assertEquals(this.nominateCard.getNominatedPlayer().isDisqualified(),otherNominateCard.getNominatedPlayer().isDisqualified());
    }

    @Test
    void nominateCardJSON() {
        String expectedJson = "{\"amount\":1,\"cards\":[],\"player\":{\"username\":\"Aremju\",\"socketId\":\"blablabla\",\"cardAmount\":0,\"cards\":[],\"ranking\":1,\"disqualified\":false},\"nominatedPlayer\":{\"username\":\"otherAremju\",\"socketId\":\"blablablu\",\"cardAmount\":0,\"cards\":[],\"ranking\":1,\"disqualified\":false},\"nominatedColor\":\"yellow\",\"nominatedAmount\":3,\"type\":\"nominate\",\"explanation\":\"random pick\"}";
        assertEquals(expectedJson,nominateCard.toJSON());
    }
}
