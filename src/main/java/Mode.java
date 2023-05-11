import com.google.gson.Gson;
import gameobjects.Jsonable;

public class Mode implements Jsonable {
    private String name;
    private int numberOfRounds;

    public Mode(String name, int numberOfRounds) {
        this.name = name;
        this.numberOfRounds = numberOfRounds;
    }

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
