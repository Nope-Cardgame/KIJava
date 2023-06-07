package ai.marian;

import ai.IArtificialIntelligence;
import gameobjects.Game;
import gameobjects.Player;
import gameobjects.actions.*;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.util.ArrayList;
import java.util.List;

// TODO 1. Fehler INVISIBLE-Card und NumberCARD
// TODO 2. Fehler NOMINATE und Value 0

public class AIMarian implements IArtificialIntelligence {
    public String calculateNextMove(Game game) {
        return decide(game);
    }

    /**
     * these method decides how to react on a game state by calculating the cards
     *
     * @param g
     * @return move.toJSON()
     */
    public String decide(Game g) {

        if(g.getState().equals("nominate_flipped")){
            Action action;
            if(g.getDiscardPile().get(0).getColors().size() == 4) {
                action = new NominateCard("nominate", "chooses amount in state nominate_flipped", 0, new ArrayList<>(), g.getCurrentPlayer(), getOtherPlayer(g), g.getDiscardPile().get(0).getColors().get(0), 1);
            } else {
                action = new NominateCard("nominate", "chooses amount in state nominate_flipped", 0, new ArrayList<>(), g.getCurrentPlayer(), getOtherPlayer(g), 1);
            }
            return action.toJSON();
        } else if((g.getDiscardPile().get(0).getCardType().equals("reset") || g.getDiscardPile().get(0).getCardType().equals("invisible"))&& g.getDiscardPile().size() == 1){
            List<Card> set = chooseBestSet(g, new NumberCard("number", 1, g.getDiscardPile().get(0).getColors(), ""));
            if(set.size() > 0) {
                if(!set.get(0).getCardType().equals("nominate")) {
                    return new DiscardCard("discard", "had to discard because invisible card", 1, set, g.getCurrentPlayer()).toJSON();
                } else {
                    if(set.get(0).getColors().size() == 4) {
                        return new NominateCard("nominate", "chooses amount in state nominate_flipped", 0, new ArrayList<>(), g.getCurrentPlayer(), getOtherPlayer(g), g.getDiscardPile().get(0).getColors().get(0), 1).toJSON();
                    } else {
                        return new NominateCard("nominate", "chooses amount in state nominate_flipped", 0, new ArrayList<>(), g.getCurrentPlayer(), getOtherPlayer(g), 1).toJSON();
                    }
                }
            }
        }

        if(g.getLastAction().getType().equals("take") && !g.getState().equals("turn_start")) return doMove(g, false).toJSON();

        return doMove(g, true).toJSON();
    }

    /**
     * decide about move before taking first card
     *
     * @param g
     * @return action
     */
    public Action doMove(Game g, boolean beforeDrawing) {

        int topCardValue = getValueOfCard(g, g.getDiscardPile().get(0), 0);
        List<String> topCardColor = getColorOfCard(g, g.getDiscardPile().get(0), 0);

        List<Card> chosenCards = chooseBestSet(g, new NumberCard("number", topCardValue, topCardColor, ""));

        return playCards(g, chosenCards, beforeDrawing);
    }

    /**
     * chooses the best set to pick in this situation
     *
     * @param game
     * @return List of cards
     */
    public List<Card> chooseBestSet(Game game, Card topCard){

        ArrayList<List<Card>> actionsCards = getActionCards(game, topCard);
        ArrayList<List<Card>> setsColor1 = getSetsOfFirstColor(game, topCard);
        ArrayList<List<Card>> setsColor2 = getSetsOfSecondColor(game, topCard);

        ArrayList<List<Card>> allSets = new ArrayList<>();

        if(actionsCards.size() != 0){
            allSets.addAll(actionsCards);
        }

        if(setsColor1.size() != 0){
            allSets.addAll(setsColor1);
        }

        if(setsColor2.size() != 0){
            allSets.addAll(setsColor2);
        }

        if(allSets.size() != 0){
            return allSets.get(0);
        } else {
            return new ArrayList<>();
        }
    }

