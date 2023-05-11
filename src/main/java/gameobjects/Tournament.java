package gameobjects;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tournament implements Jsonable {
    private String id;
    private Mode mode;
    private List<TournamentParticipant> participants;
    private List<Game> games;
    private String startTime;
    private String endTime;

    /**
     * Creates a Tournament instance using
     * a valid jsonString
     *
     * @param jsonString jsonString containing the properties
     *                   of a Tournament, String must be a valid tournament
     */
    public Tournament(String jsonString) {
        try {
            JSONObject tournamentObject = new JSONObject(jsonString);
            this.id = tournamentObject.getString("id");
            this.mode = new Mode(tournamentObject.getJSONObject("mode").toString());
            // participants
            JSONArray participantArray = tournamentObject.getJSONArray("participants");
            this.participants = new ArrayList<>();
            for (int iterator = 0; iterator < participantArray.length(); iterator++) {
                this.participants.add(new TournamentParticipant(participantArray.getJSONObject(iterator).toString()));
            }
            // games
            JSONArray gameArray = tournamentObject.getJSONArray("games");
            this.games = new ArrayList<>();
            for (int iterator = 0; iterator < gameArray.length(); iterator++) {
                this.games.add(new Game(gameArray.getJSONObject(iterator).toString()));
            }
            // times
            this.startTime = tournamentObject.getString("startTime");
            this.endTime = tournamentObject.getString("endTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public List<TournamentParticipant> getParticipants() {
        return this.participants;
    }

    public void setParticipants(List<TournamentParticipant> participants) {
        this.participants = participants;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
