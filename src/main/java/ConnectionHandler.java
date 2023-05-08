import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class ConnectionHandler {
    private static final Logger LOG = Logger.getLogger(ConnectionHandler.class.getSimpleName());

    public static String getWebToken() throws IOException, JSONException {

        String urlString = "http://nope.ddns.net/api/signin";
        // This is the body we need to send for sign in and signup
        String body = """
                {
                    "username":"Aremju",
                    "password":"thisisastring"
                }""";

        LOG.info("JSON-String: \n" + body);

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
        wr.writeBytes(body);
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
        return (String) object.getString("jsonwebtoken");
    }

    public static void main(String[] args) throws URISyntaxException, JSONException, InterruptedException, IOException {
        // Set JWT as an extra header in the options

        Map<String, String> map = Collections.singletonMap("token", getWebToken());
        IO.Options options = IO.Options.builder().setAuth(map).build();
        Socket socket = IO.socket("http://nope.ddns.net/", options);

        options.forceNew = true;
        // Verbindung zum Server herstellen
        socket.connect();

        // Listener für das "connect"-Event registrieren
        socket.on(Socket.EVENT_CONNECT, args14 -> {
            System.out.println("Connected to server!");
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, args12 -> {
            LOG.severe("It didn't work bitch");
            System.out.println(args12[0]);
        });

        socket.on(Socket.EVENT_DISCONNECT, args13 -> {
            LOG.info("That escalated quickly");
        });

        // Listener für das "chat message"-Event registrieren
        socket.on("gameInvite", args1 -> {
            LOG.info(Arrays.toString(args1));
        });

        // Warten bis die Verbindung hergestellt wurde
        while (!socket.connected()) {
            Thread.sleep(100);
        }
    }
}
