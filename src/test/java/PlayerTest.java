import gameobjects.Player;
import gameobjects.cards.Card;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setup() throws JSONException {
        List<Card> cards = new ArrayList<>();
        this.player = new Player("Aremju", "blablabla",cards, 1,false);
    }

    @Test
    void creationSuccessful() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","Aremju");
        jsonObject.put("socketId","blablabla");
        jsonObject.put("cardAmount",1);
        jsonObject.put("ranking",1);
        jsonObject.put("disqualified",false);
        Player anotherPlayer = new Player(jsonObject);
        // pseudo-assert true to determine whether exception occurred or not
        assertTrue(true);
    }
}
