package ai.julius.valid;

import ai.julius.Decider;
import ai.julius.adapters.CardAdapter;
import ai.julius.adapters.JGameAdapter;
import ai.julius.adapters.JPlayerAdapter;
import gameobjects.Game;
import gameobjects.actions.*;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JAIValidOnly implements Decider {

    @Override
    public String decide(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        if (gameAdapter.isLastAction("take") && !game.getState().equals("turn_start")) {
            action = actionAfterTakeCard(game);
        } else {
            action = actionBeforeTakeCard(game);
        }
        assert action != null;
        return action.toJSON();
    }

    @Override
    public Action actionAfterTakeCard(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.invisibleOnly()) {
            List<Card> cardToDiscard = playerAdapter.getStupidCard(gameAdapter.getTopCard().getColors().get(0));
            if (!cardToDiscard.isEmpty()) {
                if (topCardIsNominate(cardToDiscard)) {
                    action = new NominateCard(
                            "nominate",
                            "First Card was nominate",
                            cardToDiscard.size(),
                            cardToDiscard,
                            game.getCurrentPlayer(),
                            gameAdapter.getStupidPlayer(),
                            cardToDiscard.get(0).getColors().get(0),
                            2);
                } else {
                    action = new DiscardCard(
                            "discard",
                            "first card was not nominate",
                            cardToDiscard.size(),
                            cardToDiscard,
                            game.getCurrentPlayer()
                    );
                }
            } else {
                action = new SayNope(
                        "nope",
                        "no cards to discard :)",
                        game.getCurrentPlayer()
                );
            }
        } else if (gameAdapter.getTopCard().getCardType().equals("number")) {
            NumberCard currentCard = (NumberCard) gameAdapter.getTopCard();
            CardAdapter cardAdapter = new CardAdapter(currentCard);
            if (currentCard.getName().equals("wildcard")) {
                action = getActionWildcardOrReset(game, gameAdapter, playerAdapter);
            } else {
                // Determination of Color of the set, if the color stays null, it means
                // that there is no set with any color of the numbercard that is on top
                // ==> takecard if color stays null, else another action (discard or nominate)
                String validColor = null;
                if (cardAdapter.hasTwoColors()) {
                    if (playerAdapter.hasCompleteSetWithColor(currentCard,currentCard.getColors().get(0))) {
                        validColor = currentCard.getColors().get(0);
                    } else if (playerAdapter.hasCompleteSetWithColor(currentCard,currentCard.getColors().get(1))) {
                        validColor = currentCard.getColors().get(1);
                    }
                } else {
                    if (playerAdapter.hasCompleteSetWithColor(currentCard,currentCard.getColors().get(0))) {
                        validColor = currentCard.getColors().get(0);
                    }
                }
                if (validColor == null) {
                    action = new SayNope("nope","no cards to discard",game.getCurrentPlayer());
                } else {
                    action = getActionWhenNumberCardTop(game, gameAdapter, playerAdapter, currentCard, validColor);
                }
            }
        } else {
            if (gameAdapter.getTopCard().getCardType().equals("nominate")) {
                int nominatedAmount = game.getLastNominateAmount();
                String nominateColor = game.getLastNominateColor();
                if (playerAdapter.hasCompleteSetWithColor(nominatedAmount,nominateColor)) {
                    action = getActionWhenNominateTop(game, gameAdapter, playerAdapter, nominatedAmount, nominateColor);
                } else {
                    action = new SayNope(
                            "nope",
                            "no cards to discard",
                            game.getCurrentPlayer()
                    );
                }
            } else if (gameAdapter.getTopCard().getCardType().equals("reset")) {
                action = getActionWildcardOrReset(game, gameAdapter, playerAdapter);
            }
        }
        return action;
    }

    @NotNull
    private Action getActionWildcardOrReset(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter) {
        Action action;
        List<Card> oneCard = playerAdapter.getStupidCard();
        if (topCardIsNominate(oneCard)) {
            action = new NominateCard(
                    "nominate",
                    "card was nominate",
                    oneCard.size(),
                    oneCard,
                    game.getCurrentPlayer(),
                    gameAdapter.getStupidPlayer(),
                    oneCard.get(0).getColors().get(0),
                    2
            );
        } else {
            action = new DiscardCard(
                    "discard",
                    "had to discard",
                    oneCard.size(),
                    oneCard,
                    game.getCurrentPlayer()
            );
        }
        return action;
    }

    /**
     * Checks if the "top"-card is nominate or not
     * info: it loops through invisibles and looks for
     * the first card beneath it.
     *
     * @param cardsToDiscard the List of Cards that needs to be checked
     * @return true if top card is nominate, false otherwise
     */
    public static boolean topCardIsNominate(List<Card> cardsToDiscard) {
        Card currentCard = cardsToDiscard.get(0);
        // loop until the currentCard is not invisible
        for (int iterator = 1; iterator < cardsToDiscard.size() && currentCard.getCardType().equals("invisible"); iterator++) {}
        // just return equal to nominate-boolean
        return currentCard.getCardType().equals("nominate");
    }

    @Override
    public Action actionBeforeTakeCard(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.nominateOnly()) {
            action = new NominateCard(
                    "nominate",
                    "only card on discard pile",
                    0,
                    new ArrayList<>(),
                    game.getCurrentPlayer(),
                    gameAdapter.getStupidPlayer(),
                    gameAdapter.getTopCard().getColors().get(0),
                    2
            );
        } else if (gameAdapter.invisibleOnly()) {
            List<Card> oneCard = playerAdapter.getStupidCard(game.getDiscardPile().get(0).getColors().get(0));
            if (oneCard.isEmpty()) {
                action = new TakeCard(
                        "take",
                        "no cards to discard",
                        1,
                        new ArrayList<>(),
                        game.getCurrentPlayer()
                );
            } else {
                if (topCardIsNominate(oneCard)) {
                    action = new NominateCard(
                            "nominate",
                            "nominate was available",
                            oneCard.size(),
                            oneCard,
                            game.getCurrentPlayer(),
                            gameAdapter.getStupidPlayer(),
                            oneCard.get(0).getColors().get(0),
                            2
                    );
                } else {
                    action = new DiscardCard(
                            "discard",
                            "had to discard",
                            oneCard.size(),
                            oneCard,
                            game.getCurrentPlayer());
                }
            }
        } else if (gameAdapter.getTopCard().getCardType().equals("number")) {
            NumberCard currentCard = (NumberCard) gameAdapter.getTopCard();
            CardAdapter cardAdapter = new CardAdapter(currentCard);
            if (currentCard.getName().equals("wildcard")) {
                action = getActionWildcardOrReset(game, gameAdapter, playerAdapter);
            } else {
                // Determination of Color of the set, if the color stays null, it means
                // that there is no set with any color of the numbercard that is on top
                // ==> takecard if color stays null, else another action (discard or nominate)
                String validColor = null;
                if (cardAdapter.hasTwoColors()) {
                    if (playerAdapter.hasCompleteSetWithColor(currentCard,currentCard.getColors().get(0))) {
                        validColor = currentCard.getColors().get(0);
                    } else if (playerAdapter.hasCompleteSetWithColor(currentCard,currentCard.getColors().get(1))) {
                        validColor = currentCard.getColors().get(1);
                    }
                } else {
                    if (playerAdapter.hasCompleteSetWithColor(currentCard,currentCard.getColors().get(0))) {
                        validColor = currentCard.getColors().get(0);
                    }
                }
                if (validColor == null) {
                    action = new TakeCard(
                            "take",
                            "no cards to discard",
                            1,
                            new ArrayList<>(),
                            game.getCurrentPlayer());
                } else {
                    action = getActionWhenNumberCardTop(game, gameAdapter, playerAdapter, currentCard, validColor);
                }
            }
        } else {
            if (gameAdapter.getTopCard().getCardType().equals("nominate")) {
                int nominatedAmount = game.getLastNominateAmount();
                String nominateColor = game.getLastNominateColor();
                if (playerAdapter.hasCompleteSetWithColor(nominatedAmount,nominateColor)) {
                    action = getActionWhenNominateTop(game, gameAdapter, playerAdapter, nominatedAmount, nominateColor);
                } else {
                    action = new TakeCard(
                            "take",
                            "no cards to discard",
                            1,
                            new ArrayList<>(),
                            game.getCurrentPlayer()
                    );
                }
            } else if (gameAdapter.getTopCard().getCardType().equals("reset")) {
                action = getActionWildcardOrReset(game, gameAdapter, playerAdapter);
            }
        }
        return action;
    }

    @NotNull
    private Action getActionWhenNominateTop(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter, int nominatedAmount, String nominateColor) {
        Action action;
        List<Card> set = playerAdapter.getStupidSetColor(nominatedAmount,nominateColor);
        if (topCardIsNominate(set)) {
            action = new NominateCard(
                    "nominate",
                    "had to nominate",
                    set.size(),
                    set,
                    game.getCurrentPlayer(),
                    gameAdapter.getStupidPlayer(),
                    set.get(0).getColors().get(0),
                    2
            );
        } else {
            action = new DiscardCard(
                    "discard",
                    "had to discard",
                    set.size(),
                    set,
                    game.getCurrentPlayer()
            );
        }
        return action;
    }

    @NotNull
    private Action getActionWhenNumberCardTop(Game game, JGameAdapter gameAdapter, JPlayerAdapter playerAdapter, NumberCard currentCard, String validColor) {
        Action action;
        List<Card> set = playerAdapter.getStupidSetColor(currentCard,validColor);
        if (topCardIsNominate(set)) {
            action = new NominateCard(
                    "nominate",
                    "first card was nominate",
                    set.size(),
                    set,
                    game.getCurrentPlayer(),
                    gameAdapter.getStupidPlayer(),
                    set.get(0).getColors().get(0),
                    2
            );
        } else {
            action = new DiscardCard(
                    "discard",
                    "had to discard",
                    set.size(),
                    set,
                    game.getCurrentPlayer()
            );
        }
        return action;
    }


}
