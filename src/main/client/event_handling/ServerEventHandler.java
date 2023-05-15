package event_handling;

import ai.AIFactory;
import ai.IArtificialIntelligence;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gameobjects.Game;
import io.socket.client.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.socket.emitter.Emitter;
import logging.NopeLogger;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerEventHandler {
    private final Socket socketInstance;
    private final String username;
    private final IArtificialIntelligence ai;
    private static final Logger LOG = NopeLogger.getLogger(ServerEventHandler.class.getSimpleName());

    /**
     * Creates a ServerEventHandler instance
     *
     * @param socket the socket instance
     * @param username the username of the user that is logged in
     */
    public ServerEventHandler(Socket socket, String username) {
        this.socketInstance =  socket;
        this.username = username;
        LOG.setLevel(Level.ALL);
        this.ai = AIFactory.getAI(this.username);
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
            LOG.info("Received gamestate");
            // TODO: 12.05.2023 update view
            handleGameState(objects);
        });

        socketInstance.on("error", args1 -> {
            LOG.severe("error: " +Arrays.toString(args1));
        });

        socketInstance.on("eliminated", args1 -> {
            LOG.severe("eliminated: " +Arrays.toString(args1));
        });


        socketInstance.on("gameInvite", objects -> {
            socketInstance.emit("ready", handleInvitation(objects));
        });

        socketInstance.on("gameEnd", args1 -> {
            LOG.info("gameEnd: " +Arrays.toString(args1));
        });

        socketInstance.on("tournamentInvite", args1 -> {
            // TODO: 12.05.2023 Implement Event: tournament invite
            LOG.info("tournamentInvite: " +Arrays.toString(args1));
        });

        socketInstance.on("tournamentEnd", args1 -> {
            LOG.info("tournamentEnd: " + Arrays.toString(args1));
        });
    }

    /**
     * handling-method if a game state arrives and this client
     * is the current player.
     *
     * @param objects the message that is provided by the EventListener
     */
    private void handleGameState(Object[] objects) {
        Game game = new Game((String) objects[0]);
        // method is only necessary if we are at turn
        if(game.getCurrentPlayer().getUsername().equals(this.username)) {
            // calculate the move with ai instance and emit
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

    /**
     * handles the game Invitation. The Player
     * can type on the Console whether 'yes' or 'no'
     *
     * @param args1 the message that is provided by the EventListener
     * @return a new message of type Object[] which will be emitted by
     * the socket instance later on
     */
    private static Object[] handleInvitation(Object[] args1) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement gameObject = JsonParser.parseString(args1[0].toString());
        GameInvitation game = new GameInvitation(gson.toJson(gameObject));
        LOG.info("You have been invited, do you want to accept?");
        LOG.info("Accept by default!!!");
        // accept by default
        Ready ready = new Ready(true,"game",game.getId());
        Object [] message = new Object[1];
        try {
            JSONObject jsonObject = new JSONObject(ready.toJSON());
            message[0] = jsonObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}
