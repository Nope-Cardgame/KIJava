import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler {
    private static final Logger LOG = Logger.getLogger(ConnectionHandler.class.getSimpleName());
    private static final UserdataFileReader udFileReader = new UserdataFileReader();

    private TokenReceiver tokenReceiver;

    public ConnectionHandler(){
        initLogger();
    }

    private void initLogger() {
        LOG.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new ClientFormatter());
        LOG.addHandler(consoleHandler);
    }

    public void connect() throws URISyntaxException, JSONException, IOException, InterruptedException {

        if(!udFileReader.isEmpty()) {
            String[] userdata = udFileReader.getUserData();
            String username = userdata[0];
            String password = userdata[1];

            this.tokenReceiver = new TokenReceiver(Constants.POST_SIGN_IN.get(), username, password);
            LOG.info("User " + username + " logged in.");

        } else {
            String[] userdata = udFileReader.addNewUser();
            String username = userdata[0];
            String password = userdata[1];

            this.tokenReceiver = new TokenReceiver(Constants.POST_SIGN_UP.get(), username, password);
            LOG.info("User " + username + " registered.");
        }

        Map<String, String> map = Collections.singletonMap("token", this.tokenReceiver.getWebToken());
        IO.Options options = IO.Options.builder().setAuth(map).build();
        Socket socket = IO.socket(Constants.DOMAIN.get(), options);

        options.forceNew = true;
        // Verbindung zum Server herstellen
        socket.connect();

        // Listener für das "connect"-Event registrieren
        socket.on(Socket.EVENT_CONNECT, args1 -> {
            LOG.info("Connection to server established.");
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, args2 -> {
            LOG.severe("An issue appeared during the connection to the server.");
            System.out.println(args2[0]);
        });

        socket.on(Socket.EVENT_DISCONNECT, args3 -> {
            LOG.info("Disconnected from the server.");
        });

        // Listener für das "chat message"-Event registrieren
        socket.on("gameInvite", args4 -> {
            LOG.info(Arrays.toString(args4));
        });

        // Warten bis die Verbindung hergestellt wurde
        while (!socket.connected()) {
            Thread.sleep(100);
        }
    }

    public TokenReceiver getTokenReceiver() {
        return tokenReceiver;
    }
}
