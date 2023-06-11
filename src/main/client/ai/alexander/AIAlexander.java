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
import java.util.Collections;
import java.util.Comparator;

public class AIAlexander implements IArtificialIntelligence {
    ArrayList<Card> cards = new ArrayList<>();
     Player player = new Player("Player1","", 8,  cards,0, false);
    public String calculateNextMove(Game game) {//at first sort the cards by amount of colors
        Collections.sort(game.getCurrentPlayer().getCards(), Comparator.comparingInt(karte -> (5-karte.getColors().size())));
        if (game.getState().equals("nominate_flipped")){
            return nominateflipped(game).toJSON();
        }
        else if (game.getLastAction().getType().equals("take") && !game.getState().equals("turn_start")) {
            return createActionAfterTakingCard(game).toJSON(); //the player has already token a card
        }
        else {return createActionbeforeTakingCard(game).toJSON();} //the player has not taken a card yet
    }

    /**
     *
     * @param topCard usually the first card from discardpile, except of invisible or nominated state
     * @param player the current player
     * @return
     */
    public List<Card> getDiscardSet(Card topCard, Player player) {
        NumberCard upperCard = (NumberCard) topCard;
        List<Card> cardSet = new ArrayList<>(); //empty list of cards  to fill
        for(int i = 0; i < topCard.getColors().size() && cardSet.size() != upperCard.getValue(); i++) {
            cardSet = createCardSetFromHand(topCard, topCard.getColors().get(i), player);
        }
        return cardSet;
    }

    /**
     * create a list of cards from player concerning to topcard
     * @param topCard the card, which determines the players action
     * @param c as color
     * @param p the current player
     * @return
     */
    public List<Card> createCardSetFromHand(Card topCard, String c, Player p) {
        List<Card> setOfColor = new ArrayList<>();
        for (Card card : p.getCards()) {
            if (isColorInCard(c, card)) {setOfColor.add(card);} // add if color ok
            NumberCard numberCard = (NumberCard) topCard;// topcard cannot be actioncard, because action cards already handeled
            if (setOfColor.size() == numberCard.getValue()) {break;} //break if enough cards
        }
        return setOfColor;
    }

    /**
     * checks if a card has a given color (or more)
     * @param colorValue can be red, blue, green, yellow
     * @param c Card, in which is color is or not is
     * @return boolean, if card has this color
     */
    public boolean isColorInCard (String colorValue, Card c) {
        boolean found = false;
        for (String color: c.getColors()) {
            if (color.equals(colorValue)) { // check, if color is in list of colorlist in card
                found = true;
                break; // no need to go further, if color found
            }
        }
        return found;
    }

    /**
     * if the first card in discard pile  is invisible or nominate,
     * the algorithm can calculate with a number card based on the first card
     * @param game current game
     * @return frontcard, which determines the players action
     */
    public Card getFrontCard(Game game){
        Card frontCard = game.getDiscardPile().get(0);
        if (frontCard.getCardType().equals("invisible")) {
            frontCard = ifInvisibleOnHand(game); //if frontcard is invisible, search for the card below
        }//this case, a nominated card with one color requiers an action, therefore calculation of new numbercard
        if (frontCard.getCardType().equals("nominate") && frontCard.getColors().size()==1) {
            frontCard = new NumberCard("number", game.getLastNominateAmount(), frontCard.getColors(), "nominated above");
        }//this case, nominated with 4 colors requieres to be handeled as numbercard
        else if (frontCard.getCardType().equals("nominate") && frontCard.getColors().size()==4) {
            frontCard = new NumberCard("number", game.getLastNominateAmount(), List.of(game.getLastNominateColor()), "nominated above");
        }
        return frontCard;
    }

