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

public class TakeCard extends Action{
    private int amount;
    private List<Card> cards;
    private Player player;
    public TakeCard(String type, String explanation, int amount, List<Card> cards, Player player) {
        super(type, explanation);
        this.amount = amount;
        this.cards = cards;
        this.player = player;
    }

    public TakeCard(String jsonString) {
        super(jsonString);
        try {
            JSONObject takeCardObject = new JSONObject(jsonString);
            this.amount = takeCardObject.getInt("amount");
            this.cards = new ArrayList<>();
            // Cards must be done
            JSONArray cardArray = takeCardObject.getJSONArray("cards");
            for(int iterator = 0; iterator < cardArray.length(); iterator++) {
                JSONObject cardObject = cardArray.getJSONObject(iterator);
                cards.add(CardFactory.getCard(jsonString));
            }
            // player must be done
            JSONObject playerObject = takeCardObject.getJSONObject("player");
            this.player = new Player(playerObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
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

    public List<Card> getCards() {
        return this.cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
