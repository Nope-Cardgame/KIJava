import io.socket.client.Socket;
import logging.NopeLogger;

import java.util.logging.Logger;

public class ConnectionHandler {
    private static final Logger LOG = NopeLogger.getLogger(ConnectionHandler.class.getSimpleName());

    public ConnectionHandler(){
    }

    public void connect(Socket mySocket) throws InterruptedException {

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

        // Warten bis die Verbindung hergestellt wurde
        while (!mySocket.connected()) {
            Thread.sleep(100);
        }
    }
}
