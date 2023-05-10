package gameobjects.cards;

import gameobjects.cards.Card;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActionCard extends Card {
    private List<String> colors;
    public ActionCard(String type, String name, List<String> colors) {
        super(type, name);
        this.colors = colors;
    }

    public ActionCard(String jsonString) {
        super(jsonString);
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    @Override
    public String toJSON() {
        return null;
    }
}
