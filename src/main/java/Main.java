import event_handling.ServerEventHandler;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws JSONException, URISyntaxException, IOException, InterruptedException {
        Rest rest = new Rest();

        ConnectionHandler newInstance = new ConnectionHandler();
        String username = "";
        // assume we all have an account
        String token = null;
        while(token == null) {
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Please log in");
                System.out.print("Username: ");
                username = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();
                System.out.println();
                WebTokenReceiver webTokenReceiver = new WebTokenReceiver(Constants.POST_SIGN_IN.get(),username,password);
                token = webTokenReceiver.createWebToken();
            } catch (IOException e) {
                Logger.getLogger(Main.class.getSimpleName()).info("Invalid data, try again");
            }
        }
        // set auth header with token for the socket
        Map<String, String> map = Collections.singletonMap("token", token);
        IO.Options options = IO.Options.builder().setAuth(map).build();
        Socket socket = IO.socket(Constants.DOMAIN.get(), options);
        options.forceNew = true;
        // Connect the Socket
        newInstance.connect(socket);
        ServerEventHandler serverEventHandler = new ServerEventHandler(socket, username);

        //example get-request for user connections
        rest.request(Constants.GET_USER_CONNECTIONS.get(), token, Rest.RequestType.GET);
    }
}
