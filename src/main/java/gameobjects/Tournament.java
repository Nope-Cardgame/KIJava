package gameobjects;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tournament implements IJsonable {
    private String id;
    private Mode mode;
    private List<TournamentParticipant> participants;
    private List<Game> games;
    private String startTime;
    private String endTime;
    private String state;
    private boolean noActionCards;
    private boolean noWildCards;
    private boolean oneMoreStartCards;
    private int actionTimeout;
    private boolean sendGameInvite;
    private int invitationTimeout;
    private boolean startWithRejection;
    private int participantAmount;

    /**
     *
     *
     * @param id
     * @param mode
     * @param participants
     * @param games
     * @param startTime
     * @param endTime
     * @param actionTimeout
     * @param invitationTimeout
     * @param participantAmount
     * @param startWithRejection
     * @param sendGameInvite
     */
    public Tournament(String id, Mode mode, List<TournamentParticipant> participants, List<Game> games, String startTime, String endTime, int actionTimeout,int invitationTimeout, int participantAmount, boolean startWithRejection, boolean sendGameInvite
                        , String state, boolean noActionCards, boolean noWildCards, boolean oneMoreStartCards) {
        this.id = id;
        this.mode = mode;
        this.participants = participants;
        this.games = games;
        this.startTime = startTime;
        this.endTime = endTime;
        this.actionTimeout = actionTimeout;
        this.invitationTimeout = invitationTimeout;
        this.participantAmount = participantAmount;
        this.startWithRejection = startWithRejection;
        this.sendGameInvite = sendGameInvite;
        this.noActionCards = noActionCards;
        this.noWildCards = noWildCards;
        this.state = state;
        this.oneMoreStartCards = oneMoreStartCards;
    }

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


            this.state = tournamentObject.getString("state");
            this.noWildCards = tournamentObject.getBoolean("noWildCards");
            this.noActionCards = tournamentObject.getBoolean("noActionCards");
            this.oneMoreStartCards = tournamentObject.getBoolean("oneMoreStartCards");

            this.actionTimeout = tournamentObject.getInt("actionTimeout");
            this.invitationTimeout = tournamentObject.getInt("invitationTimeout");
            this.startWithRejection = tournamentObject.getBoolean("startWithRejection");
            this.sendGameInvite = tournamentObject.getBoolean("sendGameInvite");
            this.participantAmount = tournamentObject.getInt("participantAmount");
        } catch (JSONException ignored) {
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

    public boolean isSendGameInvitation() {
        return sendGameInvite;
    }

    public void setSendGameInvitation(boolean sendGameInvitation) {
        this.sendGameInvite = sendGameInvitation;
    }

    public int getActionTimeout() {
        return actionTimeout;
    }

    public void setActionTimeout(int actionTimeout) {
        this.actionTimeout = actionTimeout;
    }

    public int getInvitationTimeout() {
        return invitationTimeout;
    }

    public void setInvitationTimeout(int invitationTimeout) {
        this.invitationTimeout = invitationTimeout;
    }

    public boolean isStartWithRejection() {
        return startWithRejection;
    }

    public void setStartWithRejection(boolean startWithRejection) {
        this.startWithRejection = startWithRejection;
    }

    public int getParticipantAmount() {
        return participantAmount;
    }

    public void setParticipantAmount(int participantAmount) {
        this.participantAmount = participantAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isNoActionCards() {
        return noActionCards;
    }

    public void setNoActionCards(boolean noActionCards) {
        this.noActionCards = noActionCards;
    }

    public boolean isNoWildCards() {
        return noWildCards;
    }

    public void setNoWildCards(boolean noWildCards) {
        this.noWildCards = noWildCards;
    }

    public boolean isOneMoreStartCards() {
        return oneMoreStartCards;
    }

    public void setOneMoreStartCards(boolean oneMoreStartCards) {
        this.oneMoreStartCards = oneMoreStartCards;
    }

    public boolean isSendGameInvite() {
        return sendGameInvite;
    }

    public void setSendGameInvite(boolean sendGameInvite) {
        this.sendGameInvite = sendGameInvite;
    }
}
