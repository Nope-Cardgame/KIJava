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
    static List<String> blue = List.of("blue");
    static List<String> red = List.of("red");
    static List<String> turquoise = List.of("turquoise");
    static List<String> yellow = List.of("yellow");
    static  List<String> blueTurquoise = Arrays.asList("blue", "turquoise");
    static  List<String> redBlue = Arrays.asList("red", "blue");
    static  List<String> redYellow = Arrays.asList("red", "yellow");
    static  List<String> turquoiseRed = Arrays.asList("turquoise", "red");
    static  List<String> yellowBlue = Arrays.asList("yellow", "blue");
    static  List<String> yellowTurquoise = Arrays.asList("yellow", "turquoise");

    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }



    private JLabel message = new JLabel("Text Text Text");
   private ShowCards showCards;



    public Gui () {
        getOwnCards().add(new NumberCard("number", 1, redBlue, "card1"));
        getOwnCards().add(new NumberCard("number", 3, yellow, "card2"));
        getOwnCards().add(new NumberCard("number", 2, blue, "card3"));
        getOwnCards().add(new NumberCard("number", 1, turquoise, "card4"));
        getOwnCards().add(new NumberCard("number", 3, redYellow, "card5"));
        setVisible(true);
        setBounds(50, 50, 600, 600);
        showCards= new ShowCards();

        getMessage().setBounds(120,150,420,120);
        add(message);
        showCards.setBounds(200,200,200,200);
        showCards.setVisible(true);
        add(showCards);
        add(new JLabel());


    }

    public static List<NumberCard> getOwnCards() {
        return ownCards;
    }

    public static void setOwnCards(List<NumberCard> ownCards) {
        Gui.ownCards = ownCards;
    }


    public JLabel getMessage() {
        return message;
    }

    public void setMessage(JLabel message) {
        this.message = message;
    }
}

