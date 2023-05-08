import gameobjects.Player;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setup() throws JSONException {
        this.player = new Player("Aremju","blablabla",0,new ArrayList<>());
    }

    @Test
    void creationSuccessful() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"name\":\"Aremju\",\"webSocketID\":\"blablabla\"}");
        Player anotherPlayer = new Player(jsonObject);
        assertEquals(anotherPlayer, player);
    }
}
