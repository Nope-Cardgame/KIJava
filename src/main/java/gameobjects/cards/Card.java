package gameobjects.cards;

import com.google.gson.Gson;
import gameobjects.Jsonable;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Card implements Jsonable {
    private String type;
    private String name;

    /**
     * Standard-Constructor for Card
     *
     * @param type the type of the card
     * @param name the name of the card
     */
    public Card(String type, String name) {
        this.name = name;
        this.type = type;
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
}
