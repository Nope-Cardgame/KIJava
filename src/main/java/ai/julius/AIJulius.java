package ai.julius;

import ai.IArtificialIntelligence;
import gameobjects.Game;

public class AIJulius implements IArtificialIntelligence {
    private Decider decider;

    /**
     * Standard constructor for a AIJulius instance
     * @param decider the decider indicates whether the AI is only making valid
     *                moves, or whether the AI is gonna crush.
     */
    public AIJulius(Decider decider) {
        this.decider = decider;
    }
    @Override
    public String calculateNextMove(Game game) {
        return decider.decide(game);
    }
}
