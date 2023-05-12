import gameobjects.Player;
import gameobjects.cards.ActionCard;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    private Player player;
    @BeforeEach
    public void setUp() {
        List<String> color = new ArrayList<>();
        color.add("red");
        color.add("green");
        color.add("blue");
        color.add("yellow");
        Card numberCard = new NumberCard("number",1,color,"wildcard");
        color = new ArrayList<>();
        color.add("red");
        Card actionCard = new ActionCard("nominate", color, "red nominate");
        List<Card> cards = new ArrayList<>();
        cards.add(actionCard);
        cards.add(numberCard);
        this.player = new Player("Aremju","blablabla",2,cards,1,false);
    }

    @Test
    void creationSuccessful() {
        Player otherPlayer = new Player("{\n" +
                "  \"username\": \"Aremju\",\n" +
                "  \"socketId\": \"blablabla\",\n" +
                "  \"cardAmount\": 2,\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"type\":\"nominate\",\n" +
                "      \"color\":[\"red\"],\n" +
                "      \"name\":\"red nominate\"\n" +
                "    },\n" +
                "    {\"value\":1,\n" +
                "      \"color\": [\n" +
                "        \"red\",\n" +
                "        \"green\",\n" +
                "        \"blue\",\n" +
                "        \"yellow\"\n" +
                "      ],\n" +
                "      \"type\":\"number\",\n" +
                "      \"name\":\"wildcard\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"ranking\": 1,\n" +
                "  \"disqualified\": false\n" +
                "}");
        assertEquals(this.player.getUsername(),otherPlayer.getUsername());
        assertEquals(this.player.getRanking(),otherPlayer.getRanking());
        assertEquals(this.player.getCardAmount(),otherPlayer.getCardAmount());
        assertEquals(this.player.getSocketId(),otherPlayer.getSocketId());
        assertEquals(this.player.isDisqualified(),otherPlayer.isDisqualified());
    }

    @Test
    void toJsonSuccessful() {
        String expectedJsonString = "{\"username\":\"Aremju\"," +
                "\"socketId\":\"blablabla\"," +
                "\"cardAmount\":2," +
                "\"cards\":[{\"color\":[\"red\"],\"type\":\"nominate\",\"name\":\"red nominate\"}," +
                "{\"value\":1,\"color\":[\"red\",\"green\",\"blue\",\"yellow\"],\"type\":\"number\",\"name\":\"wildcard\"}]," +
                "\"ranking\":1," +
                "\"disqualified\":false}";
        assertEquals(expectedJsonString,this.player.toJSON());
    }
}
