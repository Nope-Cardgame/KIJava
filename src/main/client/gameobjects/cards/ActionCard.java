package gameobjects.cards;

import com.google.gson.Gson;
import java.util.List;

public class ActionCard extends Card {

    /**
     * Standard Constructor for a ActionCard-Object
     *
     * @param type the type of this card
     * @param colors the colors that the card has
     * @param name the name of the card
     */
    public ActionCard(String type, List<String> colors, String name) {
        super(type, name, colors);
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
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        ActionCard other = (ActionCard) obj;
        return other.getColors().equals(this.getColors())
                && other.getName().equals(this.getName())
                && other.getCardType().equals(this.getCardType());
    }
    public String getSpritePath(){
        return  "sprites/"+getName()+".png";
    }
}
