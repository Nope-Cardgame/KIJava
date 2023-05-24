package ai.marian;

import ai.IArtificialIntelligence;
import gameobjects.Game;
import gameobjects.Player;
import gameobjects.actions.*;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AIMarian implements IArtificialIntelligence {
    public String calculateNextMove(Game game) {
        return decide(game);
    }

    /**
     * this methods decides how to react on a game state by calculating the cards
     * @param g
     * @return move.toJSON()
     */
    public String decide(Game g) {
        Action move;
        if (g.getLastAction().getType().equals("take") && !g.getState().equals("turn_start")) {
            move = createActionAfterTakingCard(g);
        } else {
            move = createActionBeforeTakingCard(g);
        }
        return move.toJSON();
    }

    /**
     * return a single card from player
     * @param p
     * @return oneCard
     */
    public List<Card> getCard(Player p) {
        List<Card> oneCard = new ArrayList<>();
        oneCard.add(p.getCards().get(0));
        return oneCard;
    }

    /**
     * returns the possible sets with the top card and the cards on the playerhand
     * @param topCard
     * @param p
     * @return List<Cards> set
     */
    public List<Card> getSet(Card topCard, Player p) {
        NumberCard topAsNumber = (NumberCard) topCard;
        List<Card> set = new ArrayList<>();
        List<String> colors = topCard.getColors();
        for(int iterator = 0; iterator < colors.size() && set.size() != topAsNumber.getValue(); iterator++) {
            set = createCardSet(topCard, colors.get(iterator), p);
        }
        return set;
    }
    /**
     create a set of cards from player with right color
     */
    private List<Card> createCardSet(Card topCard, String c, Player p) {
        List<Card> setOfColor = new ArrayList<>();
        for (Card card : p.getCards()) {
            if(hasColor(c, card)) {
                setOfColor.add(card);
            }
            NumberCard numberCard = (NumberCard) topCard;
            if (setOfColor.size() == numberCard.getValue()) {
                break;
            }
        }
        return setOfColor;
    }

    /**
     * when a card is already take, the player has to choose between dircarding or taking further cards
     * @param g
     * @return Action action
     */
    public Action createActionAfterTakingCard(Game g) {
        Player p = g.getCurrentPlayer();
        Action action = null;
        if (g.getDiscardPile().get(0).getCardType().equals("number")) {
            if (hasCompleteSet(p, g.getDiscardPile().get(0))) {
                List<Card> discardCards = getSet(g.getDiscardPile().get(0),p);
                action = createActionIfNominated(g, discardCards);
            } else {
                action = new SayNope("nope","no cards to discard",p);
            }
        } else {
            if (g.getDiscardPile().get(0).getCardType().equals("nominate")) {
                NominateCard nominateCard = (NominateCard) g.getLastAction();
                List<Card> oneCardDiscard = getCard(nominateCard.getNominatedColor(), p);
                if (!oneCardDiscard.isEmpty()) {
                    action = createActionIfNominated(g, oneCardDiscard);
                } else {
                    action = new SayNope("nope", "no cards to discard",p);
                }

            }
        }
        return action;
    }

    /**
     * decide about move before taking first card
     * @param g
     * @return Action move
     */
    public Action createActionBeforeTakingCard(Game g) {
        Action move = null;
        if (g.getDiscardPile().get(0).getCardType().equals("reset") && g.getDiscardPile().size() == 1) {
            List<Card> discardedCards = getCard(g.getCurrentPlayer());
            if (discardedCards.get(0).getCardType().equals("nominate")) {
                move = new NominateCard("nominate","found nominate",discardedCards.size(),discardedCards,g.getCurrentPlayer(),g.getCurrentPlayer(),discardedCards.get(0).getColors().get(0),1);
            } else {
                move = new DiscardCard("discard","had to discard :(",discardedCards.size(),discardedCards,g.getCurrentPlayer());
            }
        } else if (g.getDiscardPile().get(0).getCardType().equals("nominate") && g.getDiscardPile().size() == 1) {
            move = new NominateCard("nominate","nominate was first card",0,new ArrayList<>(),g.getCurrentPlayer(),g.getCurrentPlayer(),g.getDiscardPile().get(0).getColors().get(0),1);
        }
        if (g.getDiscardPile().get(0).getCardType().equals("number")) {
            if (hasCompleteSet(g.getCurrentPlayer(),g.getDiscardPile().get(0))) {
                List<Card> discardCards = getSet(g.getDiscardPile().get(0),g.getCurrentPlayer());
                move = createActionIfNominated(g, discardCards);
            } else {
                move = new TakeCard("take","no cards to discard",1,new ArrayList<>(),g.getCurrentPlayer());
            }
        } else {
            if (g.getDiscardPile().get(0).getCardType().equals("nominate")) {
                NominateCard nominateCard = (NominateCard) g.getLastAction();
                List<Card> oneCardDiscard = getCard(nominateCard.getNominatedColor(),g.getCurrentPlayer());
                if (!oneCardDiscard.isEmpty()) {
                    move = createActionIfNominated(g, oneCardDiscard);
                } else {
                    move = new TakeCard("nominate","no cards to discard",1,new ArrayList<>(),g.getCurrentPlayer());
                }
            }
        }
        return move;
    }

    /**
     * if the player is nominated, it has to decide about how to react
     * @param g
     * @param oneCardDiscard
     * @return Action move
     */
    @NotNull
    private Action createActionIfNominated(Game g, List<Card> oneCardDiscard) {
        Action move;
        if (oneCardDiscard.get(0).getCardType().equals("nominate")) {
            move = new NominateCard(
                    "nominate",
                    "nomination was on first place",
                    oneCardDiscard.size(),
                    oneCardDiscard,
                    g.getCurrentPlayer(),
                    g.getCurrentPlayer(),
                    oneCardDiscard.get(0).getColors().get(0),1
            );
        } else {
            move = new DiscardCard("discard","had to discard cards",oneCardDiscard.size(),oneCardDiscard,g.getCurrentPlayer());
        }
        return move;
    }

    /**
     * check if a player has c complete set concering a given card
     * @param p
     * @param c
     * @return boolean erg
     */
    public boolean hasCompleteSet(Player p, Card c) {
        NumberCard numberCard = (NumberCard) c;
        List<String> colors = c.getColors();
        boolean erg = false;
        /*iterate through all colors of the card */
        for (int iterator = 0; iterator < colors.size() && !erg; iterator++) {
            erg = hasSetWithColor(numberCard,colors.get(iterator), p);
        }
        return erg;
    }

    /**
     * check if a player has enough cards of a given color
     * @param topCard
     * @param c
     * @param p
     * @return int count
     */
    private boolean hasSetWithColor(NumberCard topCard, String c, Player p) {
        int count = 0; //count of cards
        for(Card card: p.getCards()) {
            if (hasColor(c, card)) {
                count +=1;
            }
        }
        return count >= (topCard).getValue();
    }

    /**
     * checks if a card has a given color (or more)
     * @param colorValue
     * @param c
     * @return boolean found
     */
    public boolean hasColor(String colorValue, Card c) {
        boolean found = false;
        for (String color: c.getColors()) {
            if (color.equals(colorValue)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * return the cards a player has to give to stack
     * @param color
     * @param p
     * @return List<Card> cards
     */
    public List<Card> getCard(String color, Player p) {
        List<Card> cards = new ArrayList<>();
        for(Card card : p.getCards()) {
            if (hasColor(color, card)) {
                cards.add(card);
                break;
            }
        }
        return cards;
    }


}