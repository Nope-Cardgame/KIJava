package gameobjects;

import com.google.gson.Gson;

public class TournamentParticipant implements Jsonable {
    private String username;
    private int ranking;
    private boolean disqualified;
    private int score;

    /**
     * Standard Constructor for TournamentParticipant
     *
     * @param username the username of the participant
     * @param ranking the ranking of the participant
     * @param disqualified the flag for being disqualified or not
     * @param score the current score of the Player
     */
    public TournamentParticipant(String username, int ranking, boolean disqualified, int score) {
        this.username = username;
        this.ranking = ranking;
        this.disqualified = disqualified;
        this.score = score;
    }

    /**
     * Creates a TournamentParticipant instance using
     * a valid jsonString
     *
     * @param jsonString a jsonString that must be valid Participant
     */
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
