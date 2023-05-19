package view;

import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Gui extends JFrame {
    private static Gui INSTANCE;

    private NumberCard frontCard = new NumberCard("number", 1, Arrays.asList("red", "blue"), "card1");
    private List<NumberCard> ownCards = new ArrayList<NumberCard>();
    private JTextField usernameTextfield = new JTextField();
    private JLabel usernameLabel = new JLabel("User name:");
    private JLabel passwortLabel = new JLabel("Password:");
    private JPasswordField passwordfield = new JPasswordField();

    private JButton loginButton = new JButton("Log in");
    private JButton savaLoginData = new JButton("Save Data");

    private ShowCards showCards = new ShowCards();
    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }

    public Gui () {
        getOwnCards().add(new NumberCard("number", 1, Arrays.asList("red", "blue"), "card1"));
        getOwnCards().add(new NumberCard("number", 3, List.of("yellow"), "card2"));
        getOwnCards().add(new NumberCard("number", 2, List.of("blue"), "card3"));
        getOwnCards().add(new NumberCard("number", 1,  Arrays.asList("turquoise", "red"), "card4"));
        getOwnCards().add(new NumberCard("number", 3, Arrays.asList("yellow", "turquoise"), "card5"));
        getOwnCards().add(new NumberCard("number", 3, List.of("yellow"), "card2"));
        getOwnCards().add(new NumberCard("number", 2, List.of("blue"), "card3"));
        getOwnCards().add(new NumberCard("number", 1,  Arrays.asList("turquoise", "red"), "card4"));
        getOwnCards().add(new NumberCard("number", 1,  Arrays.asList("turquoise", "red"), "card4"));
        getOwnCards().add(new NumberCard("number", 3, Arrays.asList("yellow", "turquoise"), "card5"));
        getOwnCards().add(new NumberCard("number", 3, List.of("yellow"), "card2"));
        getOwnCards().add(new NumberCard("number", 2, List.of("blue"), "card3"));
        getOwnCards().add(new NumberCard("number", 1,  Arrays.asList("turquoise", "red"), "card4"));
        setVisible(true);
        setBounds(50, 50, 600, 600);

        showCards.setBounds(20,240,550,300); //showing the cards
        showCards.setVisible(true);
        loginButton.setBounds(250,30,120,30);
        loginButton.setVisible(true); // to log in
        add(loginButton);
        savaLoginData.setBounds(250,70,120,30);
        savaLoginData.setVisible(true); // to save the user name and passwort
        add(savaLoginData);
        usernameTextfield.setBounds(120,30,120,30);
        usernameTextfield.setVisible(true);
        add(usernameTextfield);
        usernameLabel.setBounds(10,30,120,30);
        usernameLabel.setVisible(true);
        add(usernameLabel);
        passwordfield.setBounds(120,70,120,30);
        passwordfield.setVisible(true);
        add(passwordfield);
        passwortLabel.setBounds(10,70,120,30);
        passwortLabel.setVisible(true);
        add(passwortLabel);
        add(showCards);
        add(new JLabel());
    }

    public List<NumberCard> getOwnCards() {
        return ownCards;
    }

    public void refresh(List<NumberCard> ownCards) {
        ownCards = ownCards;
        showCards.repaint();
    }
    public NumberCard getFrontCard(){return frontCard;}
    public String getPasswort(){return passwordfield.getText();}
    public String getUsername(){return usernameTextfield.getText();}

}

