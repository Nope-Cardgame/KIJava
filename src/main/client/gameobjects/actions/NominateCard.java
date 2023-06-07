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
    private int nominatedAmount;

    /**
     * Standard-Constructor for a NominateCardInstance to use a four colored nominate card
     *
     * @param type the type of this Action
     * @param explanation the reason of this Action
     * @param amount the Amount that is nominated by the Player
     * @param cards the Cards placed by nomination
     * @param player the Player who nominated another Player
     * @param nominatedPlayer the nominated Player
     * @param nominatedColor the nominated Color
     */
    public NominateCard(String type, String explanation, int amount, List<Card> cards, Player player, Player nominatedPlayer, String nominatedColor
            ,int nominatedAmount) {
        super(type, explanation);
        this.amount = amount;
        this.nominatedColor = nominatedColor;
        this.cards = cards;
        this.nominatedPlayer = nominatedPlayer;
        this.player = player;
        this.nominatedAmount = nominatedAmount;
    }

    /**
     * Standard-Constructor for a NominateCardInstance to use a one colored nominate card
     *
     * @param type the type of this Action
     * @param explanation the reason of this Action
     * @param amount the Amount that is nominated by the Player
     * @param cards the Cards placed by nomination
     * @param player the Player who nominated another Player
     * @param nominatedPlayer the nominated Player
     */
    public NominateCard(String type, String explanation, int amount, List<Card> cards, Player player, Player nominatedPlayer, int nominatedAmount) {
        super(type, explanation);
        this.amount = amount;
        this.cards = cards;
        this.nominatedPlayer = nominatedPlayer;
        this.player = player;
        this.nominatedAmount = nominatedAmount;
    }

    /**
     * Creates a NominateCard instance using a valid
     * jsonString
     *
     * @param jsonString the jsonString that must always be valid!
     */
    public NominateCard(String jsonString) {
        super(jsonString);
        try {
            JSONObject nominateCardObject = new JSONObject(jsonString);
            this.amount = nominateCardObject.getInt("amount");
            try {
                this.nominatedColor = nominateCardObject.getString("nominatedColor");
            } catch (JSONException ignored){
                this.nominatedColor = null;
            }
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
            this.nominatedAmount = nominateCardObject.getInt("nominatedAmount");
        } catch (JSONException ignored) {
        }
    }

    @Override
    public String toJSON() {
        if(nominatedColor != null) {
            return new Gson().toJson(this);
        } else {
            return new Gson().toJson(new NominateCard(this.getType(), this.getExplanation(), this.getAmount(),
                    this.getCards(), this.getPlayer(), this.getNominatedPlayer(), this.nominatedAmount));
        }
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

    public int getNominatedAmount() {
        return nominatedAmount;
    }

    public void setNominatedAmount(int nominatedAmount) {
        this.nominatedAmount = nominatedAmount;
    }
}