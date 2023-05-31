package ai.julius.adapters;

import gameobjects.cards.Card;

public class CardAdapter {
    private Card card;

    /**
     * Standard-Constructor for a CardAdapter
     * instance
     *
     * @param card the card where the operations are happening
     */
    public CardAdapter(Card card) {
        this.card = card;
    }

    /**
     * Checks whether a Card has a specific Color
     *
     * @param colorValue the color Value to check for
     * @return true, if the Color is available in that
     *         Card, false otherwise
     */
    public boolean hasColor(String colorValue) {
        boolean found = false;
        for (String color: card.getColors()) {
            if (color.equals(colorValue)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Checks if a card has two Colors or not
     *
     * @return true if that's so, false otherwise
     */
    public boolean hasTwoColors() {
        return this.card.getColors().size() == 2;
    }
}