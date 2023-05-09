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
    private final UserdataFileReader udFileReader = new UserdataFileReader();

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

    public TokenReceiver addUser(){
        TokenReceiver tokenReceiver;
        if(!udFileReader.isEmpty()) {
            String[] userdata = udFileReader.getUserData();
            String username = userdata[0];
            String password = userdata[1];

            tokenReceiver = new TokenReceiver(Constants.POST_SIGN_IN.get(), username, password);
            LOG.info("User " + username + " logged in.");

        } else {
            String[] userdata = udFileReader.addNewUser();
            String username = userdata[0];
            String password = userdata[1];

            tokenReceiver = new TokenReceiver(Constants.POST_SIGN_UP.get(), username, password);
            LOG.info("User " + username + " registered.");
        }

        return tokenReceiver;
    }

    public void connect(Socket mySocket) throws URISyntaxException, JSONException, IOException, InterruptedException {

        // Verbindung zum Server herstellen
        mySocket.connect();

        // Listener für das "connect"-Event registrieren
        mySocket.on(Socket.EVENT_CONNECT, args1 -> {
            LOG.info("Connection to server established.");
        });

        mySocket.on(Socket.EVENT_CONNECT_ERROR, args2 -> {
            LOG.severe("An issue appeared during the connection to the server.");
            System.out.println(args2[0]);
        });

        mySocket.on(Socket.EVENT_DISCONNECT, args3 -> {
            LOG.info("Disconnected from the server.");
        });

        // Listener für das "chat message"-Event registrieren
        mySocket.on("gameInvite", args4 -> {
            LOG.info(Arrays.toString(args4));
        });

        // Warten bis die Verbindung hergestellt wurde
        while (!mySocket.connected()) {
            Thread.sleep(100);
        }
    }
}
