package logic;

import event_handling.ServerEventHandler;
import io.socket.client.IO;
import io.socket.client.Socket;
import logic.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import view.Gui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

public class Main {

    private static Rest rest;
    private static String token;
    private static String username_global;

    public static void main(String[] args) throws JSONException, URISyntaxException, IOException, InterruptedException {
        rest = new Rest();
        Gui.getInstance();

        if(!UserdataFileReader.isEmpty()){
            String[] userData = UserdataFileReader.getUserData();
            Gui.getInstance().setUsernameTextfield(userData[0]);
            Gui.getInstance().setPasswordfield(userData[1]);
        }
    }

    public static boolean connect(String username, String password) {
        ConnectionHandler newInstance = new ConnectionHandler();
        WebTokenReceiver webTokenReceiver = new WebTokenReceiver(Constants.POST_SIGN_IN.get(), username, password);

        username_global = username;

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

        return true;
    }

    public static String getToken() {
        return token;
    }

    public static String getUsername_global() {
        return username_global;
    }

    public static String findMySocketID() throws JSONException {

        String output = null;

        try {
            output = rest.requestWithReturn(Constants.GET_USER_CONNECTIONS.get(), token, RequestType.GET);
        } catch (IOException ignored){
        }

        assert output != null;
        JSONArray jsonArray = new JSONArray(output);

        String socketId = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject player = jsonArray.getJSONObject(i);
            String usernameTemp = player.getString("username");

            if (usernameTemp.equals(username_global)) {
                socketId = player.getString("socketId");
                break;
            }
        }
        return socketId;
    }
}
