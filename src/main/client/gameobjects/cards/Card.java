package gameobjects.cards;

import com.google.gson.Gson;
import gameobjects.IJsonable;

import java.util.List;

public abstract class Card implements IJsonable {
    // Contains the colors that are used by this type of card
    protected List<String> colors;
    private String type;
    private String name;

    /**
     * Standard-Constructor for Card
     *
     * @param type the type of the card
     * @param name the name of the card
     */
    public Card(String type, String name, List<String> color) {
        this.name = name;
        this.type = type;
        this.colors = color;
    }

    /**
     * Creates a Card using a valid jsonString
     *
     * @param jsonString the jsonString that must always be valid!
     */
    public Card(String jsonString) {
        Gson gson = new Gson();
        this.type = gson.fromJson(jsonString,getClass()).getCardType();
        this.name = gson.fromJson(jsonString,getClass()).getName();
        this.colors = new Gson().fromJson(jsonString,getClass()).getColors();
    }

    public String getCardType() {
        return this.type;
    }

    public void setCardType(String type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColor(List<String> color) {
        this.colors = color;
    }
}
