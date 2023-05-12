import event_handling.ServerEventHandler;
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
        // TODO: 12.05.2023 make username more variable
        ServerEventHandler serverEventHandler = new ServerEventHandler(socket, "Aremju");

        //example
        rest.request(Constants.GET_USER_CONNECTIONS.get(), token, Rest.RequestType.GET);
    }
}
