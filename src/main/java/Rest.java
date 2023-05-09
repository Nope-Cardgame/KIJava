import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class Rest {

    private static final Logger LOG = NopeLogger.getLogger(Rest.class.getSimpleName());

    public void get_Request(String urlString, String token) throws IOException {

        LOG.info("current Connection: \n" + urlString);

        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Invoke HTTP connection with POST-Request and application/json for sending
        // element and web token
        con.setRequestMethod("GET");
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
}
