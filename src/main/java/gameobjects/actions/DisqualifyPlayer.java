package gameobjects.actions;

import com.google.gson.Gson;
import gameobjects.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class DisqualifyPlayer extends Action{

    private Player player;
    public DisqualifyPlayer(String type, String explanation, Player player) {
        super(type, explanation);
        this.player = player;
    }

    public DisqualifyPlayer(String jsonString) {
        super(jsonString);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String playerJson = jsonObject.getJSONObject("player").toString();
            this.player = new Player(playerJson);
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
