package gameobjects.cards;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class CardFactory {
    /**
     * Creates a Card out of the specific JSON-String
     * @param jsonString the Json-String for a specific card
     *
     * @return a Card, either ActionCard or NumberCard
     */
    public static Card getCard(String jsonString) {
        String type = "";
        try {
            // extract the property "type"
            JSONObject jsonObject = new JSONObject(jsonString);
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (type.equals("number")) {
            return new NumberCard(jsonString);
        } else {
            return new ActionCard(jsonString);
        }
    }
}
