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
    private Socket socketInstance;
    private String username;
    private IArtificialIntelligence ai;
    private static final Logger LOG = NopeLogger.getLogger(ServerEventHandler.class.getSimpleName());

    public ServerEventHandler(Socket socket, String username) {
        this.socketInstance =  socket;
        this.username = username;
        LOG.setLevel(Level.ALL);
        this.ai = AIFactory.getAI(this.username);
        addEventListeners();
    }

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
            socketInstance.emit("ready",handleInvitation(objects));
        });

        socketInstance.on("gameEnd", args1 -> {
            // game end ausgeben
            LOG.info("gameEnd: " +Arrays.toString(args1));
        });

        socketInstance.on("tournamentInvite", args1 -> {
            // TODO: 12.05.2023 Implement Event: tournament invite
            LOG.info("tournamentInvite: " +Arrays.toString(args1));
        });

        socketInstance.on("tournamentEnd", args1 -> {
            // Informationen auf der View ausgeben
            LOG.info("tournamentEnd: " + Arrays.toString(args1));
        });
    }

    private void handleGameState(Object[] objects) {
        Game game = new Game((String) objects[0]);
        if(game.getCurrentPlayer().getUsername().equals(this.username)) {
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
     * Handles an invitation
     *
     * @param args1
     */
    private static Object[] handleInvitation(Object[] args1) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement gameObject = JsonParser.parseString(args1[0].toString());

        GameInvitation game = new GameInvitation(gson.toJson(gameObject));
        LOG.info("You have been invited, do you want to accept?");
        LOG.info("Type 'yes' to do this, and 'no' to not do it...");
        LOG.info("NOTE: The invitation expires within 10 seconds exactly!");
        Scanner sc = new Scanner(System.in);
        String indicator = sc.nextLine();
        Ready ready = new Ready(false,"game",game.getId());
        if (indicator.equals("yes")) {
            ready.setAccept(true);
        }
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
