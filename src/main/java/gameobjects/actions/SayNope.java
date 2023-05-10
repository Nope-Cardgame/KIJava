package gameobjects.actions;

import org.json.JSONObject;

public class SayNope extends Action{
    public SayNope(String type, String explanation) {
        super(type, explanation);
    }

    public SayNope(String jsonString) {
        super(jsonString);
    }


    @Override
    public String toJSON() {
        return null;
    }
}
