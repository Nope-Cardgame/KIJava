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

    private static final Logger LOG = NopeLogger.getLogger(WebTokenReceiver.class.getSimpleName());
    private final String username;
    private final String password;
    private final String urlString;

    public WebTokenReceiver(String urlString, String userName, String password){
        this.urlString = urlString;
        this.username = userName;
        this.password = password;
    }

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
