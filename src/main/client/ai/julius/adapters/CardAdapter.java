package ai.julius.adapters;

import gameobjects.cards.Card;
import logging.NopeLogger;

import java.util.logging.Logger;

public class CardAdapter {
    private final Card card;
    private static final Logger LOG = NopeLogger.getLogger("CardAdapter");

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
        return card.getColors().stream()
                .anyMatch(color -> color.equals(colorValue));
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
