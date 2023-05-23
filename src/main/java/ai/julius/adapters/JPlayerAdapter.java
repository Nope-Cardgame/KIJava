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
     * Checks if the Player has a set of a specific Color
     *
     * @param topCard the top-card of the discard pile
     * @param color the color we're looking in that card
     * @return true if a set is completed, false otherwise
     */
    public boolean hasCompleteSetWithColor(NumberCard topCard, String color) {
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
     * Checks if a Player has a complete set
     *
     * @param amount the amount of Cards the set needs to have at least
     * @param color the color of the Set
     *
     * @return true, if player has a complete Set, false otherwise
     */
    public boolean hasCompleteSetWithColor(int amount, String color) {
        int amountCards = 0;
        for(Card card: player.getCards()) {
            CardAdapter cardAdapter = new CardAdapter(card);
            if (cardAdapter.hasColor(color)) {
                amountCards++;
            }
        }
        return amountCards >= amount;
    }

    /**
     * Creates a stupid set of Colors
     *
     * @param topCard the top card of the game
     * @param color the color that needs to be created with
     * @return the stupidest list ever
     */
    public List<Card> getStupidSetColor(Card topCard, String color) {
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
     * Creates a Stupid Set of Colors
     *
     * @param amount the amount of Cards the Set needs to obtain
     * @param color the Color the Set need to provide
     *
     * @return a list of Cards that has the Amount of Cards in it
     */
    public List<Card> getStupidSetColor(int amount, String color) {
        List<Card> cardSet = new ArrayList<>();
        for (Card card : player.getCards()) {
            CardAdapter cardAdapter = new CardAdapter(card);
            if(cardAdapter.hasColor(color)) {
                cardSet.add(card);
            }
            if (cardSet.size() == amount) {
                break;
            }
        }
        return cardSet;
    }

    /**
     * Looks for the first card with a given Color in a specific Set
     *
     * @param nominatedColor the Color we're looking for in that specific card
     * @return a List with only one Card containing, list is empty
     *         if there is no card with that nominatedColor!
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
