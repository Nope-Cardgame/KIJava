package logic;

import event_handling.ServerEventHandler;
import io.socket.client.IO;
import io.socket.client.Socket;
import logic.Constants;
import org.json.JSONException;
import view.Gui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws JSONException, URISyntaxException, IOException, InterruptedException {
        Rest rest = new Rest();
        Gui.getInstance();
    }

    public static boolean connect(String username, String password) {
        ConnectionHandler newInstance = new ConnectionHandler();
        WebTokenReceiver webTokenReceiver = new WebTokenReceiver(Constants.POST_SIGN_IN.get(), username, password);
        String token = null;

        try {
            token = webTokenReceiver.createWebToken();
        } catch (IOException | JSONException exception){
            return false;
        }

        Map<String, String> map = Collections.singletonMap("token", token);
        IO.Options options = IO.Options.builder().setAuth(map).build();
        Socket socket = null;

        try {
            socket = IO.socket(Constants.DOMAIN.get(), options);
        } catch (URISyntaxException exception){
            return false;
        }

        options.forceNew = true;

        // Connect the Socket
        try {
            newInstance.connect(socket);
        } catch (InterruptedException exception){
            return false;
        }

        ServerEventHandler serverEventHandler = new ServerEventHandler(socket, username);

        /* example get-request for user connections
        try {
            rest.request(Constants.GET_USER_CONNECTIONS.get(), token, Rest.RequestType.GET);
        } catch (IOException exception){
            return false;
        }
         */

        return true;
    }


}
