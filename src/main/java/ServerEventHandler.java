import io.socket.client.Socket;

import java.util.Arrays;
import java.util.logging.Logger;

public class ServerEventHandler {
    private Socket socketInstance;
    private static final Logger LOG = NopeLogger.getLogger(ServerEventHandler.class.getSimpleName());

    public ServerEventHandler(Socket socket) {
        this.socketInstance =  socket;
        addEventListeners();
    }

    private void addEventListeners() {
        socketInstance.on("gameInvite", args1 -> {
            LOG.info(Arrays.toString(args1));
        });

        socketInstance.on("gameInvite", args2 -> {
            LOG.info(Arrays.toString(args2));
        });

        socketInstance.on("gameInvite", args3 -> {
            LOG.info(Arrays.toString(args3));
        });

        socketInstance.on("gameInvite", args4 -> {
            LOG.info(Arrays.toString(args4));
        });
    }
}
