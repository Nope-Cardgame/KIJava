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
        } else {
            action = actionBeforeTakeCard(game);
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
        if (gameAdapter.getTopCard().getCardType().equals("reset") && game.getDiscardPile().size() == 1) {
            List<Card> discardedCards = playerAdapter.getStupidCard();
            if (discardedCards.get(0).getCardType().equals("nominate")) {
                action = new NominateCard("nominate","found nominate",discardedCards.size(),discardedCards,game.getCurrentPlayer(),game.getCurrentPlayer(),discardedCards.get(0).getColors().get(0),1);
            } else {
                action = new DiscardCard("discard","had to discard :(",discardedCards.size(),discardedCards,game.getCurrentPlayer());
            }
        } else if (gameAdapter.getTopCard().getCardType().equals("nominate") && game.getDiscardPile().size() == 1) {
            action = new NominateCard("nominate","nominate was first card",0,new ArrayList<>(),game.getCurrentPlayer(),game.getCurrentPlayer(),gameAdapter.getTopCard().getColors().get(0),1);
        } else if (gameAdapter.getTopCard().getCardType().equals("invisible") && game.getDiscardPile().size() == 1) {
            List<Card> discardedCards = playerAdapter.getStupidCard(gameAdapter.getTopCard().getColors().get(0));
            
        }
        if (gameAdapter.getTopCard().getCardType().equals("number")) {
            if (playerAdapter.hasCompleteSet(gameAdapter.getTopCard())) {
                List<Card> discardCards = playerAdapter.getStupidSet(gameAdapter.getTopCard());
                action = getActionWhenNominated(game, discardCards);
            } else {
                action = new TakeCard("take","no cards to discard",1,new ArrayList<>(),game.getCurrentPlayer());
            }
        } else {
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
                    oneCardDiscard.get(0).getColors().get(0),1
            );
        } else {
            action = new DiscardCard("discard","had to discard cards",oneCardDiscard.size(),oneCardDiscard,game.getCurrentPlayer());
        }
        return action;
    }


}
