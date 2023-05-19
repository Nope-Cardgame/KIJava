package gameobjects;

import com.google.gson.Gson;
import gameobjects.cards.Card;
import gameobjects.cards.CardFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Player implements IJsonable {
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
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.username = jsonObject.getString("username");
            this.socketId = jsonObject.getString("socketId");
            this.cardAmount = jsonObject.getInt("cardAmount");
            this.disqualified = jsonObject.getBoolean("disqualified");
            this.ranking = jsonObject.getInt("ranking");
        } catch (JSONException e) {

        } finally {
            initCardsJson(jsonString);
        }
    }

    /**
     * initializes the Cards using the CardFactory
     * @param jsonString the jsonString from Constructor
     */
    private void initCardsJson(String jsonString) {
        this.cards = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("cards");
            for(int iterator = 0; iterator < jsonArray.length(); iterator++) {
                this.cards.add(
                        CardFactory.
                                getCard(jsonArray.
                                        getJSONObject(iterator).
                                toString())
                );
            }
        }catch(JSONException e) {
            // do nothing
        }
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        Player otherPlayer = (Player) obj;
        return this.getUsername().equals(otherPlayer.getUsername())
                && this.getRanking() == otherPlayer.getRanking()
                && this.getCardAmount() == otherPlayer.getCardAmount()
                && this.getSocketId().equals(otherPlayer.getSocketId())
                && this.isDisqualified() == otherPlayer.isDisqualified()
                && this.getCards().equals(otherPlayer.getCards());
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
}
