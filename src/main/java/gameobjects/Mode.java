package gameobjects;

import com.google.gson.Gson;
import gameobjects.Jsonable;

public class Mode implements Jsonable {
    private String name;
    private int numberOfRounds;

    /**
     * Standard Constructor for Mode
     *
     * @param name the name of the Mode
     * @param numberOfRounds the number of Rounds
     */
    public Mode(String name, int numberOfRounds) {
        this.name = name;
        this.numberOfRounds = numberOfRounds;
    }

    /**
     * Creates a Mode using a valid JSON-String
     *
     * @param jsonString mode jsonString that must be valid
     */
    public Mode(String jsonString) {
        Gson gson = new Gson();
        this.name = gson.fromJson(jsonString,getClass()).getName();
        this.numberOfRounds = gson.fromJson(jsonString,getClass()).getNumberOfRounds();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    @Override
    public String toJSON() {
        return new Gson().toJson(this);
    }
}
