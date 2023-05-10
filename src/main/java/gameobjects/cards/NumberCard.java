package gameobjects.cards;

import com.google.gson.Gson;
import java.util.List;

public class NumberCard extends Card {
    private int value;
    private List<String> color;

    /**
     * Standard-Constructor for a NumberCard-instance
     *
     * @param type the card type ("number" or an action-card)
     * @param value the number value of this card
     * @param color the colors of this card
     * @param name the name of this card
     */
    public NumberCard(String type, int value, List<String> color, String name) {
        super(type, name);
        this.value = value;
        this.color = color;
    }

    /**
     * Constructor which uses a JSON-String
     *
     * @param jsonString a JSON-String which contains
     *                   - type-property as String
     *                   - value-property as int
     *                   -
     */
    public NumberCard(String jsonString) {
        super(jsonString);
        Gson gson = new Gson();
        this.color = gson.fromJson(jsonString,getClass()).getColor();
        this.value = gson.fromJson(jsonString,getClass()).getValue();
    }
    @Override
    public boolean equals(Object obj) {
        ActionCard other = (ActionCard) obj;
        return other.getColor().equals(this.getColor())
                && other.getName().equals(this.getName())
                && other.getCardType().equals(this.getCardType());
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
