package logic;

import logging.NopeLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import view.Gui;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class Rest {

    private static final Logger LOG = NopeLogger.getLogger(Rest.class.getSimpleName()); // logger of the class

    /**
     * methode to communicate with post- or get-request with the server without additional id
     *
     * @param urlString
     * @param token
     * @param requestType
     * @throws IOException
     */
    public static void request(String urlString, String token, RequestType requestType) throws IOException {

        LOG.info("current Connection: \n" + urlString);

        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke HTTP connection with POST-Request and application/json for sending
        // element and web token
        con.setRequestMethod(requestType.toString());
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + token);

        con.setDoOutput(true);

        int responseCode = con.getResponseCode();

        LOG.info("Response Code: \n" + responseCode);
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            // build response and print it

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            LOG.info("Response Body: \n" + response);
        }
    }

    /**
     * methode to communicate with post- or get-request with the server with additional id
     * (used for displaying specific games or tournaments
     *
     * @param urlString
     * @param token
     * @param requestType
     * @param id
     * @throws IOException
     */
    public static void request(String urlString, String token, RequestType requestType, int id) throws IOException {

        LOG.info("current Connection: \n" + urlString);

        URL obj = new URL(urlString + id + "/");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke HTTP connection with POST-Request and application/json for sending
        // element and web token
        con.setRequestMethod(requestType.toString());
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + token);

        con.setDoOutput(true);

        int responseCode = con.getResponseCode();

        LOG.info("Response Code: \n" + responseCode);
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            // build response and print it

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            LOG.info("Response Body: \n" + response);
        }
    }

    /**
     * methode to communicate with post- or get-request with the server without additional id
     *
     * @param urlString
     * @param token
     * @param requestType
     * @throws IOException
     */
    public static String requestWithReturn(String urlString, String token, RequestType requestType) throws IOException {

        LOG.info("current Connection: \n" + urlString);

        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke HTTP connection with POST-Request and application/json for sending
        // element and web token
        con.setRequestMethod(requestType.toString());
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + token);

        con.setDoOutput(true);

        int responseCode = con.getResponseCode();

        LOG.info("Response Code: \n" + responseCode);
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            // build response and print it

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            LOG.info("Response Body: \n" + response);
        }
        return String.valueOf(response);
    }

    /**
     * method to invite the selected players to a new game
     *
     * @param players
     * @param socketIDs
     * @throws IOException
     * @throws JSONException
     */
    public static void invitePlayerToGame(String[] players, String[] socketIDs) throws IOException, JSONException {
        LOG.info("current Connection: \n" + Constants.POST_CREATE_GAME);

        URL obj = new URL(Constants.POST_CREATE_GAME.get());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke HTTP connection with POST-Request and application/json for sending
        // element and web token
        con.setRequestMethod(RequestType.POST.toString());
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + Main.getToken());

        con.setDoOutput(true);

        // creates a json-file with the settings, your connection and the selected players
        JSONArray playersArray = new JSONArray();

        JSONObject player = new JSONObject();
        player.put("username", Main.getUsername_global());
        player.put("socketId", Main.findMySocketID());
        playersArray.put(player);

        for (int i = 0; i < players.length; i++) {
            JSONObject playerObject = new JSONObject();
            playerObject.put("username", players[i]);
            playerObject.put("socketId", socketIDs[i]);
            playersArray.put(playerObject);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("noActionCards", Gui.getInstance().getNoActionCardsComboBox().getSelectedItem());
        jsonObject.put("noWildCards", Gui.getInstance().getNoWildCardsComboBox().getSelectedItem());
        jsonObject.put("oneMoreStartCards", Gui.getInstance().getOneMoreStartCardComboBox());

        jsonObject.put("players", playersArray);

        // Send request to the server with its stream
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(String.valueOf(jsonObject));
        wr.close();

        int responseCode = con.getResponseCode();

        LOG.info("Response Code: \n" + responseCode);
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            // build response and print it

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            LOG.info("Response Body: \n" + response);
        }
    }

    public static void invitePlayerToTournament(String[] players, String[] socketIDs) throws IOException, JSONException {
        LOG.info("current Connection: \n" + Constants.POST_CREATE_TOURNAMENT);

        URL obj = new URL(Constants.POST_CREATE_TOURNAMENT.get());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke HTTP connection with POST-Request and application/json for sending
        // element and web token
        con.setRequestMethod(RequestType.POST.toString());
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + Main.getToken());

        con.setDoOutput(true);

        // creates a json-file with the settings, your connection and the selected players
        JSONArray playersArray = new JSONArray();

        JSONObject participants = new JSONObject();
        participants.put("username", Main.getUsername_global());
        participants.put("socketId", Main.findMySocketID());
        playersArray.put(participants);

        for (int i = 0; i < players.length; i++) {
            JSONObject playerObject = new JSONObject();
            playerObject.put("username", players[i]);
            playerObject.put("socketId", socketIDs[i]);
            playersArray.put(playerObject);
        }

        JSONObject mode = new JSONObject();
        mode.put("name", "round-robin");
        mode.put("numberOfRounds", 5);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("noActionCards", Gui.getInstance().getNoActionCardsComboBox().getSelectedItem());
        jsonObject.put("noWildCards", Gui.getInstance().getNoWildCardsComboBox().getSelectedItem());
        jsonObject.put("oneMoreStartCards", Gui.getInstance().getOneMoreStartCardComboBox());

        jsonObject.put("mode", mode);
        jsonObject.put("participants", playersArray);

        // Send request to the server with its stream
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(String.valueOf(jsonObject));
        wr.close();

        int responseCode = con.getResponseCode();

        LOG.info("Response Code: \n" + responseCode);
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            // build response and print it

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            LOG.info("Response Body: \n" + response);
        }
    }
}
