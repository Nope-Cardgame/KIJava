package ai;

import gameobjects.Game;
import gameobjects.actions.Action;

public interface IArtificialIntelligence {
    /**
     * calculates for a given Game the next best move
     *
     * @param game the current state of the whole game
     * @return an Action-Object in JSON-Format
     *
     * @see <a href="https://github.com/Nope-Cardgame/Doku/blob/main/Schnittstellen/Schnittstellen.md">
     *      Schnittstellen Dokumentation</a>
     * @see Game
     * @see Action
     */
    String calculateNextMove(Game game);
}
