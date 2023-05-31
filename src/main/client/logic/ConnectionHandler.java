package logic;

import io.socket.client.Socket;
import logging.NopeLogger;
import view.Gui;

import java.util.logging.Logger;

public class ConnectionHandler {
    private static final Logger LOG = NopeLogger.getLogger(ConnectionHandler.class.getSimpleName()); // logger of the class

    public void connect(Socket mySocket) throws InterruptedException {

        // client connects to server
        mySocket.connect();

        // prints out that the client is connected or disconnected or an error appeared
        mySocket.on(Socket.EVENT_CONNECT, args1 -> {
            LOG.info("Connection to server established.");
            Gui.getInstance().setTitle("Nope-Client Java (connected to URL " + Constants.DOMAIN.get() +")");
        });

        mySocket.on(Socket.EVENT_CONNECT_ERROR, args2 -> {
            LOG.severe("An issue appeared during the connection to the server.");
            System.out.println(args2[0]);
            Gui.getInstance().setTitle("Nope-Client Java (disconnected)");
        });

        mySocket.on(Socket.EVENT_DISCONNECT, args3 -> {
            LOG.info("Disconnected from the server.");
            Gui.getInstance().setTitle("Nope-Client Java (disconnected)");
        });

        // waits until the client is connected
        while (!mySocket.connected()) {
            Thread.sleep(100);
        }
    }
}
