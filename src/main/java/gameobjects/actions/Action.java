package gameobjects.actions;

import com.google.gson.Gson;
import gameobjects.Jsonable;

public abstract class Action implements Jsonable {
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
        Gson gson = new Gson();
        this.explanation = gson.fromJson(jsonString, getClass()).getExplanation();
        this.type = gson.fromJson(jsonString,getClass()).getType();
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
