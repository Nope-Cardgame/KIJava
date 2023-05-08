import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

public class ConnectionHandler {
    private static final Logger LOG = Logger.getLogger(ConnectionHandler.class.getSimpleName());

    public static void main(String[] args) throws URISyntaxException, JSONException, InterruptedException, IOException {
        // Set JWT as an extra header in the options

        TokenReceiver tokenReceiver = new TokenReceiver(Constants.POST_SIGN_IN.get(), Constants.USERNAME.get(),Constants.PASSWORD.get());

        Map<String, String> map = Collections.singletonMap("token", tokenReceiver.getWebToken());
        IO.Options options = IO.Options.builder().setAuth(map).build();
        Socket socket = IO.socket(Constants.DOMAIN.get(), options);

        options.forceNew = true;
        // Verbindung zum Server herstellen
        socket.connect();

        // Listener für das "connect"-Event registrieren
        socket.on(Socket.EVENT_CONNECT, args14 -> {
            System.out.println("Connected to server!");
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, args12 -> {
            LOG.severe("It didn't work");
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