    /**
     * if player has reset card, the player can discard either action or number card
     * @param game current game state
     * @param frontCard the card, that determines the players action
     * @return
     */
    public Action handleResetCard(Game game, Card frontCard){
        if (discardActionCard(game, frontCard, game.getCurrentPlayer()) != null) {
            return discardActionCard(game, frontCard, game.getCurrentPlayer());
        }
        else {
            return new DiscardCard("discard", "had to discard :(", 1,
                    List.of(game.getCurrentPlayer().getCards().get(0)), game.getCurrentPlayer());
        }
    }
    /**
     * when a card is already take, the player has to choose between dircarding or taking further cards
     * @param game = current game state with all information
     * @return action, what the player does after card is already token
     */
    public Action createActionAfterTakingCard(Game game) {
        Player p = game.getCurrentPlayer();
        Card frontCard = getFrontCard(game); //get the card which decides about the move
        //numbercard requiers discarding 1-3 numbercards or action card
        if (frontCard.getCardType().equals("number")) {
            if (hasCompleteSet(p, frontCard )) { //has complete set, has to discard by rules
                return createActionByNumberCard(game, getDiscardSet(frontCard ,p), frontCard);
            }//case in which  no card has to get discarded
            else {return new SayNope("nope","no cards to discard",p);}
        }// in case of reset, one card has to get discard, nope not possible
        else  {return handleResetCard(game, frontCard);}//if not number, card is reset
    }

    /**
     * decide about move before taking first card
     * @param game as current game state
     * @return action if no card was already taken
     */
    public Action createActionbeforeTakingCard(Game game) {
        Card frontCard = getFrontCard(game);
         if (frontCard.getCardType().equals("number")) { //discard action card, if available
            if (discardActionCard(game, frontCard, game.getCurrentPlayer()) != null) {
                return discardActionCard(game, frontCard, game.getCurrentPlayer());
            }
            else if (hasCompleteSet(game.getCurrentPlayer(),frontCard)) { //  discard numbercards
                return createActionByNumberCard(game, getDiscardSet(frontCard,game.getCurrentPlayer()), frontCard);
            } else { // take card
                return new TakeCard("take","no cards to discard",1,new ArrayList<>(),game.getCurrentPlayer());
            }
        }
        else {return handleResetCard(game, frontCard);} // reset Card
    }

