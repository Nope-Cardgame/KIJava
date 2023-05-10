package gameobjects.cards;

import com.google.gson.Gson;
import java.util.List;

public class ActionCard extends Card {
    // Contains the colors that are used by this type of card
    private List<String> color;

    /**
     * Standard Constructor for a ActionCard-Object
     *
     * @param type the type of this card
     * @param color the colors that the card has
     * @param name the name of the card
     */
    public ActionCard(String type, List<String> color, String name) {
        super(type, name);
        this.color = color;
    }

    /**
     * Constructs the ActionCard by using a Json-String
     *
     * @param jsonString the Json-Object-String containing
     *                   - a "type"-property as String
     *                   - a "color"-property, which is an array of Strings
     *                   - a "name"-property, which has the name of the card as string
     */
    public ActionCard(String jsonString) {
        super(jsonString);
        this.color = new Gson().fromJson(jsonString,getClass()).getColors();
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public List<String> getColors() {
        return color;
    }

    public void setColors(List<String> color) {
        this.color = color;
    }
}
