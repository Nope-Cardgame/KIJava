package view;

import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Gui extends JFrame {
    private static Gui INSTANCE;

    private Card frontCard;
    private static List<NumberCard> ownCards = new ArrayList<NumberCard>();

    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }



    private JLabel message = new JLabel("Deine Handkarten");
   private ShowCards showCards ;



    public Gui () {
        getOwnCards().add(new NumberCard("number", 1, Arrays.asList("red", "blue"), "card1"));
        getOwnCards().add(new NumberCard("number", 3, List.of("yellow"), "card2"));
        getOwnCards().add(new NumberCard("number", 2, List.of("blue"), "card3"));
        getOwnCards().add(new NumberCard("number", 1,  Arrays.asList("turquoise", "red"), "card4"));
        getOwnCards().add(new NumberCard("number", 3, Arrays.asList("yellow", "turquoise"), "card5"));
        setVisible(true);
        setBounds(50, 50, 600, 600);
        getMessage().setBounds(100,150,420,120);
        add(message);
        showCards= new ShowCards();
        showCards.setBounds(20,240,550,200);
        showCards.setVisible(true);
        add(showCards);
        add(new JLabel());

    }

    public static List<NumberCard> getOwnCards() {
        return ownCards;
    }

    public void refresh(List<NumberCard> ownCards) {
        Gui.ownCards = ownCards;
        showCards.repaint();
    }

    public JLabel getMessage() {
        return message;
    }

}

