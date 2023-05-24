package logic;

import logging.NopeLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class Rest {

    private static final Logger LOG = NopeLogger.getLogger(Rest.class.getSimpleName());

    /**
     * methode to communicate with post- or get-request with the server without additional id
     *
     * @param urlString
     * @param token
     * @param requestType
     * @throws IOException
     */
    public void request(String urlString, String token, RequestType requestType) throws IOException {

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
    public void request(String urlString, String token, RequestType requestType, int id) throws IOException {

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
    public String requestWithReturn(String urlString, String token, RequestType requestType) throws IOException {

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

    public void invitePlayer(String[] players, String[] socketIDs) throws IOException, JSONException {
        LOG.info("current Connection: \n " + Constants.POST_CREATE_GAME);

        URL obj = new URL(Constants.POST_CREATE_GAME.get());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke HTTP connection with POST-Request and application/json for sending
        // element and web token
        con.setRequestMethod(RequestType.POST.toString());
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + Main.getToken());

        con.setDoOutput(true);

        JSONArray playersArray = new JSONArray();

        for (int i = 0; i < players.length; i++) {
            JSONObject playerObject = new JSONObject();
            playerObject.put("username", players[i]);
            playerObject.put("socketId", socketIDs[i]);
            playersArray.put(playerObject);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("players", playersArray);

        // Send request to the server with its stream
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(String.valueOf(playersArray));
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
