package gameobjects;

import com.google.gson.Gson;
import gameobjects.cards.ActionCard;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Player implements Jsonable {
    private String username;
    private String socketId;
    private int cardAmount;
    private List<Card> cards;
    private int ranking;
    private boolean disqualified;

    public Player(String username, String socketId, int cardAmount, List<Card> cards, int ranking, boolean disqualified) {
        this.username = username;
        this.socketId = socketId;
        this.cardAmount = cardAmount;
        this.cards = cards;
        this.ranking = ranking;
        this.disqualified = disqualified;
    }

    /**
     * Creates a Player out of an JSONObject instance
     *
     * @param jsonString the JSON-Object of a Player instance, it contains
     *                     - name as String
     *                     - socketId as String
     *                     - the amount of Cards
     *                     - an Array of all cards held by the player
     *                     - information about ranking
     *                     - information about being disqualified
     */
    public Player(String jsonString) {

    }

    @Override
    public boolean equals(Object obj) {
        boolean erg = false;
        try {
            Player otherPlayer = (Player) obj;
            erg = otherPlayer.getUsername().equals(this.username);
            erg &= otherPlayer.getSocketId().equals(this.socketId);
            erg &= otherPlayer.getCardAmount() == this.cardAmount;
            erg &= otherPlayer.getCards().equals(this.cards);
            erg &= otherPlayer.isDisqualified() == this.disqualified;
            erg &= otherPlayer.getRanking() == this.ranking;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return erg;
    }

    public String getUsername() {
        return this.username;
    }

    public void getUsername(String username) {
        this.username = username;
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        this.cardAmount = cards.size();
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
    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }
}