    public Action playCards(Game game, List<Card> chosenCards, boolean beforeDrawing){

        Action action = null;

        if(chosenCards.size() == 0) {
            if(beforeDrawing) {
                action = new TakeCard("take", "no cards to discard", 1, new ArrayList<>(), game.getCurrentPlayer());
            } else {
                action = new SayNope("nope", "no cards to discard", game.getCurrentPlayer());
            }
        } else {
            if (chosenCards.get(0).getCardType().equals("nominate")) {
                // TODO immer rot ausgewählt und die Anzahl 1
                // TODO Spieler zufällig gewählt
                if(chosenCards.get(0).getColors().size() == 4) action = new NominateCard("nominate", "play nominate", 1, chosenCards, game.getCurrentPlayer(), getOtherPlayer(game), "red", 1);
                // TODO immer die Anzahl 1 ausgewählt
                // TODO Spieler zufällig gewählt
                if(chosenCards.get(0).getColors().size() == 1) action = new NominateCard("nominate", "play nominate", 1, chosenCards, game.getCurrentPlayer(), getOtherPlayer(game), 1);
            } else {
                action = new DiscardCard("discard", "had to discard", chosenCards.size(), chosenCards, game.getCurrentPlayer());
            }
        }

        return action;
    }

    public int getValueOfCard(Game game, Card actTopCard, int topCardIndex){

        switch (actTopCard.getCardType()) {
            case "number" -> {
                System.out.println("Number");
                return ((NumberCard) actTopCard).getValue();
            }

            case "reset" -> {
                System.out.println("Reset");
                return 1;
            }

            case "nominate" -> {
                System.out.println("Nominate");
                if(game.getLastNominateAmount() != 0) {
                    return game.getLastNominateAmount();
                }
            }

            case "invisible" -> {
                System.out.println("Invisible");
                int newTopCardIndex = topCardIndex + 1;
                if (game.getDiscardPile().size() > newTopCardIndex) {
                    return getValueOfCard(game, game.getDiscardPile().get(newTopCardIndex), newTopCardIndex);
                }

                // rare case that only invisible cards are on the discard pile
                if (game.getDiscardPile().get(game.getDiscardPile().size() - 1).getCardType().equals("invisible") && game.getDiscardPile().size() - 1 == newTopCardIndex){
                    System.out.println("Invisible0");
                    return 1;
                }
            }
        }

        System.out.println("!!!!!");
        return 0;
    }

    public List<String> getColorOfCard(Game game, Card actTopCard, int topCardIndex){

        switch (actTopCard.getCardType()) {
            case "number", "reset" -> {
                System.out.println("Number/Reset");
                return actTopCard.getColors();
            }
            case "nominate" -> {
                System.out.println("Nominate");
                if(actTopCard.getColors().size() == 4) {
                    return List.of(game.getLastNominateColor());
                } else {
                    return actTopCard.getColors();
                }
            }
            case "invisible" -> {
                System.out.println("Invisible");
                int newTopCardIndex = topCardIndex + 1;
                if (game.getDiscardPile().size() > newTopCardIndex) {
                    return getColorOfCard(game, game.getDiscardPile().get(newTopCardIndex), newTopCardIndex);
                }

                // rare case that only invisible cards are on the discard pile
                if (game.getDiscardPile().get(game.getDiscardPile().size()- 1 ).getCardType().equals("invisible")  && game.getDiscardPile().size() - 1 == newTopCardIndex){
                    System.out.println("Invisible0");
                    return game.getDiscardPile().get(game.getDiscardPile().size() - 1).getColors();
                }
            }
        }

        System.out.println("!!!!!");
        return null;
    }

