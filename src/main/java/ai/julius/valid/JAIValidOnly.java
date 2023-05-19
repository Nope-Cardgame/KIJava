package ai.julius.valid;

import ai.julius.Decider;
import ai.julius.adapters.JGameAdapter;
import ai.julius.adapters.JPlayerAdapter;
import gameobjects.Game;
import gameobjects.actions.*;
import gameobjects.cards.Card;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JAIValidOnly implements Decider {

    @Override
    public String decide(Game game) {
        Action action;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        if (gameAdapter.isLastAction("take") && !game.getState().equals("turn_start")) {
            action = actionAfterTakeCard(game);
            Logger.getLogger(getClass().getSimpleName()).info("after take");
        } else {
            action = actionBeforeTakeCard(game);
            Logger.getLogger(getClass().getSimpleName()).info("before take");
        }
        try {
            action.toJSON();
        } catch (NullPointerException e) {
            Logger.getLogger(this.getClass().getSimpleName()).info("Caught some nasty NPException");
            Logger.getLogger(this.getClass().getSimpleName()).info(game.toJSON());
        }
        return action.toJSON();
    }

    @Override
    public Action actionAfterTakeCard(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.getTopCard().getCardType().equals("number")) {
            if (playerAdapter.hasCompleteSet(gameAdapter.getTopCard())) {
                List<Card> discardCards = playerAdapter.getStupidSet(gameAdapter.getTopCard());
                action = getActionWhenNominated(game, discardCards);
            } else {
                action = new SayNope("nope","no cards to discard",game.getCurrentPlayer());
            }
        } else {
            if (gameAdapter.getTopCard().getCardType().equals("nominate")) {
                NominateCard nominateCard = (NominateCard) game.getLastAction();
                List<Card> oneCardDiscard = playerAdapter.getStupidCard(nominateCard.getNominatedColor());
                if (!oneCardDiscard.isEmpty()) {
                    action = getActionWhenNominated(game, oneCardDiscard);
                } else {
                    action = new SayNope("nope", "no cards to discard",game.getCurrentPlayer());
                }

            }
        }
        return action;
    }

    @Override
    public Action actionBeforeTakeCard(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.getTopCard().getCardType().equals("number")) {
            if (playerAdapter.hasCompleteSet(gameAdapter.getTopCard())) {
                List<Card> discardCards = playerAdapter.getStupidSet(gameAdapter.getTopCard());
                action = getActionWhenNominated(game, discardCards);
            } else {
                action = new TakeCard("take","no cards to discard",1,new ArrayList<>(),game.getCurrentPlayer());
            }
        } else {
            Logger.getLogger("IDK").info("Diesen Punkt kannst du aktuell nicht erreichen");
            Logger.getLogger("IDK").info(game.toJSON());

            if (gameAdapter.getTopCard().getCardType().equals("nominate")) {
                NominateCard nominateCard = (NominateCard) game.getLastAction();
                List<Card> oneCardDiscard = playerAdapter.getStupidCard(nominateCard.getNominatedColor());
                if (!oneCardDiscard.isEmpty()) {
                    action = getActionWhenNominated(game, oneCardDiscard);
                } else {
                    action = new TakeCard("nominate","no cards to discard",1,new ArrayList<>(),game.getCurrentPlayer());
                }
            }
        }
        return action;
    }

    @NotNull
    private Action getActionWhenNominated(Game game, List<Card> oneCardDiscard) {
        Action action;
        if (oneCardDiscard.get(0).getCardType().equals("nominate")) {
            action = new NominateCard(
                    "nominate",
                    "nomination was on first place",
                    oneCardDiscard.size(),
                    oneCardDiscard,
                    game.getCurrentPlayer(),
                    game.getCurrentPlayer(),
                    oneCardDiscard.get(0).getColors().get(0)
            );
        } else {
            action = new DiscardCard("discard","had to discard cards",oneCardDiscard.size(),oneCardDiscard,game.getCurrentPlayer());
        }
        return action;
    }


}
