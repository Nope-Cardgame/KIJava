package gameobjects.actions;

import org.json.JSONObject;

public class NominateCard extends Action{
    public NominateCard(String type, String explanation) {
        super(type, explanation);
    }

    public NominateCard(String jsonObject) {
        super(jsonObject);
    }

    @Override
    public String toJSON() {
        return null;
    }
}
