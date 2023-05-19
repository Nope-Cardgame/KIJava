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

public class Game implements IJsonable {
    private String id;
    private String state;
    private boolean noActionCards;
    private boolean noWildcards;
    private boolean oneMoreStartCard;
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

    /**
     * Standard Constructor for a game
     *
     * @param id the id of the game (not null)
     * @param state the game state (game_start for invitation, nominate_flipped, turn_start, card_draw) (not null)
     * @param noActionCards determines whether ActionCards are used or not (not null)
     * @param noWildcards determines whether Wildcards are used or not (not null)
     * @param oneMoreStartCards determines whether there is one more starting card or not (not null)
     * @param tournament the tournament this game belongs to (null when no tournament)
     * @param gameRole the role of the game (only when tournament, null else)
     * @param encounterRound the encounterRound of the game (only when tournament, else -1)
     * @param players the players of this game (not null)
     * @param discardPile the discardPile (null if created for invitation)
     * @param lastAction the last Action of this game (null if created for invitation)
     * @param currentPlayer the current Player that can make an Action (null if created for invitation)
     * @param startTime the start time of this game (not null)
     * @param initialTopCard the initial top card of this game (null if created for invitation)
     * @param actions the actions of this game (null if created for invitation)
     * @param endTime the endTime of this game (null if created for invitation)
     */
    public Game(String id, String state, boolean noActionCards, boolean noWildcards,
                boolean oneMoreStartCards, Tournament tournament, String gameRole,
                int encounterRound, List<Player> players, List<Card> discardPile,
                Action lastAction, Player currentPlayer, String startTime, Card initialTopCard,
                List<Action> actions, String endTime) {
        this.id = id;
        this.state = state;
        this.noActionCards = noActionCards;
        this.noWildcards = noWildcards;
        this.oneMoreStartCard = oneMoreStartCards;
        this.tournament = tournament;
        this.gameRole = gameRole;
        this.encounterRound = encounterRound;
        this.players = players;
        this.discardPile = discardPile;
        this.lastAction = lastAction;
        this.currentPlayer = currentPlayer;
        this.startTime = startTime;
        this.initialTopCard = initialTopCard;
        this.actions = actions;
        this.endTime = endTime;
    }

    /**
     * Creates a Game instance using the jsonString
     *
     * @param jsonString a VALID game String
     */
    public Game(String jsonString) {
        try {
            JSONObject gameObject = new JSONObject(jsonString);
            // First initialize the fields that do always come
            this.id = gameObject.getString("id");
            this.state = gameObject.getString("state");
            this.noActionCards = gameObject.getBoolean("noActionCards");
            this.noWildcards = gameObject.getBoolean("noWildCards");
            this.oneMoreStartCard = gameObject.getBoolean("oneMoreStartCard");

            try {
                this.startTime = gameObject.getString("startTime");
            } catch (JSONException e) {
                this.startTime = null;
            }

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
                this.currentPlayer = new Player(gameObject.getJSONObject("currentPlayer").toString());
                this.lastAction = ActionFactory.getAction(gameObject.getJSONObject("lastAction").toString());
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
        return oneMoreStartCard;
    }

    public void setOneMoreStartCards(boolean oneMoreStartCard) {
        this.oneMoreStartCard = oneMoreStartCard;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
