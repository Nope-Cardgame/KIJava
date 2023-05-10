package gameobjects.actions;

import com.google.gson.Gson;
import gameobjects.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class SayNope extends Action{
    private Player player;
    public SayNope(String type, String explanation, Player player) {
        super(type, explanation);
        this.player = player;
    }

    public SayNope(String jsonString) {
        super(jsonString);
        try {
            JSONObject playerObject = new JSONObject(jsonString).getJSONObject("player");
            this.player = new Player(playerObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
