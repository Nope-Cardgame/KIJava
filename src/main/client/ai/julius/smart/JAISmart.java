package ai.julius.smart;

import ai.julius.Decider;
import ai.julius.adapters.CardAdapter;
import ai.julius.adapters.JGameAdapter;
import ai.julius.adapters.JPlayerAdapter;
import gameobjects.Game;
import gameobjects.actions.Action;
import gameobjects.actions.DiscardCard;
import gameobjects.actions.NominateCard;
import gameobjects.actions.SayNope;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.util.ArrayList;
import java.util.List;

public class JAISmart implements Decider {

    @Override
    public Action actionBeforeTakeCard(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.invisibleOnly()) {
            action = handleInvisibleOnlyAfter(game, gameAdapter, playerAdapter);
        } else if (gameAdapter.getTopCard().getCardType().equals("number")) {
            action = handleNumberCardAfter(game, gameAdapter, playerAdapter);
        } else {
            action = handleActionCardAfter(game, gameAdapter,playerAdapter);
        }
        return action;
    }

    private Action handleActionCardAfter(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action = null;
        if (gameAdapter.getTopCard().getCardType().equals("nominate")) {
            int nominatedAmount = game.getLastNominateAmount();
            String nominateColor = game.getLastNominateColor();
            if (playerAdapter.hasCompleteSetWithColor(nominatedAmount,nominateColor)) {

            }
        }
        return action;
    }

    private Action handleNumberCardAfter(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action = null;
        NumberCard currentCard = (NumberCard) gameAdapter.getTopCard();
        CardAdapter cardAdapter = new CardAdapter(currentCard);
        if (currentCard.getName().equals("wildcard")) {
            Card card = playerAdapter.getSmartCard();
            action = getActionWithOneCard(game, gameAdapter, playerAdapter, card);
        } else {
            String validColor = null;
            if (cardAdapter.hasTwoColors()) {
                List<Card> cardList = null;
                for (int iterator = 0; iterator < currentCard.getColors().size() && cardList == null; iterator++) {

                }
            } else {
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
        if (card.getCardType().equals("nominate")) {
            if (hasMoreThanOneColor(card)) {
                action = new NominateCard("nominate","nominate was best decision",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),2);
            } else {
                action = new NominateCard("nominate","had to nominate",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),2);
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
    public Action actionAfterTakeCard(Game game) {
        return null;
    }
}
