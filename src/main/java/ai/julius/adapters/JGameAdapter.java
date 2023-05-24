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
     * @return the top card in the game's discard pile
     */
    public Card getTopCard() {
        return game.getDiscardPile().get(0);
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
