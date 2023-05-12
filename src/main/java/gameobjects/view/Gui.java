package gameobjects.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import gameobjects.cards.ActionCard;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import gameobjects.command.ActionHandler;

public final class Gui extends JFrame {
    private static Gui INSTANCE = setGui(); // as singleton
    private static Gui setGui(){
        Gui gui = new Gui();
       // gui.getOwnCards().add(new NumberCard())
        return gui;
    }
    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }

    private JTextField usernameInput = new JTextField();
    private JLabel usernameLabel = new JLabel("Username: ");
    private JPasswordField passwortInput= new JPasswordField();
    private JLabel passwortLabel;
    private JButton login = new JButton("Log in");

    private Card demand; // the upper card on the stack
    private ArrayList<Card> ownCards = new ArrayList<Card>();

    private ShowGame showGame = new ShowGame();
    public Gui () {
        ActionListener act = new ActionHandler();
        setBounds(50, 50, 600, 600);
        getUsernameInput().setBounds(100,10,100,30);
        getUsernameInput().setVisible(true);
        add(getUsernameInput());
        getUsernameLabel().setBounds(10,10,100, 30);
        getUsernameLabel().setVisible(true);
        add(getUsernameLabel());
        getPasswortInput().setBounds(100,50,100,30);
        getPasswortInput().setVisible(true);
        add(getPasswortInput());
        setPasswortLabel(new JLabel("Passwort: "));
        getPasswortLabel().setBounds(10,50,100, 30);
        getPasswortLabel().setVisible(true);
        add(getPasswortLabel());
        getLogin().setBounds(200, 10, 100, 30);
        getLogin().setVisible(true);
        getLogin().addActionListener(act);
        add(getLogin());
        showGame.setBounds(300,300,200,200);
        showGame.setVisible(true);
        add(showGame);
        add(new JLabel());
        setVisible(true);
    }

    public JTextField getUsernameInput() {
        return usernameInput;
    }


    public JLabel getUsernameLabel() {
        return usernameLabel;
    }


    public JTextField getPasswortInput() {
        return passwortInput;
    }


    public JLabel getPasswortLabel() {
        return passwortLabel;
    }

    public void setPasswortLabel(JLabel passwortLabel) {
        this.passwortLabel = passwortLabel;
    }

    public JButton getLogin() {
        return login;
    }

    public void setLogin(JButton login) {
        login = login;
    }

    public Card getDemand() {
        return demand;
    }

    public void setDemand(Card demand) {
        this.demand = demand;
    }

    public ArrayList<Card> getOwnCards() {
        return ownCards;
    }

    public void setOwnCards(ArrayList<Card> ownCards) {
        this.ownCards = ownCards;
    }
}

