package gameobjects;

import com.google.gson.Gson;
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

    public TournamentParticipant(String jsonString) {
        Gson gson = new Gson();
        this.disqualified = gson.fromJson(jsonString,getClass()).isDisqualified();
        this.score = gson.fromJson(jsonString,getClass()).getScore();
        this.username = gson.fromJson(jsonString,getClass()).getUsername();
        this.ranking = gson.fromJson(jsonString,getClass()).getRanking();
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public boolean isDisqualified() {
        return disqualified;
    }

    public void setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