    /**
     * searches for fitting sets of cards in your hand depending on the top card of the discarding pile
     *
     * @param game
     * @return Arraylist with lists of cards
     */
    public ArrayList<List<Card>> getSetsOfFirstColor(Game game, Card actTopCard){

        int actTopCardValue; // value of the card on the discard pile

        ArrayList<List<Card>> foundCards = new ArrayList<>(); // needed for return

        actTopCardValue = ((NumberCard) actTopCard).getValue();

        // searches for sets fitting to the color of the top card and the value 1

        if(actTopCardValue == 1){ // if value of top card is 1
            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if (matchFirstNumberCardColor(actTopCard, card)) {
                        foundCards.add(List.of(card));
                    }
                }
            }
        }

        // searches for sets fitting to the color of the top card and the value 2

        if(actTopCardValue == 2){ // if value of top card is 2

            // List for the (possible) first color of the card for devision
            List<Card> cardsOfFirstColor = new ArrayList<>();

            // puts cards fitting to the first color of the top card to the list cardsOfFirstColor

            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if (matchFirstNumberCardColor(actTopCard, card)) {
                        cardsOfFirstColor.add(card);
                    }
                }
            }

            // creates possible sets with that color and adds them to the output arraylist

            if(cardsOfFirstColor.size() == 2){
                foundCards.add(cardsOfFirstColor);
            } else if(cardsOfFirstColor.size() > 2){
                for (int firstCardcounter = 0; firstCardcounter < cardsOfFirstColor.size() - 2; firstCardcounter++) {
                    for (int secondCardCounter = firstCardcounter + 1; secondCardCounter < cardsOfFirstColor.size() - 1; secondCardCounter++) {
                        foundCards.add(List.of(cardsOfFirstColor.get(firstCardcounter), cardsOfFirstColor.get(secondCardCounter)));
                    }
                }
            }
        }

        // searches for sets fitting to the color of the top card and the value 3

        if(actTopCardValue == 3){ // if value of top card is 3

            // List for the (possible) second colors of the card for devision
            List<Card> cardsOfFirstColor = new ArrayList<>();

            // puts cards fitting to the first color of the top card to the list cardsOfFirstColor
            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if (matchFirstNumberCardColor(actTopCard, card)) {
                        cardsOfFirstColor.add(card);
                    }
                }
            }

            // creates possible sets with that color and adds them to the output arraylist

            if(cardsOfFirstColor.size() == 3){
                foundCards.add(cardsOfFirstColor);
            } else if(cardsOfFirstColor.size() > 3) {
                for (int firstCardcounter = 0; firstCardcounter < cardsOfFirstColor.size() - 3; firstCardcounter++) {
                    for (int secondCardCounter = firstCardcounter + 1; secondCardCounter < cardsOfFirstColor.size() - 2; secondCardCounter++) {
                        for (int thirdCardCounter = firstCardcounter + 2; thirdCardCounter < cardsOfFirstColor.size() - 1; thirdCardCounter++) {
                            foundCards.add(List.of(cardsOfFirstColor.get(firstCardcounter), cardsOfFirstColor.get(secondCardCounter), cardsOfFirstColor.get(thirdCardCounter)));
                        }
                    }
                }
            }
        }

        return foundCards;
    }

    /**
     * searches for fitting sets of cards in your hand depending on the top card of the discarding pile
     *
     * @param game
     * @return Arraylist with lists of cards
     */
    public ArrayList<List<Card>> getSetsOfSecondColor(Game game, Card actTopCard){

        int actTopCardValue; // value of the card on the discard pile

        ArrayList<List<Card>> foundCards = new ArrayList<>(); // needed for return

        actTopCardValue = ((NumberCard) actTopCard).getValue();

        System.out.println(actTopCardValue);
        System.out.println(actTopCard.getColors());

        // searches for sets fitting to the color of the top card and the value 1

        if(actTopCardValue == 1){ // if value of top card is 1
            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if (matchSecondNumberCardColor(actTopCard, card)) {
                        foundCards.add(List.of(card));
                    }
                }
            }
        }

        // searches for sets fitting to the color of the top card and the value 2

        if(actTopCardValue == 2){ // if value of top card is 2

            // List for the (possible) first color of the card for devision
            List<Card> cardsOfSecondColor = new ArrayList<>();

            // puts cards fitting to the second color of the top card to the list cardsOfFirstColor

            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if (matchSecondNumberCardColor(actTopCard, card)) {
                        cardsOfSecondColor.add(card);
                    }
                }
            }

            // creates possible sets with that color and adds them to the output arraylist

            if(cardsOfSecondColor.size() == 2){
                foundCards.add(cardsOfSecondColor);
            } else if(cardsOfSecondColor.size() > 2) {
                for (int firstCardcounter = 0; firstCardcounter < cardsOfSecondColor.size() - 2; firstCardcounter++) {
                    for (int secondCardCounter = firstCardcounter + 1; secondCardCounter < cardsOfSecondColor.size() - 1; secondCardCounter++) {
                        foundCards.add(List.of(cardsOfSecondColor.get(firstCardcounter), cardsOfSecondColor.get(secondCardCounter)));
                    }
                }
            }
        }

        // searches for sets fitting to the color of the top card and the value 3

        if(actTopCardValue == 3){ // if value of top card is 3

            // List for the (possible) second color of the card for devision
            List<Card> cardsOfSecondColor = new ArrayList<>();

            // puts cards fitting to the second color of the top card to the list cardsOfFirstColor

            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if (matchSecondNumberCardColor(actTopCard, card)) {
                        cardsOfSecondColor.add(card);
                    }
                }
            }

            // creates possible sets with that color and adds them to the output arraylist

            if(cardsOfSecondColor.size() == 3){
                foundCards.add(cardsOfSecondColor);
            } else if(cardsOfSecondColor.size() > 3) {
                for (int firstCardcounter = 0; firstCardcounter < cardsOfSecondColor.size() - 3; firstCardcounter++) {
                    for (int secondCardCounter = firstCardcounter + 1; secondCardCounter < cardsOfSecondColor.size() - 2; secondCardCounter++) {
                        for (int thirdCardCounter = firstCardcounter + 2; thirdCardCounter < cardsOfSecondColor.size() - 1; thirdCardCounter++) {
                            foundCards.add(List.of(cardsOfSecondColor.get(firstCardcounter), cardsOfSecondColor.get(secondCardCounter), cardsOfSecondColor.get(thirdCardCounter)));
                        }
                    }
                }
            }
        }

        return foundCards;
    }

    public ArrayList<List<Card>> getActionCards(Game game, Card actTopCard){
        ArrayList<List<Card>> foundActionCards = new ArrayList<>();
        for (Card card : game.getCurrentPlayer().getCards()) {
            if (!card.getCardType().equals("number") && (matchFirstNumberCardColor(actTopCard, card) || matchSecondNumberCardColor(actTopCard, card))) {
                foundActionCards.add(List.of(card));
            }
        }

        if (foundActionCards.size() != 0) {
            return foundActionCards;
        }
        return new ArrayList<>();
    }

    /**
     * compares the first color of the card card1 to the first and second (if given) color of card2
     *
     * @param card1
     * @param card2
     * @return true (if matches), false (if not matches)
     */
    public boolean matchFirstNumberCardColor(Card card1, Card card2){
        boolean match = false;

        // for the first color of the numbercards

        if(card1.getColors() == null || card2.getColors() == null) return false;

        if(card1.getColors().size() <= 2) {
            String cardColor1 = card1.getColors().get(0);
            List<String> cardColors2 = card2.getColors();

            for (String colors : cardColors2) {
                if (colors.equals(cardColor1)) return true;
            }
        } else { // for wildcard
            for(String colors1 : card1.getColors()){
                for(String colors2 : card2.getColors()){
                    if(colors1.equals(colors2)) return true;
                }
            }
        }

        return match;
    }

    /**
     * compares the second color (if given) of the card card1 to the first and second (if given) color of card2
     *
     * @param card1
     * @param card2
     * @return true (if matches), false (if not matches)
     */
    public boolean matchSecondNumberCardColor(Card card1, Card card2){
        boolean match = false;

        // for the second color of numbercards

        if(card1.getColors() == null || card2.getColors() == null) return false;

        if(card1.getColors().size() > 1) {
            String cardColor1 = card1.getColors().get(1);
            List<String> cardColors2 = card2.getColors();

            for(String colors: cardColors2){
                if(colors.equals(cardColor1)) return true;
            }
        } else { // for wildcard
            for(String colors1 : card1.getColors()){
                for(String colors2 : card2.getColors()){
                    if(colors1.equals(colors2)) return true;
                }
            }
        }

        return match;
    }

    /**
     * counts the amount of card in the hand of the player of the given color
     *
     * @param game
     * @param color
     * @return colorCounter
     */
    public int getAmountOfCardsWithColor(Game game, String color){
        int colorCounter = 0;
        for(Card card: game.getCurrentPlayer().getCards()){
            if(card.getColors().size() == 2){
                if(card.getColors().get(0).equals(color) || card.getColors().get(1).equals(color)) {
                    colorCounter++;
                }
            } else if(card.getColors().size() == 1){
                if(card.getColors().get(0).equals(color)){
                    colorCounter++;
                }
            }
        }
        return colorCounter;
    }

    public Player getOtherPlayer(Game game){
        List<Player> playerList = game.getPlayers();
        for(Player player: playerList){
            if(!player.equals(game.getCurrentPlayer())){
                return player;
            }
        }
        return null;
    }
}