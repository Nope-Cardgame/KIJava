import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import io.jsonwebtoken.Jwts;
import java.util.logging.Logger;

public class HttpPostExample {
    private static final Logger LOG = Logger.getLogger(HttpPostExample.class.getSimpleName());
    public static void main(String[] args) throws IOException, JSONException {

        String urlString = "http://nope.ddns.net/api/signin";
        // This is the body we need to send for signin and signup
        String body = """
                {
                    "username":"Aremju",
                    "password":"thisisastring"
                }""";

        LOG.info("JSON-String: \n" + body);

        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke a HTTP connection with POST-Request and application/json for sending
        // element and webtoken
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

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            // build response and print it
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            LOG.info("Response Body: \n" + response);
        }

    }
}
