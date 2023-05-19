package gameobjects.actions;

import org.json.JSONException;
import org.json.JSONObject;

public class ActionFactory {
    /**
     * Creates a specific Action instance with a valid
     * jsonString
     *
     * @param jsonString Action json which must be valid
     * @return the correct Action Instance
     */
    public static Action getAction(String jsonString) {
        Action action = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String type = jsonObject.getString("type");
            if (type.equals("disqualify")) {
                action = new DisqualifyPlayer(jsonString);
            } else if (type.equals("take")) {
                action = new TakeCard(jsonString);
            } else if (type.equals("discard")) {
                action = new DiscardCard(jsonString);
            } else if (type.equals("nope")) {
                action = new SayNope(jsonString);
            } else {
                action = new NominateCard(jsonString);
            }
        } catch (JSONException e) {
        }
        return action;
    }
}
