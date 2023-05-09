package gameobjects.cards;

public abstract class Card {
    private String type;
    private String name;

    public Card(String type, String name) {
        this.name = name;
        this.type = type;
    }

    public String getCardType() {
        return this.type;
    }

    public void setCardType(String type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
