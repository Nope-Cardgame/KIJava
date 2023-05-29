package gameobjects.cards;

import com.google.gson.Gson;
import java.util.List;

public class NumberCard extends Card {
    private int value;

    /**
     * Standard-Constructor for a NumberCard-instance
     *
     * @param type the card type ("number" or an action-card)
     * @param value the number value of this card
     * @param colors the colors of this card
     * @param name the name of this card
     */
    public NumberCard(String type, int value, List<String> colors, String name) {
        super(type, name, colors);
        this.value = value;
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
        this.value = gson.fromJson(jsonString,getClass()).getValue();
    }
    @Override
    public boolean equals(Object obj) {
        NumberCard other = (NumberCard) obj;
        return other.getColors().equals(this.getColors())
                && other.getName().equals(this.getName())
                && other.getCardType().equals(this.getCardType())
                && other.getValue() == this.getValue();
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSpritePath(){
        String output = "sprites\\" + colors.get(0);
        if(colors.size() == 2){
            output += colors.get(1);
        }
        if(colors.size() == 4){
            output ="sprites\\wildcard";
        }
        output += value + ".png";

        return output;
    }
}
