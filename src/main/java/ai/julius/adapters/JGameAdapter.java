package ai.julius.adapters;

import gameobjects.Game;
import gameobjects.cards.Card;

public class JGameAdapter {
    private final Game game;

    /**
     * Standard-Constructor for a JGameAdapter
     * instance
     *
     * @param game the game that needs to be performed on
     */
    public JGameAdapter(Game game) {
        this.game = game;
    }

    /**
     * loops through the discard-Pile until there is no more invisible-card
     *
     * @implNote Only call this function if you can assume that
     *           the top card is not the only card in the discard pile of the game
     *
     * @return the card value of that specific card, null if that card does not exist...
     */
    public Card getTopCard() {
        Card returnValue = null;
        for (int i = 0; i < game.getDiscardPile().size() && returnValue == null; i++) {
            if (!game.getDiscardPile().get(i).getCardType().equals("invisible")) {
                returnValue = game.getDiscardPile().get(i);
            }
        }
        return returnValue;
    }

    /**
     * checks if the last
     *
     * @param action the action we want to check
     * @return true if lastAction is equal to action, false otherwise
     */
    public boolean isLastAction(String action) {
        return game.getLastAction().getType().equals(action);
    }
}
