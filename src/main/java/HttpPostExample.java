import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpPostExample {
    private static final Logger LOG = Logger.getLogger(HttpPostExample.class.getSimpleName());
    public static void main(String[] args) throws IOException, JSONException {
        LOG.setLevel(Level.INFO);
        String urlString = "http://nope.ddns.net/api/signin";
        String body = """
                {
                    "username":"Aremju",
                    "password":"thisisastring"
                }""";

        LOG.info("JSON-String: \n" + body);

        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept","application/json");

        con.setDoOutput(true);

        //Send request
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.close();

        int responseCode = con.getResponseCode();

        LOG.info("Response Code: \n" + responseCode);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            LOG.info("Response Body: \n" + response);
        }

    }
}
