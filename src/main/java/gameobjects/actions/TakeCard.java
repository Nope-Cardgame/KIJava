package gameobjects.actions;

import org.json.JSONObject;

public class TakeCard extends Action{
    public TakeCard(String type, String explanation) {
        super(type, explanation);
    }

    public TakeCard(String jsonString) {
        super(jsonString);
    }

    @Override
    public String toJSON() {
        return null;
    }
}
