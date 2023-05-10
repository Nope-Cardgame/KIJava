package gameobjects.actions;

import org.json.JSONObject;

public class DiscardCard extends Action{
    public DiscardCard(String type, String explanation) {
        super(type, explanation);
    }

    public DiscardCard(JSONObject jsonObject) {
        super(jsonObject);
    }
    @Override
    public String toJSON() {
        return null;
    }
}
