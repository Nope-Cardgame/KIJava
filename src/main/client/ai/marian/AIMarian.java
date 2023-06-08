package ai.marian;

import ai.IArtificialIntelligence;
import gameobjects.Game;
import gameobjects.Player;
import gameobjects.actions.*;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.util.ArrayList;
import java.util.List;

public class AIMarian implements IArtificialIntelligence {

    /**
     *
     * @param game the current state of the whole game
     * @return jsonstring to answer the request
     */
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
                String nominateColor = getBestNominateColor(g);
                int nominateValue = getBestNominateValue(g, nominateColor);
                action = new NominateCard("nominate", "chooses amount in state nominate_flipped", 0, new ArrayList<>(), g.getCurrentPlayer(), getOtherPlayer(g), nominateColor, nominateValue);
            } else {
                int nominateValue = getBestNominateValue(g, g.getDiscardPile().get(0).getColors().get(0));
                action = new NominateCard("nominate", "chooses amount in state nominate_flipped", 0, new ArrayList<>(), g.getCurrentPlayer(), getOtherPlayer(g), nominateValue);
            }
            return action.toJSON();
        } else if((g.getDiscardPile().get(0).getCardType().equals("reset") || g.getDiscardPile().get(0).getCardType().equals("invisible")) && g.getDiscardPile().size() == 1){
            List<Card> set = addPossibleCardsToOneList(g, new NumberCard("number", 1, g.getDiscardPile().get(0).getColors(), ""));
            if(set.size() > 0) {
                if(!set.get(0).getCardType().equals("nominate")) {
                    return new DiscardCard("discard", "had to discard because invisible card", 1, set, g.getCurrentPlayer()).toJSON();
                } else {
                    if(set.get(0).getColors().size() == 4) {
                        String nominateColor = getBestNominateColor(g);
                        int nominateValue = getBestNominateValue(g, nominateColor);
                        return new NominateCard("nominate", "plays the nominate card", 1, List.of(set.get(0)), g.getCurrentPlayer(), getOtherPlayer(g), nominateColor, nominateValue).toJSON();
                    } else {
                        int nominateValue = getBestNominateValue(g, g.getDiscardPile().get(0).getColors().get(0));
                        return new NominateCard("nominate", "plays the nominate card", 1, List.of(set.get(0)), g.getCurrentPlayer(), getOtherPlayer(g), nominateValue).toJSON();
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

        List<Card> chosenCards = addPossibleCardsToOneList(g, new NumberCard("number", topCardValue, topCardColor, ""));

        return playCards(g, chosenCards, beforeDrawing);
    }

    /**
     * adds all possible sets/cards to an arraylist<list<Card>>
     *
     * @param game
     * @param topCard
     * @return List of cards
     */
    public List<Card> addPossibleCardsToOneList(Game game, Card topCard){

        ArrayList<List<Card>> actionsCards = getActionCards(game, topCard);
        ArrayList<List<Card>> setsColor1 = getSetsOfColor(game, topCard, true);
        ArrayList<List<Card>> setsColor2 = getSetsOfColor(game, topCard, false);

        ArrayList<List<Card>> allSets = new ArrayList<>(decideActionOrNumberCards(actionsCards, setsColor1, setsColor2, topCard));

        return selectCardsWithMostTwoColoredCards(allSets);
    }

    /**
     * STRATEGY
     * if amount of cards to discard is 1: plays numbercard if possible, else plays actioncard
     * if amount of cards to discard is 2 or 3: plays actioncard if possible, else plays numbercard
     * strategy to save actionscards; plays actioncards only if the discardamount is higher than one to safe one or two cards
     *
     * @param possibleActioncards
     * @param possibleSetsColor1
     * @param possibleSetsColor2
     * @param topCard
     * @return possibleDecisions
     */
    public ArrayList<List<Card>> decideActionOrNumberCards(ArrayList<List<Card>> possibleActioncards, ArrayList<List<Card>> possibleSetsColor1, ArrayList<List<Card>> possibleSetsColor2, Card topCard){

        ArrayList<List<Card>> possibleDecisions = new ArrayList<>();

        if(((NumberCard)topCard).getValue() == 1){ // preferes to play a numbercard instead of an actioncard when amount of discarded cards is 1
            if(possibleSetsColor1.size() != 0){
                possibleDecisions.addAll(possibleSetsColor1);
            }
            if(possibleSetsColor2.size() != 0){
                possibleDecisions.addAll(possibleSetsColor2);
            }

            // only wants to play actioncards if no numbercards are possible to play
            if(possibleDecisions.size() == 0 && possibleActioncards.size() != 0){
                possibleDecisions.addAll(possibleActioncards);
            }
        } else { // if value of discarded cards is higher than 1; preferes to play actioncard
            if(possibleActioncards.size() != 0){
                possibleDecisions.addAll(possibleActioncards);
            }

            // only wants to play numbercards if no actioncards are possible to play
            if((possibleDecisions.size() == 0 && (possibleSetsColor1.size() != 0 || possibleSetsColor2.size() != 0))){
                if(possibleSetsColor1.size() != 0){
                    possibleDecisions.addAll(possibleSetsColor1);
                }

                if(possibleSetsColor2.size() != 0){
                    possibleDecisions.addAll(possibleSetsColor2);
                }
            }
        }

        // size = 0 when no possible decisions are possible (draw or say nope)
        return possibleDecisions;
    }

    /**
     * STRATEGY
     * if actioncard is in first place
     * return action
     * if numbercards are in first place
     * search for the set with the highest amount of two colored cards
     *
     * @param possibleCards
     * @return set
     */
    public List<Card> selectCardsWithMostTwoColoredCards(ArrayList<List<Card>> possibleCards){

        if(possibleCards.size() != 0){
            //possible cards to play found
            Card firstCard = possibleCards.get(0).get(0);
            if(firstCard.getCardType().equals("reset") || firstCard.getCardType().equals("invisible") || firstCard.getCardType().equals("nominate")){
                return possibleCards.get(0);
            } else {
                int wantedTwoColoredAmount = 3; // value of two colored numbercards wanted
                while(true) {
                    for (List<Card> set : possibleCards) {
                        int twoColoredCardsCounter = 0;
                        for (Card card : set) {
                            if (card.getColors().size() == wantedTwoColoredAmount) {
                                twoColoredCardsCounter++;
                            }
                        }
                        if (twoColoredCardsCounter == wantedTwoColoredAmount) {
                            return set;
                        }
                    }
                    if(wantedTwoColoredAmount > 0) {
                        wantedTwoColoredAmount--; // reduces the value of wantedToColoredAmount by 1 if no set was found
                    } else {
                        // no sets found
                        break;
                    }
                }
            }
        }

        // no possible cards to play -> draw or say nope
        return new ArrayList<>();
    }

    /**
     * STRATEGY
     * determines the player with the most cards on hand
     *
     * @param game
     * @return player
     */
    public Player getPlayerWithMostCards(Game game){
        List<Player> playerList = game.getPlayers();

        int highestCardOnHandValue = 0;

        for(Player player: playerList){
            if(!player.equals(game.getCurrentPlayer()) && player.getCards().size() > highestCardOnHandValue){
                highestCardOnHandValue = player.getCards().size();
            }
        }

        for(Player player: playerList){
            if(!player.equals(game.getCurrentPlayer()) && player.getCards().size() == highestCardOnHandValue){
                return player;
            }
        }

        return null;
    }

    /**
     * STRATEGY
     * determines the max. nominate value, that doesnt give you problems, if the enemy cant lay a card on it
     *
     * @param game
     * @param nominatedColor
     * @return value to nominate
     */
    public int getBestNominateValue(Game game, String nominatedColor){

        int amountColor = getAmountOfCardsWithColor(game, nominatedColor);

        if(amountColor >= 5){
            return 3;
        } else if(amountColor == 4){
            return 2;
        } else if(amountColor == 3){
            return 1;
        }

        return 1;
    }

    /**
     * STRATEGY
     * determines the lowest amount of cards of one of the four colors
     *
     * @return color to nominate
     */
    public String getBestNominateColor(Game game){

        int amountRed = getAmountOfCardsWithColor(game, "red");
        int amountBlue = getAmountOfCardsWithColor(game, "blue");
        int amountYellow = getAmountOfCardsWithColor(game, "yellow");
        int amountGreen = getAmountOfCardsWithColor(game, "green");

        int tempHighestAmountOfColor = 999;

        List<Integer> amounts = new ArrayList<>();
        amounts.add(amountRed);
        amounts.add(amountBlue);
        amounts.add(amountYellow);
        amounts.add(amountGreen);

        for(Integer amount: amounts){
            if(amount < tempHighestAmountOfColor){
                tempHighestAmountOfColor = amount;
            }
        }

        if(tempHighestAmountOfColor == amountRed){
            return "red";
        } else if(tempHighestAmountOfColor == amountBlue){
            return "blue";
        } else if(tempHighestAmountOfColor == amountYellow){
            return "yellow";
        } else if(tempHighestAmountOfColor == amountGreen){
            return "green";
        }

        return "red";
    }

    /**
     * plays the selected card(s) through creating an object of the needed action
     *
     * @param game
     * @param chosenCards
     * @param beforeDrawing
     * @return action
     */
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
                if(chosenCards.get(0).getColors().size() == 4){
                    String nominateColor = getBestNominateColor(game);
                    int nominateValue = getBestNominateValue(game, nominateColor);
                    action = new NominateCard("nominate", "play nominate", 1, List.of(chosenCards.get(0)), game.getCurrentPlayer(), getPlayerWithMostCards(game), nominateColor, nominateValue);
                }
                if(chosenCards.get(0).getColors().size() == 1){
                    int nominateValue = getBestNominateValue(game, chosenCards.get(0).getColors().get(0));
                    action = new NominateCard("nominate", "play nominate", 1, List.of(chosenCards.get(0)), game.getCurrentPlayer(), getPlayerWithMostCards(game), nominateValue);
                }
            } else {
                action = new DiscardCard("discard", "had to discard", chosenCards.size(), chosenCards, game.getCurrentPlayer());
            }
        }

        return action;
    }

    /**
     * determines the amount of cards to discard depending on the card on top of the discardpile
     *
     * @param game
     * @param actTopCard
     * @param topCardIndex
     * @return value of the card
     */
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

                if (game.getDiscardPile().get(game.getDiscardPile().size() - 1).getCardType().equals("invisible") && game.getDiscardPile().size() - 1 == newTopCardIndex){
                    System.out.println("Invisible0");
                    return 1;
                }
            }
        }

        System.out.println("!!!!!");
        return 0;
    }

    /**
     * determines the color(s) of cards to discard depending on the card on top of the discardpile
     *
     * @param game
     * @param actTopCard
     * @param topCardIndex
     * @return color(s) of the card
     */
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

                if (game.getDiscardPile().get(game.getDiscardPile().size()- 1 ).getCardType().equals("invisible")  && game.getDiscardPile().size() - 1 == newTopCardIndex){
                    System.out.println("Invisible0");
                    return game.getDiscardPile().get(game.getDiscardPile().size() - 1).getColors();
                }
            }
        }

        return null;
    }

    /**
     * searches for fitting sets of cards in your hand depending on the top card of the discarding pile
     *
     * @param game
     * @return Arraylist with lists of cards
     */
    public ArrayList<List<Card>> getSetsOfColor(Game game, Card actTopCard, boolean matchFirstColor){

        int actTopCardValue; // value of the card on the discard pile

        ArrayList<List<Card>> foundCards = new ArrayList<>(); // needed for return

        actTopCardValue = ((NumberCard) actTopCard).getValue();

        // searches for sets fitting to the color of the top card and the value 1

        if(actTopCardValue == 1){ // if value of top card is 1
            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if(matchFirstColor) {
                        if (matchColorOfCard(actTopCard, card, true)) {
                            foundCards.add(List.of(card));
                        }
                    } else {
                        if (matchColorOfCard(actTopCard, card, false)) {
                            foundCards.add(List.of(card));
                        }
                    }
                }
            }
        }

        // searches for sets fitting to the color of the top card and the value 2

        if(actTopCardValue == 2){ // if value of top card is 2

            // List for the (possible) first color of the card for devision
            List<Card> cardsOfColor = new ArrayList<>();

            // puts cards fitting to the first color of the top card to the list cardsOfFirstColor

            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if(matchFirstColor) {
                        if (matchColorOfCard(actTopCard, card, true)) {
                            cardsOfColor.add(card);
                        }
                    } else {
                        if (matchColorOfCard(actTopCard, card, false)) {
                            cardsOfColor.add(card);
                        }
                    }
                }
            }

            // creates possible sets with that color and adds them to the output arraylist

            if(cardsOfColor.size() == 2){
                foundCards.add(cardsOfColor);
            } else if(cardsOfColor.size() > 2){
                for (int firstCardcounter = 0; firstCardcounter < cardsOfColor.size() - 2; firstCardcounter++) {
                    for (int secondCardCounter = firstCardcounter + 1; secondCardCounter < cardsOfColor.size() - 1; secondCardCounter++) {
                        foundCards.add(List.of(cardsOfColor.get(firstCardcounter), cardsOfColor.get(secondCardCounter)));
                    }
                }
            }
        }

        // searches for sets fitting to the color of the top card and the value 3

        if(actTopCardValue == 3){ // if value of top card is 3

            // List for the (possible) second colors of the card for devision
            List<Card> cardsOfColor = new ArrayList<>();

            // puts cards fitting to the first color of the top card to the list cardsOfFirstColor
            for(Card card: game.getCurrentPlayer().getCards()){
                if(card.getCardType().equals("number")) {
                    if(matchFirstColor) {
                        if (matchColorOfCard(actTopCard, card, true)) {
                            cardsOfColor.add(card);
                        }
                    } else {
                        if (matchColorOfCard(actTopCard, card, false)) {
                            cardsOfColor.add(card);
                        }
                    }
                }
            }

            // creates possible sets with that color and adds them to the output arraylist

            if(cardsOfColor.size() == 3){
                foundCards.add(cardsOfColor);
            } else if(cardsOfColor.size() > 3) {
                for (int firstCardcounter = 0; firstCardcounter < cardsOfColor.size() - 3; firstCardcounter++) {
                    for (int secondCardCounter = firstCardcounter + 1; secondCardCounter < cardsOfColor.size() - 2; secondCardCounter++) {
                        for (int thirdCardCounter = firstCardcounter + 2; thirdCardCounter < cardsOfColor.size() - 1; thirdCardCounter++) {
                            foundCards.add(List.of(cardsOfColor.get(firstCardcounter), cardsOfColor.get(secondCardCounter), cardsOfColor.get(thirdCardCounter)));
                        }
                    }
                }
            }
        }

        return foundCards;
    }

    /**
     * creates an arraylist with the playable actioncards in this situation
     *
     * @param game
     * @param actTopCard
     * @return foundActionCards
     */
    public ArrayList<List<Card>> getActionCards(Game game, Card actTopCard){
        ArrayList<List<Card>> foundActionCards = new ArrayList<>();
        for (Card card : game.getCurrentPlayer().getCards()) {
            if (!card.getCardType().equals("number") && (matchColorOfCard(actTopCard, card, true) || matchColorOfCard(actTopCard, card, false))) {
                foundActionCards.add(List.of(card));
            }
        }

        if (foundActionCards.size() != 0) {
            return foundActionCards;
        }
        return new ArrayList<>();
    }

    public boolean matchColorOfCard(Card card1, Card card2, boolean matchFirstColor){

        // for cards with one or two colors
        if(matchFirstColor){
            if(card1.getColors().size() <= 2) {
                String cardColor1 = card1.getColors().get(0);
                List<String> cardColors2 = card2.getColors();

                for (String colors : cardColors2) { // compares the first color of the first card with the colors of the second card
                    if (colors.equals(cardColor1)) return true;
                }
            }
        } else {
            // for cards with two colors
            if(card1.getColors().size() > 1) {
                String cardColor1 = card1.getColors().get(1);
                List<String> cardColors2 = card2.getColors();

                for(String colors: cardColors2){ // compares the second color of the first card with the colors of the second card
                    if(colors.equals(cardColor1)) return true;
                }
            }
        }

        // for wildcards
        if(card1.getColors().size() == 4 || card2.getColors().size() == 4) {
            for (String colors1 : card1.getColors()) {
                for (String colors2 : card2.getColors()) {
                    if (colors1.equals(colors2)) return true;
                }
            }
        }

        return false;
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

    /**
     * determines another player in the game (except yourself)
     *
     * @param game
     * @return player
     */
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