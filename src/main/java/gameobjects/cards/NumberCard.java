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

    public NumberCard(JSONObject jsonObject) {
        super(jsonObject);
        try {
            this.value = (int) jsonObject.get("value");
            this.colors = new ArrayList<>();
            JSONArray colorArray = jsonObject.getJSONArray("color");
            for (int colorIterator = 0; colorIterator < colorArray.length(); colorIterator++) {
                colors.add(colorArray.getString(colorIterator));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject toJSONObject() {
        // TODO: 09.05.2023 implement feature: object back to json
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
