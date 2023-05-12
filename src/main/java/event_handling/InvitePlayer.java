package event_handling;

import com.google.gson.Gson;
import gameobjects.IJsonable;

public class InvitePlayer implements IJsonable {
    private String socketId;
    private int cardAmount;
    private boolean disqualified;
    private boolean accepted;
    private String username;

    /**
     * Standard Constructor for a InvitePlayer Object
     *
     * @param socketId the socketId of the player
     * @param cardAmount the card amount of the player
     * @param disqualified flag for being disqualified
     * @param accepted flag for being accepted
     * @param username username of the invited Player
     *
     * @see <a href="https://github.com/Nope-Cardgame/Doku/blob/main/Schnittstellen/Schnittstellen.md">
     *      Schnittstellen Dokumentation</a>
     */
    public InvitePlayer(String socketId, int cardAmount, boolean disqualified, boolean accepted, String username) {
        this.socketId = socketId;
        this.cardAmount = cardAmount;
        this.disqualified = disqualified;
        this.accepted = accepted;
        this.username = username;
    }

    /**
     * Creates a InvitePlayer instance using a jsonString
     *
     * @param jsonString the jsonString that must always be valid
     *
     * @see <a href="https://github.com/Nope-Cardgame/Doku/blob/main/Schnittstellen/Schnittstellen.md">
     *      Schnittstellen Dokumentation</a>
     */
    public InvitePlayer(String jsonString) {
        Gson gson = new Gson();
        this.socketId = gson.fromJson(jsonString,getClass()).getSocketId();
        this.cardAmount = gson.fromJson(jsonString,getClass()).getCardAmount();
        this.disqualified = gson.fromJson(jsonString,getClass()).isDisqualified();
        this.accepted = gson.fromJson(jsonString,getClass()).isAccepted();
        this.username = gson.fromJson(jsonString,getClass()).getUsername();
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }



    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public int getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(int cardAmount) {
        this.cardAmount = cardAmount;
    }

    public boolean isDisqualified() {
        return disqualified;
    }

    public void setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
