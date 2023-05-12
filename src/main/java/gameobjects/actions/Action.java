package gameobjects.actions;

import gameobjects.IJsonable;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Action implements IJsonable {
    private String type;
    private String explanation;

    /**
     * Standard-Constructor for an Action-Instance
     * @param type the type of this Action
     * @param explanation
     */
    public Action(String type, String explanation) {
        this.type = type;
        this.explanation = explanation;
    }

    /**
     * Constructs an Action-Instance
     * with a json-String
     *
     * @param jsonString a JSON-formatted String which contains
     *                   - a "type"-property
     *                   - an "explanation"-property
     */
    public Action(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.explanation = jsonObject.getString("explanation");
            this.type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
