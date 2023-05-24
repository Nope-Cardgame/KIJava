package ai.julius.adapters;

import gameobjects.Player;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.util.ArrayList;
import java.util.List;

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
        NumberCard numberCard = (NumberCard) topCard;
        List<String> colors = topCard.getColors();
        boolean erg = false;
        for (int iterator = 0; iterator < colors.size() && !erg; iterator++) {
            erg = hasSetWithColor(numberCard,colors.get(iterator));
        }
        return erg;
    }

    /**
     * Checks if the Player has a set of a specific Color
     *
     * @param topCard the top-card of the discard pile
     * @param color the color we're looking in that card
     * @return true if a set is completed, false otherwise
     */
    private boolean hasSetWithColor(NumberCard topCard, String color) {
        int amountCards = 0;
        for(Card card: player.getCards()) {
            CardAdapter cardAdapter = new CardAdapter(card);
            if (cardAdapter.hasColor(color)) {
                amountCards++;
            }
        }
        return amountCards >= ((NumberCard) topCard).getValue();
    }

    /**
     * Creates a "stupid" set of Cards which the Player can use later on
     *
     * @param topCard the color of the set
     * @return a random and stupid set only for valid moves
     */
    public List<Card> getStupidSet(Card topCard) {
        NumberCard topAsNumber = (NumberCard) topCard;
        List<Card> set = new ArrayList<>();
        List<String> colors = topCard.getColors();
        for(int iterator = 0; iterator < colors.size() && set.size() != topAsNumber.getValue(); iterator++) {
            set = getStupidSetColor(topCard, colors.get(iterator));
        }
        return set;
    }

    /**
     * Creates a stupid set of Colors
     *
     * @param topCard the top card of the game
     * @param color the color that needs to be created with
     * @return the stupidest list ever
     */
    private List<Card> getStupidSetColor(Card topCard, String color) {
        List<Card> cardSet = new ArrayList<>();
        for (Card card : player.getCards()) {
            CardAdapter cardAdapter = new CardAdapter(card);
            if(cardAdapter.hasColor(color)) {
                cardSet.add(card);
            }
            NumberCard numberCard = (NumberCard) topCard;
            if (cardSet.size() == numberCard.getValue()) {
                break;
            }
        }
        return cardSet;
    }

    /**
     * Looks for the first card with a given Color in a specific Set
     *
     * @param nominatedColor the Color we're looking for in that specific card
     * @return a List with only one Card containing
     */
    public List<Card> getStupidCard(String nominatedColor) {
        List<Card> cards = new ArrayList<>();
        for(Card card : player.getCards()) {
            CardAdapter cardAdapter = new CardAdapter(card);
            if (cardAdapter.hasColor(nominatedColor)) {
                cards.add(card);
                break;
            }
        }
        return cards;
    }

    public List<Card> getStupidCard() {
        List<Card> oneCard = new ArrayList<>();
        oneCard.add(player.getCards().get(0));
        return oneCard;
    }
}