package gameobjects;

import com.google.gson.Gson;
import gameobjects.actions.Action;
import gameobjects.actions.ActionFactory;
import gameobjects.cards.Card;
import gameobjects.cards.CardFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Game implements Jsonable {
    private String id;
    private String state;
    private boolean noActionCards;
    private boolean noWildcards;
    private boolean oneMoreStartCards;
    private Tournament tournament;
    private String gameRole;
    private int encounterRound;
    private List<Player> players;
    private List<Card> discardPile;
    private Action lastAction;
    private Player currentPlayer;
    private String startTime;
    private Card initialTopCard;
    private List<Action> actions;
    private String endTime;

    public Game() {

    }

    /**
     * WARNING: Only use this constructor if game is already in progress
     * @param jsonString
     */
    public Game(String jsonString) {
        try {
            JSONObject gameObject = new JSONObject(jsonString);
            // First initialize the fields that do always come
            this.id = gameObject.getString("id");
            this.state = gameObject.getString("state");
            this.noActionCards = gameObject.getBoolean("noActionCards");
            this.noWildcards = gameObject.getBoolean("noWildCards");
            this.oneMoreStartCards = gameObject.getBoolean("oneMoreStartCards");
            this.startTime = gameObject.getString("startTime");
            // Init Player List
            this.players = new ArrayList<>();
            JSONArray playerArray = gameObject.getJSONArray("players");
            for (int iterator = 0; iterator < playerArray.length(); iterator++) {
                this.players.add(new Player(playerArray.getJSONObject(iterator).toString()));
            }
            // handle tournament
            try {
                this.tournament = new Tournament(gameObject.getJSONObject("tournament").toString());
                this.gameRole = gameObject.getString("gameRole");
                this.encounterRound = gameObject.getInt("encounterRound");

            } catch (JSONException e) {
                // if not tournament, set values to null and encounterRound to -1
                this.tournament = null;
                this.gameRole = null;
                this.encounterRound = -1;
            }
            // handle game in progress
            try {
                JSONArray cardArray = gameObject.getJSONArray("discardPile");
                this.discardPile = new ArrayList<>();
                for (int iterator = 0; iterator < cardArray.length(); iterator++) {
                    this.discardPile.add(CardFactory.getCard(cardArray.getJSONObject(iterator).toString()));
                }
            } catch (JSONException e) {
                // if not in progress, set values to null...
                this.discardPile = null;
                this.lastAction = null;
                this.currentPlayer = null;
            }
            // handle game finished
            try {
                this.initialTopCard = CardFactory.getCard(gameObject.getJSONObject("initialTopCard").toString());
                this.endTime = gameObject.getString("endTime");
                JSONArray actionArray = gameObject.getJSONArray("actions");
                this.actions = new ArrayList<>();
                for (int iterator = 0; iterator < actionArray.length(); iterator++) {
                    actions.add(ActionFactory.getAction(actionArray.getJSONObject(iterator).toString()));
                }
            } catch (JSONException e) {
                this.initialTopCard = null;
                this.endTime = null;
                this.actions = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toJSON() {
        // TODO: 11.05.2023 Implement toJSON for Game
        return new Gson().toJson(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isNoWildcards() {
        return noWildcards;
    }

    public void setNoWildcards(boolean noWildcards) {
        this.noWildcards = noWildcards;
    }

    public boolean isOneMoreStartCards() {
        return oneMoreStartCards;
    }

    public void setOneMoreStartCards(boolean oneMoreStartCards) {
        this.oneMoreStartCards = oneMoreStartCards;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public String getGameRole() {
        return gameRole;
    }

    public void setGameRole(String gameRole) {
        this.gameRole = gameRole;
    }

    public int getEncounterRound() {
        return encounterRound;
    }

    public void setEncounterRound(int encounterRound) {
        this.encounterRound = encounterRound;
    }

    public Action getLastAction() {
        return lastAction;
    }

    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Card getInitialTopCard() {
        return initialTopCard;
    }

    public void setInitialTopCard(Card initialTopCard) {
        this.initialTopCard = initialTopCard;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
