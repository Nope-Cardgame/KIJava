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

    public ActionCard(JSONObject jsonObject) {
        super(jsonObject);
        try {
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
        JSONObject object = new JSONObject();
        // TODO: 09.05.2023 parsing object back to json object 
        return object;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }
}
