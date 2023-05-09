package gameobjects.actions;

import gameobjects.Jsonable;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Action implements Jsonable {
    private String type;
    private String explanation;

    public Action(String type, String explanation) {
        this.type = type;
        this.explanation = explanation;
    }

    public Action(JSONObject jsonObject) {
        try {
            this.type = jsonObject.getString("type");
            this.explanation = jsonObject.getString("explanation");
        } catch(JSONException e) {
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
