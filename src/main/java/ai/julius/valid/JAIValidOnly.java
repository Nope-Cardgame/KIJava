package ai.julius.valid;

import ai.julius.Decider;
import ai.julius.adapters.JGameAdapter;
import ai.julius.adapters.JPlayerAdapter;
import gameobjects.Game;
import gameobjects.actions.Action;

public class JAIValidOnly implements Decider {

    @Override
    public String decide(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.isLastAction("takeCard")) {

        } else {

        }
        return action.toJSON();
    }
}
