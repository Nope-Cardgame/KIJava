package gameobjects.cards;

import gameobjects.cards.Card;

import java.util.List;

public class ActionCard extends Card {
    private List<String> colors;
    public ActionCard(String type, String name, List<String> colors) {
        super(type, name);
        this.colors = colors;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }
}