    /**
     * if the player is nominated, it has to decide about how to react
     * @param g
     * @param oneCardDiscard
     * @return action
     */
    @NotNull
    private Action createActionByNumberCard(Game g, List<Card> oneCardDiscard, Card frontCard) {
        if (discardActionCard(g, frontCard, g.getCurrentPlayer()) != null) {
            return discardActionCard(g, frontCard, g.getCurrentPlayer());//try to play an action card if available
        }
        else { // if no action card available, discard a set of number cards
            return new DiscardCard("discard", "had to discard cards", oneCardDiscard.size(), oneCardDiscard, g.getCurrentPlayer());
        }
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
        boolean erg = false; //iterate through all colors of the card and search for set
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
    public boolean hasSetWithColor(NumberCard topCard, String c, Player p) {
        int count = 0; //count of cards
        for(Card card: p.getCards()) {
            if (isColorInCard(c, card)) {count +=1;} // count carts of given color
        }
        return count >= (topCard).getValue();
    }

    /**
     * If any action card is available, the players discards this card immediately
     * @param g as current game
     * @param frontcard as the calculated card on which the player has to react
     * @param player current player
     * @return
     */
    public Action discardActionCard(Game g, Card frontcard, Player player){
        Action action = null;
        for (Card card: player.getCards()){
            if (card.getCardType().equals("nominate") && cardWithSameColor(frontcard, card)){
               if (card.getColors().size()==4){
                   action = new NominateCard("nominate","had to nominate",1, List.of(card),player, getOpponent(g) , chooseNominateColor(card,player), getNomimationInt(g));
               }
               else {
                   action = new NominateCard("nominate","had to nominate",1,List.of(card),player, getOpponent(g) , getNomimationInt(g));
               }
               break;
            }
            if (card.getCardType().equals("invisible") && cardWithSameColor(frontcard, card)){
                return new DiscardCard("discard", "invisible", 1, List.of(card), player);
            }
            if (card.getCardType().equals("reset") && cardWithSameColor(frontcard, card)){
                return new DiscardCard("discard", "reset", 1, List.of(card), player);
            }

        }
        return action;
    }

    /**
     * compare two card and return true, if one color in both is the same
     * @param a as card
     * @param b as card
     * @return
     */
    public boolean cardWithSameColor(Card a, Card b){
        for (String colorA: a.getColors()){
            for (String colorB: b.getColors()){
                if (colorA.equals(colorB)) {return true;}
            }
        }
        return false;
    }

    /**
     * is game state nominated flipped, te
     * @param game
     * @return
     */
    public Action nominateflipped(Game game){
        if (game.getDiscardPile().get(0).getColors().size() == 4){
            return new NominateCard("nominate", "flipped", 0,
                new ArrayList<Card>(), game.getPlayers().get(game.getPlayers().size()-1),
                game.getCurrentPlayer(), game.getDiscardPile().get(0).getColors().get(0), 1);
    }
       else  {
            return new NominateCard("nominate","flipped",0, new ArrayList<Card>(),
                    game.getPlayers().get(game.getPlayers().size()-1), game.getCurrentPlayer(),1);
        }
    }

    /**
     * maximal 4 invisible cards can be together on discardpile, if multiple color card below
     * @param g as current game
     * @return the first not-invisible card below invisible card
     */
   public Card ifInvisibleOnHand(Game g) {
        if (g.getDiscardPile().size() == 1) { // only one card in stack
            return new NumberCard("number", 1, g.getDiscardPile().get(0).getColors(), "one");
        } else {// more than one single card in discardpile
            int index = 0;
            while (g.getDiscardPile().get(index).getCardType().equals("invisible")){index++;}
            return g.getDiscardPile().get(index);  // return the last not-visible card
        }
    }

    /**
     * choose the best color to nominate, which is the color the player has at least
     * @param card usually the multi color nominate card
     * @param p the current player
     * @return
     */
    public String chooseNominateColor(Card card, Player p){
        int[] i = new int[4];//save the color amounts in this array
        for (int index = 0; index < 4; index++) {i[index] = countCardsOfColor(p, card.getColors().get(index));}
        if (i[0] < i[1] && i[0] < i[2] && i[0] < i[3]) {return card.getColors().get(0);}
        else if (i[1] < i[0] && i[1] < i[2] && i[1] < i[3]) {return card.getColors().get(1);}
        else if (i[2] < i[1] && i[2] < i[0] && i[2] < i[3]) {return card.getColors().get(2);}
        else {return card.getColors().get(3);}
    }

    /**
     * count how many cards of given color has a player
     * @param p the current player
     * @param color the choosen color
     * @return
     */
   public  int countCardsOfColor(Player p, String color){
        int i = 0;
        for (Card card: p.getCards()){
            if (card.getColors().contains(color)){i++;} //count the cards
        }
        return i;
    }

    /**
     * find the opponent, which should be nominated
     * @param g as current game
     * @return the player which should be nominated
     */
    public Player getOpponent(Game g) {
        for (Player p : g.getPlayers()) { //find opponent, which has another username and should not be disqualified
            if (!p.equals(g.getCurrentPlayer()) && !p.isDisqualified()) {return p;}
        }
        return g.getPlayers().get(0);//else return
    }

    /**
     * return the best interger for nominated
     * @param g as current came
     * @return the best amount for nomination
     */
    public int getNomimationInt(Game g) {
        int v = 3; // usually ask for three cards
        if (getOpponent(g).getCardAmount()<5) { v = 1;} //if the player has less than 5 cards, ask for two card
        if (getOpponent(g).getCardAmount()<3) { v = 1;} //if the player has less than 3 cards, ask for 1 card
        return v;
    }
}