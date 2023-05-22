package view;

import command.ActionHandler;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Gui extends JFrame {
    private static Gui INSTANCE;

    private NumberCard frontCard = new NumberCard("number", 1, Arrays.asList("red", "blue"), "card1");
    private List<NumberCard> ownCards = new ArrayList<NumberCard>(); // the cards on the hand
    private JTextField usernameTextfield = new JTextField(); // the user can type in his name
    private JLabel usernameLabel = new JLabel("User name:"); // label to descibe
    private JLabel passwortLabel = new JLabel("Password:");// label to descibe
    private JPasswordField passwordfield = new JPasswordField(); //passwordfield to not show pw
    private JButton loginButton = new JButton("Log in"); // button for login
    private JButton savaLoginData = new JButton("Save Data"); //button to save login data in txt document
    private ActionHandler act = new ActionHandler(); // for the buttons
    private JTable table = new JTable(); //shows all actions of game in a list
    private ShowCards showCards = new ShowCards();// jpanel showing the cards with picutes
    DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nr.", "Player", "Action"});
    JScrollPane scroll= new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }

    public Gui () {
        getOwnCards().add(new NumberCard("number", 1, Arrays.asList("red", "blue"), "card1"));
        getOwnCards().add(new NumberCard("number", 3, List.of("yellow"), "card2"));

        showCards.setBounds(20,240,550,300); //showing the cards
        showCards.setVisible(true);
        add(showCards);
        scroll.setVisible(false); //scroll bar for game table
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.setEnabled(true);
        table.setPreferredScrollableViewportSize(new Dimension(450,63));
        table.setFillsViewportHeight(true);
        model = (DefaultTableModel) table.getModel();
        scroll.setBounds(10,0,550,200);
        scroll.setViewportView(table);
        add(scroll);

        loginButton.setBounds(250,30,120,30);
        loginButton.setVisible(true); // to log in
        loginButton.addActionListener(act);
        add(loginButton); // button for login
        savaLoginData.setBounds(250,70,120,30);
        savaLoginData.setVisible(true); // to save the user name and passwort
        savaLoginData.addActionListener(act);
        add(savaLoginData); // button to save date in txt
        usernameTextfield.setBounds(120,30,120,30);
        usernameTextfield.setVisible(true);
        add(usernameTextfield);

        passwordfield.setBounds(120,70,120,30);
        passwordfield.setVisible(true);
        add(passwordfield); // pw is not visible for user
        passwortLabel.setBounds(10,70,120,30);
        passwortLabel.setVisible(true);
        add(passwortLabel);
        usernameLabel.setBounds(10,30,120,30);
        usernameLabel.setVisible(true);
        add(usernameLabel);
        add(new JLabel());
        setSize(700,700); //square size
        setVisible(true);


    }

    public List<NumberCard> getOwnCards() {
        return ownCards;
    }

    /**
     * update gui  with current cards
     * @param ownCards
     */
    public void refresh(List<NumberCard> ownCards) {
        ownCards = ownCards;
        showCards.repaint();
    }
    public NumberCard getFrontCard(){return frontCard;}
    public String getPasswort(){return passwordfield.getText();}
    public String getUsername(){return usernameTextfield.getText();}
    public JPasswordField getPasswordfield(){return passwordfield;}
    public JTextField getUsernameTextfield(){return usernameTextfield;}
    public JLabel getUsernameLabel(){return usernameLabel;}
    public  JLabel getPasswortLabel(){return passwortLabel;}
    public JButton getLoginButton(){return loginButton;}
    public JButton getSavaLoginData(){return savaLoginData;}
    public JTable getTable(){return table;}

    public JScrollPane getScroll(){return scroll;}


}

