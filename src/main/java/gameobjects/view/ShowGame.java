package gameobjects.view;
import javax.swing.*;
import gameobjects.cards.Card;
import java.awt.*;
import java.util.ArrayList;


public final class ShowGame extends JPanel{

    private ArrayList<Card> ownCards = new ArrayList<Card>();
    private Card demand;
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g.setColor(Color.WHITE);
        g.drawRect(0,0,200,200);
    }
    public ArrayList<Card> getOwnCards() {
        return ownCards;
    }

    public void setOwnCards(ArrayList<Card> ownCards) {
        this.ownCards = ownCards;
    }

    public Card getDemand() {
        return demand;
    }

    public void setDemand(Card demand) {
        this.demand = demand;
    }
}
