package gameobjects;

import org.json.JSONException;
import org.json.JSONObject;

public class TournamentParticipant implements Jsonable {
    private String username;
    private int ranking;
    private boolean disqualified;
    private int score;

    public TournamentParticipant(String username, int ranking, boolean disqualified, int score) {
        this.username = username;
        this.ranking = ranking;
        this.disqualified = disqualified;
        this.score = score;
    }

    public TournamentParticipant(JSONObject jsonObject) {
        try {
            this.username = jsonObject.getString("username");
            this.ranking = jsonObject.getInt("ranking");
            this.score = jsonObject.getInt("score");
            this.disqualified = jsonObject.getBoolean("disqualified");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }
}
