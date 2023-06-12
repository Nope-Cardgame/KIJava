package ai.julius;

import ai.julius.adapters.JGameAdapter;
import gameobjects.Game;
import gameobjects.actions.Action;
import logging.NopeLogger;

import java.util.logging.Logger;

public interface Decider {
    static final Logger LOG = NopeLogger.getLogger("Decider");
    /**
     * Adapter interface for strategy pattern which
     * is used to put a strategy into the strategy
     * (either a Valid AI that makes only the next valid move,
     * or a smart AI which tends to make the next best move)
     *
     * @param game the game state the decider needs to decide on that strategy
     * @return a String that indicates the next move
     */
    default String decide(Game game){
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        if (gameAdapter.isLastAction("take") && !game.getState().equals("turn_start")) {
            action = actionAfterTakeCard(game);
        } else {
            action = actionBeforeTakeCard(game);
        }
        assert action != null;
        LOG.info("Played Action: " + action.getClass().getSimpleName() + ": " + action.toJSON());
        return action.toJSON();
    }

    /**
     * Calculates an action String when you haven't drawn
     * a card yet
     *
     * @param game the game that you need to calculate your next move
     * @return an Action-Object as JSON-String (must be valid)
     */
    Action actionBeforeTakeCard(Game game);

    /**
     * Calculates an action String when you have drawn a card
     *
     * @param game the game that you need to calculate your next move
     * @return an Action-Object as JSON-String (must be valid)
     */
    Action actionAfterTakeCard(Game game);
}
