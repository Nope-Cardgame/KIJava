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
     * these method decides how to react on a game state by calculating the best possible decision
     *
     * @param g
     * @return move.toJSON()
     */
    public String decide(Game g) {

        // if you are the first player in the game and there is an actioncard on top of the discard pile
        if(g.getState().equals("nominate_flipped")){ // top card is nominate card
            Action action;
            if(g.getDiscardPile().get(0).getColors().size() == 4) { // top card has four colors
                String nominateColor = getBestNominateColor(g);
                int nominateValue = getBestNominateValue(g, nominateColor);
                action = new NominateCard("nominate", "chooses amount in state nominate_flipped", 0, new ArrayList<>(), g.getCurrentPlayer(), getOtherPlayer(g), nominateColor, nominateValue);
            } else { // top card has one color
                int nominateValue = getBestNominateValue(g, g.getDiscardPile().get(0).getColors().get(0));
                action = new NominateCard("nominate", "chooses amount in state nominate_flipped", 0, new ArrayList<>(), g.getCurrentPlayer(), getOtherPlayer(g), nominateValue);
            }
            return action.toJSON();
        } else if((g.getDiscardPile().get(0).getCardType().equals("reset") || g.getDiscardPile().get(0).getCardType().equals("invisible")) && g.getDiscardPile().size() == 1){ // if top card is reset or invisible actioncard
            List<Card> set = addPossibleCardsToOneList(g, new NumberCard("number", 1, g.getDiscardPile().get(0).getColors(), ""));
            if(set.size() > 0) {
                if(!set.get(0).getCardType().equals("nominate")) { // if first card of set is not a nominate card
                    return new DiscardCard("discard", "had to discard", 1, set, g.getCurrentPlayer()).toJSON();
                } else { // if first card is nominate card
                    if(set.get(0).getColors().size() == 4) { //
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

        // "normal turn"

        if(g.getLastAction().getType().equals("take") && !g.getState().equals("turn_start")) return doMove(g, false).toJSON(); // action after drawing

        return doMove(g, true).toJSON(); // action before drawing
    }

    /**
     * decide about move before taking first card
     *
     * @param g
     * @return action
     */
    public Action doMove(Game g, boolean beforeDrawing) {

        // creates a numbercard of the current card on top of the discard pile -> easier to handle
        int topCardValue = getValueOfCard(g, g.getDiscardPile().get(0), 0);
        List<String> topCardColor = getColorOfCard(g, g.getDiscardPile().get(0), 0);

        // calculates the best set
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

        // calculates all possible sets/cards
        ArrayList<List<Card>> actionsCards = sortActionCards(game, getActionCards(game, topCard));
        ArrayList<List<Card>> setsColor1 = getSetsOfColor(game, topCard, true);
        ArrayList<List<Card>> setsColor2 = getSetsOfColor(game, topCard, false);

        // searches for the sets of numbercards with the highest amount of two colored cards in it
        ArrayList<List<Card>> twoColoredSetColor1 = selectCardsWithMostTwoColoredCards(setsColor1);
        ArrayList<List<Card>> twoColoredSetColor2 = selectCardsWithMostTwoColoredCards(setsColor2);

        // decides which color is better
        ArrayList<List<Card>> setsOfBestColor = choseSetsOfBestColor(game, twoColoredSetColor1, twoColoredSetColor2);
        // looks if there are sets with wildcards in it
        ArrayList<List<Card>> filteredSets = getBestSetWithWildCards(setsOfBestColor);

        // merges all possible sets/cards together
        ArrayList<List<Card>> allSets = new ArrayList<>(decideActionOrNumberCards(actionsCards, filteredSets, topCard));

        if(allSets.size() != 0){
            return allSets.get(0); // if there are possible playable cards
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * STRATEGY
     * if amount of cards to discard is 1: plays numbercard if possible, else plays actioncard
     * if amount of cards to discard is 2 or 3: plays actioncard if possible, else plays numbercard
     * strategy to save actionscards; plays actioncards only if the discardamount is higher than one to safe one or two cards
     *
     * @param possibleActioncards
     * @param possibleSets
     * @param topCard
     * @return possibleDecisions
     */
    public ArrayList<List<Card>> decideActionOrNumberCards(ArrayList<List<Card>> possibleActioncards, ArrayList<List<Card>> possibleSets, Card topCard){

        ArrayList<List<Card>> possibleDecisions = new ArrayList<>();

        if(((NumberCard)topCard).getValue() == 1){ // preferes to play a numbercard instead of an actioncard when amount of discarded cards is 1
            if(possibleSets.size() != 0){
                possibleDecisions.addAll(possibleSets);
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
            if(possibleDecisions.size() == 0 && possibleSets.size() != 0){
                possibleDecisions.addAll(possibleSets);
            }
        }

        // size = 0 when no possible decisions are possible (draw or say nope)
        return possibleDecisions;
    }

    /**
     * STRATEGY
     * if numbercards are in first place
     * search for the sets with the highest amount of two colored cards
     * if possibleSets is empty
     * returns possibleSets (might be empty then)
     *
     * @param possibleSets
     * @return set
     */
    public ArrayList<List<Card>> selectCardsWithMostTwoColoredCards(ArrayList<List<Card>> possibleSets){

        ArrayList<List<Card>> foundSets = new ArrayList<>();

        if(possibleSets.size() != 0){
            //possible cards to play found
            int wantedTwoColoredAmount = 3; // value of two colored numbercards wanted
            while(true) {
                for (List<Card> set : possibleSets) {
                    int twoColoredCardsCounter = 0;
                    for (Card card : set) {
                        if (card.getColors().size() >= 2) {
                            twoColoredCardsCounter++;
                        }
                    }
                    if (twoColoredCardsCounter == wantedTwoColoredAmount) {
                        foundSets.add(set);
                    }
                }
                if(foundSets.size() != 0){
                    break;
                }
                if(wantedTwoColoredAmount > 0) {
                    wantedTwoColoredAmount--; // reduces the value of wantedToColoredAmount by 1 if no set was found
                } else {
                    // no sets found
                    break;
                }
            }
        }

        if(foundSets.size() != 0){
            return foundSets;
        }

        // no possible cards to play -> draw or say nope
        return possibleSets;
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

        // searches the highest amount of cards of all players (except yourself)
        for(Player player: playerList){
            if(!player.equals(game.getCurrentPlayer()) && player.getCards().size() > highestCardOnHandValue && player.getCardAmount() > 0){
                highestCardOnHandValue = player.getCards().size();
            }
        }

        // returns the player with that found amount
        for(Player player: playerList){
            if(!player.equals(game.getCurrentPlayer()) && player.getCards().size() == highestCardOnHandValue && player.getCardAmount() > 0){
                return player;
            }
        }

        // if something went wrong, returns a random player
        return getOtherPlayer(game);
    }

    /**
     * STRATEGY
     * determines the max. nominate value, that doesn't give you problems, if the enemy cant lay a card on it
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

        // chooses the color where the player has the least amount of on his hand
        if(tempHighestAmountOfColor == amountRed){
            return "red";
        } else if(tempHighestAmountOfColor == amountBlue){
            return "blue";
        } else if(tempHighestAmountOfColor == amountYellow){
            return "yellow";
        } else if(tempHighestAmountOfColor == amountGreen){
            return "green";
        }

        // if something would go wrong, returns color red
        return "red";
    }

    /**
     * STRATEGY
     * sorts the playable actioncards:
     * priority:
     * 1. reset -> enemy always has discard one card; no own disadvantage
     * 2. invisible (if card on top is wildcard)
     * 3. invisible (if card on top is reset)
     * 4. nominate
     * 5. invisible (if card on top is nominate)
     * 6. invisible (if card on top is numbercard)
     *
     * @param possibleActionCards
     * @return sortedActionCards
     */
    public ArrayList<List<Card>> sortActionCards(Game game, ArrayList<List<Card>> possibleActionCards){

        ArrayList<List<Card>> sortedActionCards = new ArrayList<>();

        if(possibleActionCards.size() != 0){

            // add cards of priority 1 to list
            for(List<Card> possibleCards: possibleActionCards){
                if(possibleCards.get(0).getCardType().equals("reset")){
                    sortedActionCards.add(possibleCards);
                }
            }

            // add cards of priority 2 to list
            if(possibleActionCards.size() != 0) {
                for (List<Card> possibleCards : possibleActionCards) {
                    Card topCard = game.getDiscardPile().get(0);
                    if (possibleCards.get(0).getCardType().equals("invisible") && (topCard.getColors().size() == 4 && topCard.getCardType().equals("number"))) {
                        sortedActionCards.add(possibleCards);
                    }
                }
            }

            // add cards of priority 3 to list
            if(possibleActionCards.size() != 0) {
                for (List<Card> possibleCards : possibleActionCards) {
                    Card topCard = game.getDiscardPile().get(0);
                    if (possibleCards.get(0).getCardType().equals("invisible") && topCard.getCardType().equals("reset")) {
                        sortedActionCards.add(possibleCards);
                    }
                }
            }

            // add cards of priority 4 to list
            if(possibleActionCards.size() != 0) {
                for (List<Card> possibleCards : possibleActionCards) {
                    if (possibleCards.get(0).getCardType().equals("nominate")) {
                        sortedActionCards.add(possibleCards);
                    }
                }
            }

            // add cards of priority 5 to list
            if(possibleActionCards.size() != 0) {
                for (List<Card> possibleCards : possibleActionCards) {
                    Card topCard = game.getDiscardPile().get(0);
                    if (possibleCards.get(0).getCardType().equals("invisible") && topCard.getCardType().equals("nominate")) {
                        sortedActionCards.add(possibleCards);
                    }
                }
            }

            // add cards of priority 6 to list
            if(possibleActionCards.size() != 0) {
                for (List<Card> possibleCards : possibleActionCards) {
                    Card topCard = game.getDiscardPile().get(0);
                    if (possibleCards.get(0).getCardType().equals("invisible") && topCard.getCardType().equals("number")) {
                        sortedActionCards.add(possibleCards);
                    }
                }
            }
        }

        if(sortedActionCards.size() != 0) {
            return sortedActionCards;
        } else {
            return possibleActionCards;
        }
    }

    /**
     * STRATEGY
     * determines the best color of sets by comparing the amount of the cards of the color in your hand
     *
     * @return sets of best color
     */
    public ArrayList<List<Card>> choseSetsOfBestColor(Game game, ArrayList<List<Card>> possibleSetColor1, ArrayList<List<Card>> possibleSetColor2) {

        if (possibleSetColor1.size() == 0 && possibleSetColor2.size() == 0) { // if there are no sets of both colors
            return new ArrayList<>();
        }

        if (possibleSetColor1.size() != 0 && possibleSetColor2.size() == 0) { // if there just sets of the first color
            return possibleSetColor1;
        }

        if (possibleSetColor1.size() == 0) { // if there just sets of the second color
            return possibleSetColor2;
        }

        // if there are sets of both colors

        // calculates the main color of both sets independent
        String firstSetsMainColor = "";
        for (List<Card> set : possibleSetColor1) {
            for (Card card : set) {
                if (card.getColors().size() == 1) {
                    firstSetsMainColor = card.getColors().get(0);
                }
            }
        }

        String secondSetsMainColor = "";

        for (List<Card> set : possibleSetColor2) {
            for (Card card : set) {
                if (card.getColors().size() == 1) {
                    secondSetsMainColor = card.getColors().get(0);
                }
            }
        }

        // compares the amount of cards on your hand of each color
        if (getAmountOfCardsWithColor(game, firstSetsMainColor) <= getAmountOfCardsWithColor(game, secondSetsMainColor)) {
            return possibleSetColor1; // if there are less or an even amount of cards the first color on the hand
        } else {
            return possibleSetColor2;
        }
    }

    /**
     * STRATEGY
     * if wildcard on hand
     * searches for sets with a wildcard
     * else
     * returns all sets
     *
     * @param possibleSets
     * @return foundSets (if there are sets with wildcards), possibleSets (if there are no wildcards), empty arraylist (if there are no sets)
     */
    public ArrayList<List<Card>> getBestSetWithWildCards(ArrayList<List<Card>> possibleSets){

        ArrayList<List<Card>> foundWildCardSets = new ArrayList<>();

        if(possibleSets.size() != 0) {
            for (List<Card> set : possibleSets) {
                for (Card card: set){
                    if(card.getColors().size() == 4){
                        foundWildCardSets.add(set);
                    }
                }
            }

            if(foundWildCardSets.size() != 0){
                return foundWildCardSets;
            } else {
                return possibleSets;
            }
        } else {
            return new ArrayList<>();
        }
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

        if(chosenCards.size() == 0) { // if no cards could be played
            if(beforeDrawing) {
                action = new TakeCard("take", "no cards to discard", 1, new ArrayList<>(), game.getCurrentPlayer());
            } else {
                action = new SayNope("nope", "no cards to discard", game.getCurrentPlayer());
            }
        } else {
            // plays the nominate card depending on the amount of colors
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
                // discards card depending on the calculated set
                action = new DiscardCard("discard", "had to discard", chosenCards.size(), chosenCards, game.getCurrentPlayer());
            }
        }

        return action;
    }

    /**
     * determines the amount of cards to discard depending on the card on top of the discardpile
     * translates all cards to numbercards -> easier to handle
     *
     * @param game
     * @param actTopCard
     * @param topCardIndex
     * @return value of the card
     */
    public int getValueOfCard(Game game, Card actTopCard, int topCardIndex){

        switch (actTopCard.getCardType()) {
            case "number" -> { // if card on top of the discard pile is a numbercards
                return ((NumberCard) actTopCard).getValue();
            }

            case "reset" -> { // if card on top of the discard pile is a reset actioncard
                return 1;
            }

            case "nominate" -> { // if card on top of the discard pile is a nominate actioncard
                if(game.getLastNominateAmount() != 0) {
                    return game.getLastNominateAmount();
                }
            }

            case "invisible" -> { // if card on top of the discard pile is a invisible actioncard
                int newTopCardIndex = topCardIndex + 1;
                if (game.getDiscardPile().size() > newTopCardIndex) {
                    return getValueOfCard(game, game.getDiscardPile().get(newTopCardIndex), newTopCardIndex); // recursive call to check the card below the invisible actioncard
                }

                if (game.getDiscardPile().get(game.getDiscardPile().size() - 1).getCardType().equals("invisible") && game.getDiscardPile().size() - 1 == newTopCardIndex){
                    return 1;
                }
            }
        }

        // theoretically never used
        return 0;
    }

    /**
     * determines the color(s) of cards to discard depending on the card on top of the discardpile
     * translates all cards to numbercards -> easier to handle
     *
     * @param game
     * @param actTopCard
     * @param topCardIndex
     * @return color(s) of the card
     */
    public List<String> getColorOfCard(Game game, Card actTopCard, int topCardIndex){

        switch (actTopCard.getCardType()) {
            case "number", "reset" -> { // if card on top of the discard pile is a numbercards or reset actioncard
                return actTopCard.getColors();
            }
            case "nominate" -> { // if card on top of the discard pile is a nominate actioncard
                if(actTopCard.getColors().size() == 4) {
                    return List.of(game.getLastNominateColor());
                } else {
                    return actTopCard.getColors();
                }
            }
            case "invisible" -> { // if card on top of the discard pile is a invisible actioncard
                int newTopCardIndex = topCardIndex + 1;
                if (game.getDiscardPile().size() > newTopCardIndex) {
                    return getColorOfCard(game, game.getDiscardPile().get(newTopCardIndex), newTopCardIndex); // recursive call to check the card below the invisible actioncard
                }

                if (game.getDiscardPile().get(game.getDiscardPile().size()- 1 ).getCardType().equals("invisible")  && game.getDiscardPile().size() - 1 == newTopCardIndex){
                    System.out.println("Invisible0");
                    return game.getDiscardPile().get(game.getDiscardPile().size() - 1).getColors();
                }
            }
        }

        // theoretically never used
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
     * @return foundActionCards (if the amount of found action cards > 0), else: empty arraylist
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

    /**
     * method to check if the colors of two cards match fit together
     *
     * @param card1
     * @param card2
     * @param matchFirstColor
     * @return true (if color matches of both cards), else: false
     */
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
            if(!player.equals(game.getCurrentPlayer()) && player.getCardAmount() > 0){
                return player;
            }
        }

        return null;
    }
}