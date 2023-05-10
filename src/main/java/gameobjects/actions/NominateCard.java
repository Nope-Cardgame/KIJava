package gameobjects.actions;

import com.google.gson.Gson;
import gameobjects.Player;
import gameobjects.cards.Card;
import gameobjects.cards.CardFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NominateCard extends Action{
    private int amount;
    private List<Card> cards;
    private Player player;
    private Player nominatedPlayer;
    private String nominatedColor;
    public NominateCard(String type, String explanation, int amount, List<Card> cards, Player player, Player nominatedPlayer, String nominatedColor ) {
        super(type, explanation);
        this.amount = amount;
        this.nominatedColor = nominatedColor;
        this.cards = cards;
        this.nominatedPlayer = nominatedPlayer;
        this.player = player;
    }

    public NominateCard(String jsonString) {
        super(jsonString);
        try {
            JSONObject nominateCardObject = new JSONObject(jsonString);
            this.amount = nominateCardObject.getInt("amount");
            this.nominatedColor = nominateCardObject.getString("nominatedColor");
            // Cards
            this.cards = new ArrayList<>();
            JSONArray cardArray = nominateCardObject.getJSONArray("cards");
            for(int iterator = 0; iterator < cardArray.length(); iterator++) {
                this.cards.add(CardFactory.getCard(cardArray.getJSONObject(iterator).toString()));
            }
            // player
            this.player = new Player(nominateCardObject.getJSONObject("player").toString());
            // nominatedPlayer
            this.nominatedPlayer = new Player(nominateCardObject.getJSONObject("nominatedPlayer").toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getNominatedPlayer() {
        return nominatedPlayer;
    }

    public void setNominatedPlayer(Player nominatedPlayer) {
        this.nominatedPlayer = nominatedPlayer;
    }

    public String getNominatedColor() {
        return nominatedColor;
    }

    public void setNominatedColor(String nominatedColor) {
        this.nominatedColor = nominatedColor;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
