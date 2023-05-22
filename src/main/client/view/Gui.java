package view;

import command.ActionHandler;
import gameobjects.Game;
import gameobjects.cards.ActionCard;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Gui extends JFrame {
    private static Gui INSTANCE;

    private Card initialTopCard = new NumberCard("number", 1, Arrays.asList("red", "blue"), "card1");
    private List<Card> discardPile = new ArrayList<Card>(); // the cards on the hand
    private JTextField usernameTextfield = new JTextField(); // the user can type in his name
    private JLabel usernameLabel = new JLabel("User name:"); // label to descibe
    private JLabel passwortLabel = new JLabel("Password:");// label to descibe
    private JPasswordField passwordfield = new JPasswordField(); //passwordfield to not show pw
    private JButton loginButton = new JButton("Log in"); // button for login
    private JButton savaLoginData = new JButton("Save Data"); //button to save login data in txt document
    private ActionHandler act = new ActionHandler(); // for the buttons
    private JTable table = new JTable(); //shows all actions of game in a list
    private ShowCards showCards = new ShowCards();// jpanel showing the cards with picutes
    private JLayeredPane gamePanel;
    DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nr.", "Player", "Action"});
    JScrollPane scroll= new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }

    public Gui () {
        getDiscardPile().add(new NumberCard("number", 1, Arrays.asList("red", "green", "yellow","blue"), "card1"));
        getDiscardPile().add(new ActionCard("action",  List.of("red", "green", "yellow","blue"),"reset"));
        getDiscardPile().add(new NumberCard("number", 3, List.of("red"), "card3"));
        getDiscardPile().add(new NumberCard("number", 1, List.of("turquoise"), "card1"));

        paintGamePanel(false);

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
        setSize(585,805); //square size
        setVisible(true);
        setResizable(false);

        setIconImage(new ImageIcon("cardimages\\image_icon.png").getImage());
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     * update gui  with current cards
     * @param game
     */
    public void refresh(Game game) {
        discardPile = game.getDiscardPile();
        initialTopCard = game.getInitialTopCard();
        paintGamePanel(true);
        showCards.repaint();
    }

    public void paintGamePanel(boolean visibility){
        BackgroundImagePanel background = new BackgroundImagePanel();

        try {
            Image backgroundImage = ImageIO.read(new File("cardimages\\background.png"));
            background.setImage(backgroundImage);
        } catch (IOException ignored){
        }

        background.setBounds(0,0,580,580);

        showCards.setBounds(120,100,550,300); //showing the cards
        showCards.setOpaque(false);

        gamePanel = new JLayeredPane();
        gamePanel.setBounds(0, 200, 580, 580);

        gamePanel.add(background, 1);
        gamePanel.add(showCards, 0);

        gamePanel.setVisible(visibility);

        add(gamePanel);
    }

    public void setUsernameTextfield(String userName){
        getUsernameTextfield().setText(userName);
    }

    public void setPasswordfield(String password){
        getPasswordfield().setText(password);
    }

    public JLayeredPane getGamePanel(){
        return gamePanel;
    }

    public Card getInitialTopCard(){return initialTopCard;}
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

