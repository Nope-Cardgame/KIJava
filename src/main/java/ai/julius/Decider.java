package ai.julius;

import ai.julius.adapters.JGameAdapter;
import gameobjects.Game;
import gameobjects.actions.Action;

public interface Decider {
    /**
     * Adapter interface for strategy pattern which
     * is used to put a strategy into the strategy
     * (either a Valid AI that makes only the next valid move,
     * or a smart AI which tends to make the next best move)
     *
     * @param game the game state the decider needs to decide on that strategy
     * @return a String that indicates the next move
     */
    String decide(Game game);
    Action actionBeforeTakeCard(Game game);
    Action actionAfterTakeCard(Game game);
}