package event_handling;

import ai.AIFactory;
import ai.IArtificialIntelligence;
import ai.julius.AIJulius;
import ai.julius.smart.JAISmart;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gameobjects.Game;
import gameobjects.Tournament;
import io.socket.client.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.socket.emitter.Emitter;
import logging.NopeLogger;
import logic.Main;
import org.json.JSONException;
import org.json.JSONObject;
import view.ComponentPainter;
import view.Gui;

public class ServerEventHandler {
    private final Socket socketInstance;
    private final String username;
    private final IArtificialIntelligence ai;
    private static final Logger LOG = NopeLogger.getLogger(ServerEventHandler.class.getSimpleName()); // logger of the class
    private static int roundCounter;
    private static String currentPlayer;
    private static Game game;

    /**
     * Creates a ServerEventHandler instance
     *
     * @param socket the socket instance
     * @param username the username of the user that is logged in
     */
    public ServerEventHandler(Socket socket, String username) {
        this.socketInstance =  socket;
        this.username = username;
        LOG.info(username);
        LOG.setLevel(Level.ALL);
        this.ai = AIFactory.getAI(Gui.getInstance().getChosenAI());
        LOG.info("Ai-Strategy: " + Gui.getInstance().getChosenAI());
        addEventListeners();
    }

    /**
     * Adds the event listeners for our socket instance
     * to that instance and directly implements them
     *
     * @see <a href="https://github.com/Nope-Cardgame/Doku/blob/main/Schnittstellen/Schnittstellen.md">
     *     Schnittstellen Dokumentation</a>
     */
    private void addEventListeners() {
        socketInstance.on("gameState", objects -> {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            if(game != null) {
                Gui.getInstance().refresh(game, false);
            }
            LOG.info("Received game-state");
            try {
                handleGameState(objects);
            } catch (InterruptedException ignored) {
            }
        });

        socketInstance.on("error", args1 -> {
            LOG.severe("error: " +Arrays.toString(args1));
        });

        socketInstance.on("eliminated", args1 -> {
            ComponentPainter.setEliminated(true);
            LOG.info("eliminated: " +Arrays.toString(args1));
        });


        socketInstance.on("gameInvite", objects -> {
            socketInstance.emit("ready", handleInvitation(objects, "game"));
        });

        socketInstance.on("gameEnd", args1 -> {
            roundCounter = 0;
            LOG.info("gameEnd: " +Arrays.toString(args1));
            ComponentPainter.setEliminated(false);
            Gui.getInstance().resetGameTable();
        });

        socketInstance.on("tournamentInvite", args1 -> {
            handleTournamentInvitation(args1,"tournament");
        });

        socketInstance.on("tournamentEnd", args1 -> {
            roundCounter = getPlayerCount();
            LOG.info("tournamentEnd: " + Arrays.toString(args1));
        });
    }

    /**
     * handling-method if a game state arrives and this client
     * is the current player.
     *
     * @param objects the message that is provided by the EventListener
     */
    private void handleGameState(Object[] objects) throws InterruptedException {
        game = new Game(((JSONObject) objects[0]).toString());
        LOG.info(game.toJSON());
        // method is only necessary if we are at turn
        currentPlayer = game.getCurrentPlayer().getUsername();

        Gui.getInstance().refresh(game, true);
        Thread.sleep(Gui.getInstance().getDelay());

        if (!game.getState().equals("cancelled") && !game.getState().equals("game_end")) {
            if(game.getCurrentPlayer().getUsername().equals(this.username)) {
                if(game.getState().equals("turn_start")){
                    roundCounter++;
                }
                // calculate the move with instance and emit
                String move = this.ai.calculateNextMove(game);
                Object[] message = new Object[1];
                try {
                    JSONObject moveObject = new JSONObject(move);
                    message[0] = moveObject;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                socketInstance.emit("playAction", message);
            }
        }
    }

    /**
     * handles the game Invitation. The Player
     * can type on the Console whether 'yes' or 'no'
     *
     * @param args1 the message that is provided by the EventListener
     * @return a new message of type Object[] which will be emitted by
     * the socket instance later on
     */
    private static Object[] handleInvitation(Object[] args1, String type) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement gameObject = JsonParser.parseString(args1[0].toString());
        GameInvitation game = new GameInvitation(gson.toJson(gameObject));
        LOG.info("You have been invited to " + type + ", do you want to accept?");
        LOG.info("Accept by default!!!");
        Gui.getInstance().resetGameTable();
        ComponentPainter.setEliminated(false);
        // accept by default
        Ready ready = new Ready(true,type,game.getId());
        Object [] message = new Object[1];
        try {
            JSONObject jsonObject = new JSONObject(ready.toJSON());
            message[0] = jsonObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    /**
     * handles the game Invitation. The Player
     * can type on the Console whether 'yes' or 'no'
     *
     * @param args1 the message that is provided by the EventListener
     * @return a new message of type Object[] which will be emitted by
     * the socket instance later on
     */
    private void handleTournamentInvitation(Object[] args1, String type) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONObject jsonObject = (JSONObject) args1[0];
        JsonElement jsonElement = JsonParser.parseString(jsonObject.toString());
        Tournament tournament = new Tournament(jsonObject.toString());
        LOG.info(gson.toJson(jsonElement));
        LOG.info("You have been invited to " + type + ", do you want to accept?");
        LOG.info("Accept by default!!!");
        Gui.getInstance().resetGameTable();
        ComponentPainter.setEliminated(false);
        // accept by default
        Ready ready = new Ready(true,type,tournament.getId());
        Object [] message = new Object[1];
        try {
            jsonObject = new JSONObject(ready.toJSON());
            message[0] = jsonObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        LOG.info("Everying successful with tournament");
        this.socketInstance.emit("ready",message);
    }

    public static int getRoundCounter() {
        return roundCounter;
    }

    public static String getCurrentPlayer() {
        return currentPlayer;
    }

    public static int getPlayerCount(){
        if(game != null) {
            return game.getPlayers().size();
        }
        return 1;
    }
}