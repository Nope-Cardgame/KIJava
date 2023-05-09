package gameobjects.cards;

import gameobjects.Jsonable;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Card implements Jsonable {
    private String type;
    private String name;

    public Card(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public Card(JSONObject jsonObject) {
        try {
            this.name = (String) jsonObject.get("name");
            this.name = (String) jsonObject.get("type");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

        }

    }

    public abstract JSONObject toJSONObject();

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
