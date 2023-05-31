package ai.julius.smart;

import ai.julius.Decider;
import gameobjects.Game;
import gameobjects.actions.Action;

public class JAISmart implements Decider {

    @Override
    public Action actionBeforeTakeCard(Game game) {
        return null;
    }

    @Override
    public Action actionAfterTakeCard(Game game) {
        return null;
    }
}