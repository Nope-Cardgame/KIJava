package ai.julius.smart;

import ai.julius.Decider;
import ai.julius.adapters.JGameAdapter;
import ai.julius.adapters.JPlayerAdapter;
import gameobjects.Game;
import gameobjects.actions.Action;
import gameobjects.actions.DiscardCard;
import gameobjects.actions.NominateCard;
import gameobjects.actions.SayNope;
import gameobjects.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class JAISmart implements Decider {

    @Override
    public Action actionBeforeTakeCard(Game game) {
        Action action = null;
        JGameAdapter gameAdapter = new JGameAdapter(game);
        JPlayerAdapter playerAdapter = new JPlayerAdapter(game.getCurrentPlayer());
        if (gameAdapter.invisibleOnly()) {
            String colorOfInvisible = game.getDiscardPile().get(0).getColors().get(0);
            if (playerAdapter.hasColor(colorOfInvisible)) {
                Card card = playerAdapter.getSmartCard(colorOfInvisible);
                List<Card> cardList = new ArrayList<>();
                if (card.getCardType().equals("nominate")) {
                    if (hasMoreThanOneColor(card)) {
                        action = new NominateCard("nominate","nominate was best decision",1,cardList,game.getCurrentPlayer(),gameAdapter.getSmartPlayer(),playerAdapter.getSmartColor(),2);
                    } else {

                    }
                } else {
                    action = new DiscardCard("discard","had to discard an actioncard",1,cardList,game.getCurrentPlayer());
                }
            } else {
                action = new SayNope("nope", "no cards to discard",game.getCurrentPlayer());
            }
        }
        return action;
    }

    private boolean hasMoreThanOneColor(Card nominate) {
        return nominate.getColors().size() > 1;
    }

    @Override
    public Action actionAfterTakeCard(Game game) {
        return null;
    }
}
