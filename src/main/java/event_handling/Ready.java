package event_handling;

import com.google.gson.Gson;
import gameobjects.IJsonable;

public class Ready implements IJsonable {
    private boolean accept;
    private String type;
    private String inviteId;

    /**
     * Standard-Constructor for a ready-object
     *
     * @param accept true if game or tournament accepted, false otherwise
     * @param type type of Ready (either "game" or "tournament")
     * @param inviteId the id of game or tournament
     */
    public Ready(boolean accept, String type, String inviteId) {
        this.accept = accept;
        this.type = type;
        this.inviteId = inviteId;
    }

    /**
     * Creates a ready-object using a json-String
     *
     * @param jsonString jsonString that must be valid
     */
    public Ready(String jsonString) {
        Gson gson = new Gson();
        this.accept = gson.fromJson(jsonString,getClass()).isAccept();
        this.type = gson.fromJson(jsonString,getClass()).getType();
        this.inviteId = gson.fromJson(jsonString,getClass()).getInviteId();
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }
}
