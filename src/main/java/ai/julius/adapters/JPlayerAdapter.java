package ai.julius.adapters;

import gameobjects.Player;
import gameobjects.cards.Card;

import java.util.ArrayList;

public class JPlayerAdapter {
    private Player player;

    public JPlayerAdapter(Player player) {
        this.player = player;
    }

    public boolean hasCompleteSet(Card topCard) {
        return false;
    }
}
