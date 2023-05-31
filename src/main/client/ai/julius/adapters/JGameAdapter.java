package ai.julius.adapters;

import gameobjects.Game;
import gameobjects.Player;
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
        if (invisibleOnly()) {
            return game.getDiscardPile().get(0);
        }
        for (int i = 0; i < game.getDiscardPile().size() && returnValue == null; i++) {
            if (!game.getDiscardPile().get(i).getCardType().equals("invisible")) {
                returnValue = game.getDiscardPile().get(i);
            }
        }
        return returnValue;
    }


    public boolean invisibleOnly() {
        return game.getDiscardPile().size() == 1 && game.getDiscardPile().get(0).getCardType().equals("invisible");
    }

    public boolean nominateOnly() {
        return game.getDiscardPile().size() == 1 && game.getDiscardPile().get(0).getCardType().equals("nominate");
    }

    public boolean resetOnly() {
        return game.getDiscardPile().size() == 1 && game.getDiscardPile().get(0).getCardType().equals("reset");
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

    /**
     * simply returns a player instance that is not equal to the current Player
     *
     * @return the Player that's socketID is not equal to the current players socket id
     */
    public Player getStupidPlayer() {
        Player anotherPlayer = null;
        for (int iterator = 0; iterator < game.getPlayers().size() && anotherPlayer == null; iterator++) {
            if (game.getPlayers().get(iterator).getSocketId().equals(game.getCurrentPlayer().getSocketId())) {
                anotherPlayer = game.getPlayers().get(iterator);
            }
        }
        return anotherPlayer;
    }
}