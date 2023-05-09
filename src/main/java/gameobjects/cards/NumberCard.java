package gameobjects.cards;

import gameobjects.cards.Card;

import java.util.List;

public class NumberCard extends Card {
    private int value;
    private List<String> color;

    public NumberCard(String type, String name) {
        super(type, name);
    }
}
