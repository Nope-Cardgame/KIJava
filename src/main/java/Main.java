import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws JSONException, URISyntaxException, IOException, InterruptedException {
        Rest rest = new Rest();

        ConnectionHandler newInstance = new ConnectionHandler();

        WebTokenReceiver webTokenReceiver = WebTokenReceiver.addUserData();
        String token = webTokenReceiver.createWebToken();

        Map<String, String> map = Collections.singletonMap("token", token);
        IO.Options options = IO.Options.builder().setAuth(map).build();
        Socket socket = IO.socket(Constants.DOMAIN.get(), options);
        options.forceNew = true;

        newInstance.connect(socket);
        //example
        rest.request(Constants.GET_USER_CONNECTIONS.get(), token, Rest.RequestType.GET);
    }
}
