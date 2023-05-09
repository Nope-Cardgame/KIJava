package gameobjects.actions;

import gameobjects.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class DisqualifyPlayer extends Action{
    private Player player;
    public DisqualifyPlayer(String type, String explanation, Player player) {
        super(type, explanation);
        this.player = player;
    }

    public DisqualifyPlayer(JSONObject jsonObject) {
        super(jsonObject);
        try {
            this.player = new Player(jsonObject.getJSONObject("player"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }
}
