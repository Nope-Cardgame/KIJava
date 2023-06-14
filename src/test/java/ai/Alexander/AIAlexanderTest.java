package ai.Alexander;
import ai.alexander.AIAlexander;
import gameobjects.Game;
import gameobjects.Player;
import gameobjects.actions.NominateCard;
import gameobjects.cards.ActionCard;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the methods of AIAlexander, which calculates the actions to play Nope according
 * to empirically tested strategies
 * @author Alexander
 */
public class AIAlexanderTest {
    AIAlexander aiAlexander = new AIAlexander(); //the ai calculating the actions
    //four-colored numbercard to test purpuses
    NumberCard multicard = new NumberCard("number", 1, List.of("red", "blue", "green", "yellow"), "");
    //single colored invisible action card
    ActionCard invisible = new ActionCard("invisible", List.of("blue"), "");
    //the complete game state on which the tests base
    Game game = createTestGame();
    // two different numbercards for test purposes
    NumberCard testCard1 =  new NumberCard("number", 3, List.of("red", "green"), "");
    NumberCard testCard2 =  new NumberCard("number", 3, List.of("blue"), "");
    //method to create a static game state for all tests
    Game createTestGame() {
        ArrayList<Card> cards = new ArrayList<>(); //add three different colors
        cards.add(new NumberCard("number", 1, List.of("red", "blue"), ""));
        cards.add(new NumberCard("number", 2, List.of("red", "green"), ""));
        cards.add(new NumberCard("number", 3, List.of("red"), ""));
        //both players can get the same cards, because only one player is used for action tests
        Player player1 = new Player("p1", "", 3, cards, 0, false);
        Player player2 = new Player("p2", "", 3, cards, 0, false);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        ArrayList<Card> discardPile = new ArrayList<>();
        discardPile.add(invisible); // add a single invisible card to test nomimated flipped
        Game game = new Game("", "nominate_flipped",
                false, false, false,
                null, "", 0,
                players, discardPile,
                null, player1, "", null,
                null, "", 0, "",
                0, 0, false, 0

        );
        return game;
    }

    /**
     * test if the opponent is not the current player
     */
    @Test
    void getOpponentTest() {
        assertEquals(aiAlexander.getOpponent(game), game.getPlayers().get(1));
        assertFalse(aiAlexander.getOpponent(game).equals(game.getCurrentPlayer()));
    }

    /**
     * test if colors of players are counted correctely
     */
    @Test
    void countOfColorTest() {
        assertTrue(aiAlexander.countCardsOfColor(game.getCurrentPlayer(), "red") == 3);
        assertTrue(aiAlexander.countCardsOfColor(game.getCurrentPlayer(), "blue") == 1);
        assertTrue(aiAlexander.countCardsOfColor(game.getCurrentPlayer(), "green") == 1);
        assertTrue(aiAlexander.countCardsOfColor(game.getCurrentPlayer(), "yellow") == 0);
    }

    /**
     * test, if the color of lowest color count is selected
     */
    @Test
    void chooseNominatedColorTest() {
        assertEquals(aiAlexander.chooseNominateColor(multicard, game.getCurrentPlayer()), "yellow");
    }

    /**
     * test, if the invisible card is correctely transfered to a number card
     */
    @Test
    void ifInvisibleOnHandTest() {
        assertTrue(aiAlexander.ifInvisibleOnHand(game).getColors().equals(invisible.getColors()));
        assertTrue(aiAlexander.ifInvisibleOnHand(game).getCardType().equals("number"));
    }

    /**
     * test if the nomination value is choosen as planned
     */
    @Test
    void getNominationIntTest() {
        assertTrue(aiAlexander.getNomimationInt(game) == 1);
    }

    /**
     * test if the nominated-flipped states get a return of nomination
     * with amount 0 and the opponent is choosen
     */
    @Test
    void nominateFlippedTest() {
        assertTrue(aiAlexander.nominateflipped(game).getType().equals("nominate"));
        NominateCard nominateCard = (NominateCard) aiAlexander.nominateflipped(game);
        assertTrue(nominateCard.getAmount() == 0);
        assertEquals(nominateCard.getNominatedPlayer(), aiAlexander.getOpponent(game));
    }

