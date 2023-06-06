package ai.alexander;

import ai.IArtificialIntelligence;
import gameobjects.Game;
import gameobjects.Player;
import gameobjects.actions.*;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AIAlexander implements IArtificialIntelligence {
    public String calculateNextMove(Game game) {
        return decide(game);
    }

    /**
     * this methods decides how to react on a game state by calculating the cards
     * @param g
     * @return
     */
    public String decide(Game g) {
        Action move;
        if (g.getLastAction().getType().equals("take") && g.getState().equals("turn_start")) {
            move = createActionAfterTakingCard(g);
        } else { move = createActionbeforeTakingCard(g);}
        return move.toJSON();//return what to do during the game
    }

    /**
     * return a single card from player
     * @param p
     * @return
     */
    public List<Card> getCard(Player p) {
        List<Card> oneCard = new ArrayList<>();
        oneCard.add(p.getCards().get(0));
        return oneCard;
    }
    public List<Card> getSet(Card topCard, Player p) {
        NumberCard topAsNumber = (NumberCard) topCard;
        List<Card> set = new ArrayList<Card>();
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
            if(hasColor(c, card)) {setOfColor.add(card);}
            NumberCard numberCard = (NumberCard) topCard;
            if (setOfColor.size() == numberCard.getValue()) {break;}
        }
        return setOfColor;
    }

    /**
     * checks if a card has a given color (or more)
     * @param colorValue
     * @param c
     * @return
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
     * when a card is already take, the player has to choose between dircarding or taking further cards
     * @param g
     * @return
     */
    public Action createActionAfterTakingCard(Game g) {
        System.out.println("after "+g.getDiscardPile().size());
        Player p = g.getCurrentPlayer();
        Action action = null;
        Card frontCard = g.getDiscardPile().get(0);
        if (frontCard.getCardType().equals("invisible")&& g.getDiscardPile().size() > 1) {
            frontCard = g.getDiscardPile().get(1);}//invisible, take the card beneath
        if (frontCard.getCardType().equals("invisible")&& g.getDiscardPile().size() == 1) {
            frontCard = new NumberCard("number", 1, frontCard.getColors(), "one");
        }
        else if (frontCard.getCardType().equals("nominate")) {
            int cardAmount=g.getLastNominateAmount();
            if (cardAmount == 0) {cardAmount = 1;}
            frontCard = new NumberCard("number", cardAmount, frontCard.getColors(), "TEXT");
        }
        if (frontCard.getCardType().equals("number")) {
            if (hasCompleteSet(p, frontCard )) {
                List<Card> discardCards = getSet(frontCard ,p);
                action = createActionIfNominated(g, discardCards);
            }
            else {action = new SayNope("nope","no cards to discard",p);}
        }
        else if (frontCard.getCardType().equals("reset")) {
            ArrayList<Card> discardCards = new ArrayList<Card>();
            discardCards.add(g.getCurrentPlayer().getCards().get(0));
            action = new DiscardCard("discard","had to discard :(",1,discardCards,g.getCurrentPlayer());
        }
        return action;
    }

    /**
     * decide about move before taking first card
     * @param g
     * @return
     */

    public Action createActionbeforeTakingCard(Game g) {
        System.out.println("before "+g.getDiscardPile().size());
        Action move = null;
        Card frontCard = g.getDiscardPile().get(0);
        if (frontCard.getCardType().equals("invisible")&& g.getDiscardPile().size() > 1) {
            frontCard = g.getDiscardPile().get(1);}//invisible, take the card beneath
        else if (frontCard.getCardType().equals("invisible")&& g.getDiscardPile().size() == 1) {
            frontCard = new NumberCard("number", 1, frontCard.getColors(), "one");
        }
        if (frontCard.getCardType().equals("nominate")) {
            int cardAmount=g.getLastNominateAmount();
            if (cardAmount == 0) {cardAmount = 1;}
            frontCard = new NumberCard("number", cardAmount, frontCard.getColors(), "Text");
        }
        else if (frontCard.getCardType().equals("reset")) {
            ArrayList<Card> discardCards = new ArrayList<Card>();
            discardCards.add(g.getCurrentPlayer().getCards().get(0));
            move = new DiscardCard("discard","had to discard :(",1,discardCards,g.getCurrentPlayer());
        }

        if (frontCard.getCardType().equals("number")) {
            if (hasCompleteSet(g.getCurrentPlayer(),frontCard)) {
                List<Card> discardCards = getSet(frontCard,g.getCurrentPlayer());
                move = createActionIfNominated(g, discardCards);
            } else {
                move = new TakeCard("take","no cards to discard",1,new ArrayList<>(),g.getCurrentPlayer());
            }
        }
        return move;
    }

    /**
     * if the player is nominated, it has to decide about how to react
     * @param g
     * @param oneCardDiscard
     * @return
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
     * @return
     */
    public boolean hasCompleteSet(Player p, Card c) {
        NumberCard numberCard = (NumberCard) c;
        List<String> colors = c.getColors();
        boolean erg = false;
        /*iterate through all colors of the card */
        for (int i = 0; i < colors.size() && !erg; i++) {
            erg = hasSetWithColor(numberCard,colors.get(i), p);
        }
        return erg;
    }

    /**
     * check if a player has enough cards of a given color
     * @param topCard
     * @param c
     * @param p
     * @return
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
}