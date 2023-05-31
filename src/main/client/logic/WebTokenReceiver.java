package logic;

import logging.NopeLogger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class WebTokenReceiver {

    private static final Logger LOG = NopeLogger.getLogger(WebTokenReceiver.class.getSimpleName()); // logger of the class
    private final String username; // stores the given name of the connected client
    private final String password; // stores the password of the connected client
    private final String urlString; // stores the url the client is connecting to

    /**
     * constructor of the class WebtokenReceiver
     *
     * @param urlString
     * @param userName
     * @param password
     */
    public WebTokenReceiver(String urlString, String userName, String password){
        this.urlString = urlString;
        this.username = userName;
        this.password = password;
    }

    /**
     * method to create a webtoken with the username, password and the url
     *
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public String createWebToken() throws IOException, JSONException {

        // This is the body we need to send for sign in and signup
        JSONObject userData = new JSONObject();
        userData.put("username", this.username);
        userData.put("password", this.password);

        LOG.info("JSON-String: \n" + userData);

        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke HTTP connection with POST-Request and application/json for sending
        // element and web token
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept","application/json");

        con.setDoOutput(true);

        // Send request to the server with its stream
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(String.valueOf(userData));
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
        JSONObject object = new JSONObject(response.toString());
        return object.getString("jsonwebtoken");
    }
}
