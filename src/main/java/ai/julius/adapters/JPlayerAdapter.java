package ai.julius.adapters;

import gameobjects.Player;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.util.ArrayList;

public class JPlayerAdapter {
    private Player player;

    public JPlayerAdapter(Player player) {
        this.player = player;
    }

    /**
     * checks if a Player has a complete Set of a certain Card
     * on his hand.
     *
     * @param topCard the Card that is on top of the discardPile which
     *                is actually a numberCard
     *
     * @return true if the player has an actual set of Cards acc.
     *         to the Game rules, false otherwise
     */
    public boolean hasCompleteSet(Card topCard) {
        // if topCard is not numberCard, throw exception
        if(topCard.getCardType().equals("number")) {
            throw new RuntimeException("This function can only be called" +
                    "if the passed Card is a NumberCard");
        }
        NumberCard numberCard = (NumberCard) topCard;
        boolean erg = false;
        int amountCards = 0;
        for (Card card: player.getCards()) {

        }
        erg |= amountCards >= ((NumberCard) topCard).getValue();

        return erg;
    }
}
