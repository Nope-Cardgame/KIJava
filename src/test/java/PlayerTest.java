import gameobjects.Player;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    private JSONObject jsonObject;
    private Player player;

    @BeforeEach
    void setup() throws JSONException {
        this.jsonObject = new JSONObject("{\"name\":\"Aremju\",\"webSocketID\":\"blablabla\"}");
        this.player = new Player("Aremju","blablabla",0,new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        this.jsonObject = null;
    }

    @Test
    void creationSuccessful() {
        Player anotherPlayer = new Player(jsonObject);
        assertEquals(anotherPlayer, player);
    }
}
