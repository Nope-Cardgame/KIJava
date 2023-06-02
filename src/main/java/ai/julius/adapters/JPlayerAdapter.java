package ai.julius.adapters;

import gameobjects.Player;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.util.*;

public class JPlayerAdapter {
    private final Player player;

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
        if(containsNominate(cardSet)) {
            List<Card> newCardList = new ArrayList<>();
            newCardList.add(findNominate(cardSet));
            cardSet = newCardList;
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
        if(containsNominate(cardSet)) {
            List<Card> newCardList = new ArrayList<>();
            newCardList.add(findNominate(cardSet));
            cardSet = newCardList;
        }
        return cardSet;
    }

    private boolean containsNominate(List<Card> cards) {
        boolean found = false;
        for (Card card : cards) {
            if (card.getCardType().equals("nominate")) {
                found = true;
                break;
            }
        }
        return found;
    }

    private Card findNominate(List<Card> cards) {
        Card card = null;
        for(int iterator = 0; iterator < cards.size() && card == null; iterator++) {
            if (cards.get(iterator).getCardType().equals("nominate")) {
                card = cards.get(iterator);
            }
        }
        return card;
    }

    private List<Card> sortNominatedFirst(List<Card> cardListToSort) {
        cardListToSort.sort((o1, o2) -> {
            int returnValue = 0;
            if (o1.getCardType().equals("nominate") || o2.getCardType().equals("nominate")) {
                returnValue = -1;
            } else if (o1.getCardType().equals("nominate") && !o2.getCardType().equals("nominate")) {
                returnValue = -1;
            } else if (!o1.getCardType().equals("nominate") && o2.getCardType().equals("nominate")) {
                returnValue = 1;
            }
            return returnValue;
        });
        return cardListToSort;
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

    /**
     * @return any card in the player's inventory
     */
    public List<Card> getStupidCard() {
        List<Card> oneCard = new ArrayList<>();
        oneCard.add(player.getCards().get(0));
        return oneCard;
    }

    /**
     * Checks if a Player has a specific Color on his hands
     *
     * @param color the color we're looking for
     * @return true if player has color, false otherwise
     */
    public boolean hasColor(String color) {
        for(Card card : player.getCards()) {
            for (String cardColor : card.getColors()) {
                if (cardColor.equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * returns the best color of the player which he has on his hands
     * (best color for me is where he got the lessest amount of cards of)
     *
     * @return the best Color as string
     */
    public String getSmartColor() {
        Set<String> colors = new HashSet<>();
        colors.add("red");
        colors.add("blue");
        colors.add("yellow");
        colors.add("green");
        Iterator<String> iterator = colors.iterator();

        int maxColorAmount = Integer.MAX_VALUE;
        String bestColor = iterator.next();
        while (iterator.hasNext()) {
            String currentColor = iterator.next();
            int currentCardAmount = 0;
            for (Card card : player.getCards()) {
                CardAdapter cardAdapter = new CardAdapter(card);
                if (cardAdapter.hasColor(currentColor)) {
                    currentCardAmount++;
                }
            }
            if (currentCardAmount < maxColorAmount) {
                bestColor = currentColor;
                maxColorAmount = currentCardAmount;
            }
        }
        return bestColor;
    }

    public Card getSmartCard(String colorOfInvisible) {
        return null;
    }
}
