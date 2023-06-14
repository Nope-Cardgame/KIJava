package ai.julius.adapters;

import gameobjects.Player;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import logging.NopeLogger;

import java.util.*;
import java.util.logging.Logger;

public class JPlayerAdapter {
    private final Player player;
    private static final Logger LOG = NopeLogger.getLogger(JPlayerAdapter.class.getSimpleName());

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
        LOG.info("in hasCompleteSetWithColor: " + (amountCards >= amount));
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
        return player.getCards().stream()
                .flatMap(card -> card.getColors().stream())
                .anyMatch(cardColor -> cardColor.equals(color));
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
        // Loop through all colors
        while (iterator.hasNext()) {
            String currentColor = iterator.next();
            int currentCardAmount = 0;
            for (Card card : player.getCards()) {
                CardAdapter cardAdapter = new CardAdapter(card);
                // if a color is available, increment the amount
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

    /**
     * returns a smart card depending on a color
     *
     * @param color color the card must contain
     * @return the best card for that specific color
     */
    public Card getSmartCard(String color) {
        // for reset
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("reset")) {
                return card;
            }
        }
        // for nominate
        for (Card card : player.getCards()) {
            CardAdapter cardAdapter = new CardAdapter(card);
            if (card.getCardType().equals("nominate")) {
                if (cardAdapter.hasColor(color)) {
                    return card;
                }
            }
        }
        // for invisible
        for (Card card : player.getCards()) {
            CardAdapter cardAdapter = new CardAdapter(card);
            if (card.getCardType().equals("invisible")) {
                if (cardAdapter.hasColor(color)) {
                    return card;
                }
            }
        }

        // looking for wildcard
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("number")) {
                if (card.getName().equals("wildcard")) {
                    return card;
                }
            }
        }

        // looking for multiple color
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("number")) {
                CardAdapter cardAdapter = new CardAdapter(card);
                if (cardAdapter.hasTwoColors() && cardAdapter.hasColor(color)) {
                    return card;
                }
            }
        }

        // looking for another card
        for (Card card : player.getCards()) {
            CardAdapter cardAdapter = new CardAdapter(card);
            if (card.getCardType().equals("number")) {
                if (cardAdapter.hasColor(color)) {
                    return card;
                }
            }
        }

        return null;
    }

    /**
     * returns a smart card when the color doesn't matter
     *
     * @return a smart card for the player
     */
    public Card getSmartCard() {
        // Looking for Reset
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("reset")) {
                return card;
            }
        }
        // Looking for nominate
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("nominate")) {
                return card;
            }
        }
        // looking for invisible
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("invisible")) {
                return card;
            }
        }
        // looking for wildcard
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("number")) {
                if (card.getName().equals("wildcard")) {
                    return card;
                }
            }
        }
        // looking for multiple color
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("number")) {
                CardAdapter cardAdapter = new CardAdapter(card);
                if (cardAdapter.hasTwoColors()) {
                    return card;
                }
            }
        }

        // looking for another card
        for (Card card : player.getCards()) {
            if (card.getCardType().equals("number")) {
                return card;
            }
        }
        // returning not null here
        return player.getCards().get(0);
    }

    /**
     * Checks if the player has action cards in his
     * inventory
     *
     * @return true if that is the case, false otherwise
     */
    public boolean hasActionCards() {
        return player.getCards().stream().
                anyMatch(card -> !card.getCardType().equals("number"));
    }

    /**
     * Checks if a player contains a specific action card
     * depending on a color
     *
     * @param color the color we are looking for in that specific action card
     * @return true, if there is an action card with that specific color, false otherwise
     */
    public boolean hasActionCards(String color) {
        return player.getCards().stream()
                .filter(card -> {
                    CardAdapter cardAdapter = new CardAdapter(card);
                    return cardAdapter.hasColor(color);
                })
                .anyMatch(card -> !card.getCardType().equals("number"));
    }

    /**
     * returns a smart for a given numbercard and a valid color
     *
     * @param currentCard the current card displayed on the discard pile
     * @param color the color necessary for this purpose
     *
     * @return a smart set for that specific color
     */
    public List<Card> getSmartSet(NumberCard currentCard, String color) {
        return getSmartSetCallback(currentCard.getValue(),color);
    }

    /**
     * Creates a smart set and returns it
     *
     * @param amount the amount of cards the set needs to contain
     * @param color the color the set needs to provide
     * @return a valid set of cards
     */
    public List<Card> getSmartSet(int amount, String color) {
        return getSmartSetCallback(amount,color);
    }

    private List<Card> getSmartSetCallback(int amount, String color) {
        List<Card> set = new ArrayList<>();
        for (Card card : player.getCards()) {
            if (amount == set.size()) {
                break;
            }
            CardAdapter cardAdapter = new CardAdapter(card);
            if (cardAdapter.hasColor(color)) {
                if (card.getName().equals("wildcard")) {
                    set.add(card);
                    continue;
                } else if (cardAdapter.hasTwoColors()) {
                    set.add(card);
                    continue;
                }
                set.add(card);
            }
        }
        return set;
    }
}
