package gameobjects;

import gameobjects.cards.Card;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Player {
    // Logger for logging purposes
    private static final Logger LOG = Logger.getLogger(Player.class.getSimpleName());
    private String name;
    private String webSocketID;
    private int cardAmount;
    private List<Card> cards;

    public Player(String name, String webSocketID, int cardAmount, List<Card> cards) {
        this.name = name;
        this.webSocketID = webSocketID;
        this.cardAmount = cardAmount;
        this.cards = cards;
    }

    /**
     * Creates a Player out of an JSONObject instance
     *
     * @param playerObject the JSON-Object of a Player instance, it contains
     *                     - name as String
     *                     - webSocketID as String
     *                     - the amount of Cards
     *                     - an Array of all cards held by the player
     */
    public Player(JSONObject playerObject) {
        try {
            this.name = (String) playerObject.get("name");
            this.webSocketID = (String) playerObject.get("webSocketID");
            // TODO: 05.05.2023 make something with the cards
            this.cards = new ArrayList<>();
            this.cardAmount = 0;

        } catch (JSONException e) {
            LOG.severe(e.getMessage());
        } finally {
            LOG.info("Player " + this + " was created successfully!");
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getWebSocketID() {
        return webSocketID;
    }

    public void setWebSocketID(String webSocketID) {
        this.webSocketID = webSocketID;
    }

    public int getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(int cardAmount) {
        this.cardAmount = cardAmount;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public boolean equals(Object obj) {
        boolean erg = false;
        try {
            Player otherPlayer = (Player) obj;
            erg = otherPlayer.getName().equals(this.name);
            erg &= otherPlayer.getWebSocketID().equals(this.webSocketID);
            erg &= otherPlayer.getCardAmount() == this.cardAmount;
            erg &= otherPlayer.getCards().equals(this.cards);
        } catch (ClassCastException e) {
            LOG.severe(e.getMessage());
        }
        return erg;
    }
}
