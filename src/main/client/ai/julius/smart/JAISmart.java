package ai.julius.smart;

import ai.julius.adapters.CardAdapter;
import ai.julius.Decider;
import ai.julius.adapters.JGameAdapter;
import ai.julius.adapters.JPlayerAdapter;
import gameobjects.Game;
import gameobjects.actions.*;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JAISmart implements Decider {

    @Override
    public Action actionBeforeTakeCard(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.nominateOnly()) {
            LOG.info("in nominateOnly");
            action = handleNominateOnly(game,gameAdapter,playerAdapter);
        } else if (gameAdapter.invisibleOnly()) {
            LOG.info("in invisibleOnly");
            action = handleInvisibleOnlyBefore(game, gameAdapter, playerAdapter);
        } else if (gameAdapter.getTopCard().getCardType().equals("number")) {
            LOG.info("in numbercard top");
            action = handleNumberCardBefore(game,gameAdapter,playerAdapter);
        } else {
            LOG.info("in actioncard top");
            action = handleActionCardBefore(game,gameAdapter,playerAdapter);
        }
        return action;
    }

    private Action handleNominateOnly(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        LOG.info("in handleNominateOnly");
        Action action;
        Card topCard = gameAdapter.getTopCard();
        LOG.info("in handleNominateOnly");
        if (hasMoreThanOneColor(topCard)) {
            action = new NominateCard("nominate","had to nominate",0,new ArrayList<Card>(),game.getCurrentPlayer(),gameAdapter.getSmartPlayer(), playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
        } else {
            action = new NominateCard("nominate","had to nominate",0,new ArrayList<Card>(),game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
        }
        return action;
    }

    @Override
    public Action actionAfterTakeCard(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.nominateOnly()) {
            LOG.info("in nominateOnly");
            action = handleNominateOnly(game,gameAdapter,playerAdapter);
        }else if (gameAdapter.invisibleOnly()) {
            action = handleInvisibleOnlyAfter(game, gameAdapter, playerAdapter);
        } else if (gameAdapter.getTopCard().getCardType().equals("number")) {
            action = handleNumberCardAfter(game, gameAdapter, playerAdapter);
        } else {
            action = handleActionCardAfter(game, gameAdapter,playerAdapter);
        }
        return action;
    }

    private Action handleActionCardBefore(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action = null;
        if (gameAdapter.getTopCard().getCardType().equals("nominate")) {
            LOG.info("in handleActionCardAfter() at nominate");
            int nominatedAmount = game.getLastNominateAmount();
            LOG.info("nominateAmount: " + nominatedAmount);
            String nominateColor = hasMoreThanOneColor(gameAdapter.getTopCard()) ? game.getLastNominateColor() : gameAdapter.getTopCard().getColors().get(0);
            LOG.info("nominateColor: " + nominateColor);
            if (playerAdapter.hasCompleteSetWithColor(nominatedAmount,nominateColor)) {
                LOG.info("handleActionCardBefore at has Set");
                if (playerAdapter.hasActionCards(nominateColor)) {
                    LOG.info("handleActionCardBefore at hasActionCards");
                    Card bestCard = playerAdapter.getSmartCard(nominateColor);
                    List<Card> cardList = new ArrayList<>();
                    cardList.add(bestCard);
                    LOG.info("Best Card: " + bestCard.toJSON());
                    if (bestCard.getCardType().equals("nominate")) {
                        LOG.info("card is nominate");
                        if (hasMoreThanOneColor(bestCard)) {
                            LOG.info("nominate with all colors");
                            action = new NominateCard("nominate","had to nominate",cardList.size(),cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(), playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                        } else {
                            LOG.info("nominate with ONE COLOR ONLY");
                            action = new NominateCard("nominate","had to nominate",cardList.size(),cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                        }
                    } else {
                        LOG.info("NOT an nominateCard");
                        action = new DiscardCard("discard","had to discard",cardList.size(),cardList,game.getCurrentPlayer());
                    }
                } else {
                    LOG.info("handleActionCardBefore at has no action cards");
                    List<Card> set = playerAdapter.getSmartSet(nominatedAmount,nominateColor);
                    if (set.isEmpty()) {
                        set = playerAdapter.getStupidSetColor(nominatedAmount,nominateColor);
                    }
                    LOG.info("Found set: ");
                    LOG.info(Arrays.toString(set.toArray()));
                    action = new DiscardCard("discard","had to discard",set.size(),set,game.getCurrentPlayer());
                }
            } else {
                LOG.info("handleActionCardBefore at has NOT A Set");
                action = new TakeCard("take","no cards to discard",1,new ArrayList<Card>(),game.getCurrentPlayer());
            }
        } else if (gameAdapter.getTopCard().getCardType().equals("reset")) {
            action = handleResetOrWildcard(game,gameAdapter,playerAdapter);
        }
        return action;
    }

    private Action handleNumberCardBefore(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action = null;
        NumberCard currentCard = (NumberCard) gameAdapter.getTopCard();
        CardAdapter cardAdapter = new CardAdapter(currentCard);
        if (currentCard.getName().equals("wildcard")) {
            action = handleResetOrWildcard(game,gameAdapter,playerAdapter);
        } else {
            if (cardAdapter.hasTwoColors()) {
                String validColor = null;
                for (int iterator = 0; iterator < currentCard.getColors().size(); iterator++) {
                    String color = currentCard.getColors().get(iterator);
                    if (playerAdapter.hasCompleteSetWithColor(currentCard,color)) {
                        validColor = color;
                        break;
                    }
                }
                if (validColor == null) {
                    action = new TakeCard("take","no cards to discard",1,new ArrayList<Card>(),game.getCurrentPlayer());
                } else {
                    if (playerAdapter.hasActionCards(validColor)) {
                        Card bestCard = playerAdapter.getSmartCard(validColor);
                        List<Card> cardList = new ArrayList<>();
                        cardList.add(bestCard);
                        if (bestCard.getCardType().equals("nominate")) {
                            if (hasMoreThanOneColor(bestCard)) {
                                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                            } else {
                                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                            }
                        } else {
                            action = new DiscardCard("discard","had to discard",1,cardList,game.getCurrentPlayer());
                        }
                    } else {
                        List<Card> set = playerAdapter.getSmartSet(currentCard,validColor);
                        if (set.isEmpty()) {
                            set = playerAdapter.getStupidSetColor(currentCard,validColor);
                        }
                        action = new DiscardCard("discard","had to discard",set.size(),set,game.getCurrentPlayer());
                    }
                }
            } else {
                String color = currentCard.getColors().get(0);
                if (playerAdapter.hasCompleteSetWithColor(currentCard,color)) {
                    if (playerAdapter.hasActionCards(color)) {
                        Card bestCard = playerAdapter.getSmartCard(color);
                        List<Card> cardList = new ArrayList<>();
                        cardList.add(bestCard);
                        if (bestCard.getCardType().equals("nominate")) {
                            if (hasMoreThanOneColor(bestCard)) {
                                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                            } else {
                                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(), Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                            }
                        } else {
                            action = new DiscardCard("discard","had to discard",1,cardList,game.getCurrentPlayer());
                        }
                    } else {
                        List<Card> set = playerAdapter.getSmartSet(currentCard,color);
                        if (set.isEmpty()) {
                            set = playerAdapter.getStupidSetColor(currentCard,color);
                        }
                        action = new DiscardCard("discard","had to discard",set.size(),set,game.getCurrentPlayer());
                    }
                } else {
                    action = new TakeCard("take","no cards to discard",1,new ArrayList<Card>(),game.getCurrentPlayer());
                }
            }
        }
        return action;
    }

    private Action handleInvisibleOnlyBefore(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action;
        String invisibleColor = game.getDiscardPile().get(0).getColors().get(0);
        if (playerAdapter.hasColor(invisibleColor)) {
            Card bestCard = playerAdapter.getSmartCard(invisibleColor);
            List<Card> cardList = new ArrayList<>();
            cardList.add(bestCard);
            if (bestCard.getCardType().equals("nominate")) {
                if (hasMoreThanOneColor(bestCard)) {
                    action = new NominateCard("nominate","had to nominate",1,cardList, game.getCurrentPlayer(), gameAdapter.getSmartPlayer(), playerAdapter.getSmartColor(), Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                } else {
                    action = new NominateCard("nominate","had to nominate",1,cardList, game.getCurrentPlayer(), gameAdapter.getSmartPlayer(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                }
            } else {
                action = new DiscardCard("discard", "had to discard",1,cardList, game.getCurrentPlayer());
            }
        } else {
            action = new TakeCard("take", "no cards to discard",1,new ArrayList<>(), game.getCurrentPlayer());
        }
        return action;
    }

    private Action handleActionCardAfter(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action = null;
        if (gameAdapter.getTopCard().getCardType().equals("nominate")) {
            LOG.info("in handleActionCardAfter()");
            int nominatedAmount = game.getLastNominateAmount();
            LOG.info("nominateAmount: " + nominatedAmount);
            String nominateColor = hasMoreThanOneColor(gameAdapter.getTopCard()) ? game.getLastNominateColor() : gameAdapter.getTopCard().getColors().get(0);
            LOG.info("nominateColor: " + nominateColor);
            if (playerAdapter.hasCompleteSetWithColor(nominatedAmount,nominateColor)) {
                if (playerAdapter.hasActionCards(nominateColor)) {
                    Card bestCard = playerAdapter.getSmartCard(nominateColor);
                    List<Card> cardList = new ArrayList<>();
                    cardList.add(bestCard);
                    if (bestCard.getCardType().equals("nominate")) {
                        if (hasMoreThanOneColor(bestCard)) {
                            action = new NominateCard("nominate","had to nominate",cardList.size(),cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(), playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                        } else {
                            action = new NominateCard("nominate","had to nominate",cardList.size(),cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                        }
                    } else {
                        action = new DiscardCard("discard","had to discard",cardList.size(),cardList,game.getCurrentPlayer());
                    }
                } else {
                    List<Card> set = playerAdapter.getSmartSet(nominatedAmount,nominateColor);
                    if (set.isEmpty()) {
                        set = playerAdapter.getStupidSetColor(nominatedAmount,nominateColor);
                    }
                    action = new DiscardCard("discard","had to discard",set.size(),set,game.getCurrentPlayer());
                }
            } else {
                action = new SayNope("nope","no cards to discard",game.getCurrentPlayer());
            }
        } else if (gameAdapter.getTopCard().getCardType().equals("reset")) {
            action = handleResetOrWildcard(game,gameAdapter,playerAdapter);
        }
        return action;
    }

    private Action handleResetOrWildcard(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action;
        Card bestCard = playerAdapter.getSmartCard();
        List<Card> cardList = new ArrayList<>();
        cardList.add(bestCard);
        if (bestCard.getCardType().equals("nominate")) {
            if (hasMoreThanOneColor(bestCard)) {
                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
            } else {
                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
            }
        } else {
            action = new DiscardCard("discard","had to discard",1,cardList,game.getCurrentPlayer());
        }
        return action;
    }

    private Action handleNumberCardAfter(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action = null;
        NumberCard currentCard = (NumberCard) gameAdapter.getTopCard();
        CardAdapter cardAdapter = new CardAdapter(currentCard);
        if (currentCard.getName().equals("wildcard")) {
            action = handleResetOrWildcard(game,gameAdapter,playerAdapter);
        } else {
            if (cardAdapter.hasTwoColors()) {
                String validColor = "";
                for (int iterator = 0; iterator < currentCard.getColors().size() && validColor.equals(""); iterator++) {
                    String color = currentCard.getColors().get(iterator);
                    if (playerAdapter.hasCompleteSetWithColor(currentCard,color)) {
                        validColor = color;
                    }
                }
                if (validColor.equals("")) {
                    action = new SayNope("nope","no cards to discard",game.getCurrentPlayer());
                } else {
                    if (playerAdapter.hasActionCards(validColor)) {
                        Card bestCard = playerAdapter.getSmartCard(validColor);
                        List<Card> cardList = new ArrayList<>();
                        cardList.add(bestCard);
                        if (bestCard.getCardType().equals("nominate")) {
                            if (hasMoreThanOneColor(bestCard)) {
                                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                            } else {
                                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(), Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                            }
                        } else {
                            action = new DiscardCard("discard","had to discard",1,cardList,game.getCurrentPlayer());
                        }
                    } else {
                        List<Card> set = playerAdapter.getSmartSet(currentCard,validColor);
                        if (set.isEmpty()) {
                            set = playerAdapter.getStupidSetColor(currentCard,validColor);
                        }
                        action = new DiscardCard("discard","had to discard",set.size(),set,game.getCurrentPlayer());
                    }
                }
            } else {
                String color = currentCard.getColors().get(0);
                if (playerAdapter.hasCompleteSetWithColor(currentCard,color)) {
                    if (playerAdapter.hasActionCards(color)) {
                        Card bestCard = playerAdapter.getSmartCard(color);
                        List<Card> cardList = new ArrayList<>();
                        cardList.add(bestCard);
                        if (bestCard.getCardType().equals("nominate")) {
                            if (hasMoreThanOneColor(bestCard)) {
                                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                            } else {
                                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(), Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
                            }
                        } else {
                            action = new DiscardCard("discard","had to discard",1,cardList,game.getCurrentPlayer());
                        }
                    } else {
                        List<Card> set = playerAdapter.getSmartSet(currentCard,color);
                        if (set.isEmpty()) {
                            set = playerAdapter.getStupidSetColor(currentCard,color);
                        }
                        action = new DiscardCard("discard","had to discard",set.size(),set,game.getCurrentPlayer());
                    }
                } else {
                    action = new SayNope("nope","no cards to discard",game.getCurrentPlayer());
                }
            }
        }
        return action;
    }


    private Action handleInvisibleOnlyAfter(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action;
        String colorOfInvisible = game.getDiscardPile().get(0).getColors().get(0);
        if (playerAdapter.hasColor(colorOfInvisible)) {
            Card card = playerAdapter.getSmartCard(colorOfInvisible);
            action = getActionWithOneCard(game, gameAdapter, playerAdapter, card);
        } else {
            action = new SayNope("nope", "no cards to discard", game.getCurrentPlayer());
        }
        return action;
    }

    private Action getActionWithOneCard(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter, Card card) {
        Action action;
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        if (card.getCardType().equals("nominate")) {
            if (hasMoreThanOneColor(card)) {
                action = new NominateCard("nominate","nominate was best decision",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
            } else {
                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),Math.min(gameAdapter.getSmartPlayer().getCardAmount(),2));
            }
        } else {
            action = new DiscardCard("discard","had to discard an actioncard",1,cardList,game.getCurrentPlayer());
        }
        return action;
    }

    /**
     * Checks if a card has more than one color
     *
     * @param card the card that was put in
     * @return true if the colors size is more than one, false otherwise
     */
    private boolean hasMoreThanOneColor(Card card) {
        return card.getColors().size() > 1;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
