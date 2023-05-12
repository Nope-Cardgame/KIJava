package event_handling;

import com.google.gson.Gson;
import gameobjects.IJsonable;

import java.util.List;

public class GameInvitation implements IJsonable {
    boolean noWildCards;
    private List<InvitePlayer> players;
    private String id;
    private String state;
    private boolean noActionCards;
    private boolean oneMoreStartCard;

    public GameInvitation(boolean noWildCards, List<InvitePlayer> players, String id, String state, boolean noActionCards, boolean oneMoreStartCard) {
        this.noWildCards = noWildCards;
        this.players = players;
        this.id = id;
        this.state = state;
        this.noActionCards = noActionCards;
        this.oneMoreStartCard = oneMoreStartCard;
    }

    public GameInvitation(String jsonString) {
        Gson gson = new Gson();
        this.players = gson.fromJson(jsonString,getClass()).getPlayers();
        this.noWildCards = gson.fromJson(jsonString,getClass()).isNoWildCards();
        this.id = gson.fromJson(jsonString,getClass()).getId();
        this.state = gson.fromJson(jsonString,getClass()).getState();
        this.noActionCards = gson.fromJson(jsonString,getClass()).isNoActionCards();
        this.oneMoreStartCard = gson.fromJson(jsonString,getClass()).isOneMoreStartCard();
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public boolean isNoWildCards() {
        return noWildCards;
    }

    public void setNoWildCards(boolean noWildCards) {
        this.noWildCards = noWildCards;
    }

    public List<InvitePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<InvitePlayer> players) {
        this.players = players;
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

    public boolean isOneMoreStartCard() {
        return oneMoreStartCard;
    }

    public void setOneMoreStartCard(boolean oneMoreStartCard) {
        this.oneMoreStartCard = oneMoreStartCard;
    }
}