    /**
     * test if two cards are compared correctely according to color
     */
    @Test
    void cardWithSameColotTest() {
        assertTrue(aiAlexander.cardWithSameColor(multicard, invisible));
        assertTrue(aiAlexander.cardWithSameColor(game.getCurrentPlayer().getCards().get(0),
                game.getCurrentPlayer().getCards().get(2)));
        assertTrue(aiAlexander.cardWithSameColor(game.getCurrentPlayer().getCards().get(1),
                game.getCurrentPlayer().getCards().get(2)));
        assertFalse(aiAlexander.cardWithSameColor(invisible,
                game.getCurrentPlayer().getCards().get(2)));
    }

    /**
     * test if no action is discarded if no action card is available
     */
    @Test
    void disCardActionCardTest() {
        assertEquals(aiAlexander.discardActionCard(game,  multicard,game.getCurrentPlayer()), null);
    }

    /**
     * test if a player has full sets of given color
     */
    @Test
    void hasSetWithColorTest() {
        assertTrue(aiAlexander.hasSetWithColor(multicard, "red", game.getCurrentPlayer()));
        assertTrue(aiAlexander.hasSetWithColor(multicard, "blue", game.getCurrentPlayer()));
        assertTrue(aiAlexander.hasSetWithColor(multicard, "green", game.getCurrentPlayer()));
        assertFalse(aiAlexander.hasSetWithColor(multicard, "yellow", game.getCurrentPlayer()));
        assertTrue(aiAlexander.hasSetWithColor(testCard1, "red", game.getCurrentPlayer()));
        assertFalse(aiAlexander.hasSetWithColor(testCard1, "green", game.getCurrentPlayer()));
    }

    /**
     * test if a player has a complete set according to a given card
     */
    @Test
    void hasCompleteSetTest() {
        assertTrue(aiAlexander.hasCompleteSet(game.getCurrentPlayer(), multicard));
        assertTrue(aiAlexander.hasCompleteSet(game.getCurrentPlayer(), testCard1));
        assertFalse(aiAlexander.hasCompleteSet(game.getCurrentPlayer(), testCard2));
    }

    /**
     * test if reset gives a discard action return
     */
    @Test
    void handleResetCartTest() {
        assertFalse(aiAlexander.handleResetCard(game, multicard).equals(null));
        assertTrue(aiAlexander.handleResetCard(game, multicard).getType().equals("discard"));
    }

    /**
     * test if the front card is calculated correctely if the
     * upper card of discard pile is/are invisible
     */
    @Test
    void getFrontCartTest() {
        assertEquals(aiAlexander.getFrontCard(game).getColors(), game.getDiscardPile().get(0).getColors());
    }

    /**
     * test if a color has a given color
     */
    @Test
    void isColorinCardTest() {
        assertTrue(aiAlexander.isColorInCard("blue", multicard));
        assertTrue(aiAlexander.isColorInCard("red", multicard));
        assertTrue(aiAlexander.isColorInCard("green", multicard));
        assertTrue(aiAlexander.isColorInCard("yellow", multicard));
        assertFalse(aiAlexander.isColorInCard("black", multicard));
        assertTrue(aiAlexander.isColorInCard("blue", invisible));
        assertFalse(aiAlexander.isColorInCard("red", invisible));
        assertFalse(aiAlexander.isColorInCard("yellow", invisible));
        assertFalse(aiAlexander.isColorInCard("green", invisible));
        assertFalse(aiAlexander.isColorInCard("blue", testCard1));
        assertTrue(aiAlexander.isColorInCard("red", testCard1));
        assertFalse(aiAlexander.isColorInCard("yellow", testCard1));
        assertTrue(aiAlexander.isColorInCard("green", testCard1));
    }

    /**
     * test if the player returns to correct cards to a given color
     */
    @Test
    void createCardSetFromHandTest() {
        assertEquals(aiAlexander.createCardSetFromHand(multicard, "red", game.getCurrentPlayer()).get(0), game.getCurrentPlayer().getCards().get(0));
    }
}