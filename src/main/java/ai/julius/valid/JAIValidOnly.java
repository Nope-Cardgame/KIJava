package ai.julius.valid;

import ai.julius.Decider;
import ai.julius.adapters.JGameAdapter;
import ai.julius.adapters.JPlayerAdapter;
import gameobjects.Game;
import gameobjects.actions.Action;
import gameobjects.actions.DiscardCard;
import gameobjects.actions.TakeCard;

import java.util.ArrayList;

public class JAIValidOnly implements Decider {

    @Override
    public String decide(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.isLastAction("takeCard")) {
            if (playerAdapter.hasCompleteSet(gameAdapter.getTopCard())) {
            } else {
                action = new TakeCard("takeCard","no cards to discard",1,new ArrayList<>(),game.getCurrentPlayer());
            }
        } else {

        }
        assert action != null;
        return action.toJSON();
    }
}
