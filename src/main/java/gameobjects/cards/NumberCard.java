package gameobjects.cards;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NumberCard extends Card {
    private int value;
    private List<String> colors;

    public NumberCard(String type, String name) {
        super(type, name);
    }

    public NumberCard(String jsonString) {
        super(jsonString);
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toJSON() {
        return null;
    }
}
