package gameobjects.actions;

import org.json.JSONObject;

public class SayNope extends Action{
    public SayNope(String type, String explanation) {
        super(type, explanation);
    }

    public SayNope(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }
}
