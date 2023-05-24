package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.ActionHandler;
import gameobjects.Game;
import gameobjects.cards.Card;
import logic.Constants;
import logic.Main;
import logic.RequestType;
import logic.Rest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Gui extends JFrame {
    private static Gui INSTANCE;
    private final Rest rest = new Rest();

    private Card initialTopCard = null;
    private List<Card> playerHand = new ArrayList<Card>(); // the cards on the hand
    private JTextField usernameTextfield = new JTextField(); // the user can type in his name
    private JLabel usernameLabel = new JLabel("User name:"); // label to descibe
    private JLabel passwortLabel = new JLabel("Password:");// label to descibe
    private JPasswordField passwordfield = new JPasswordField(); //passwordfield to not show pw
    private JButton loginButton = new JButton("Log in"); // button for login
    private JButton savaLoginData = new JButton("Save Data"); //button to save login data in txt document
    private JButton reloadPlayerList = new JButton("Reload player list");
    private JButton addPlayerToInvite = new JButton("Add marked player to list");
    private JButton removePlayerToInvite = new JButton("Remove marked player to list");
    private JButton inviteChosenPlayer = new JButton("Invite players to game");
    private ActionHandler act = new ActionHandler(); // for the buttons
    private JTable table = new JTable(); //shows all actions of game in a list
    private JTable playerListTable = new JTable();
    private JTable addedPlayerToInviteTable = new JTable();
    private ShowCards showCards = new ShowCards();// jpanel showing the cards with picutes
    private JLayeredPane gamePanel;
    private DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nr.", "Player", "Action"});
    private DefaultTableModel playerListModel = new DefaultTableModel(new Object[][]{}, new String[]{"Playername", "Socket-ID"});
    private DefaultTableModel addedPlayerToInviteModel = new DefaultTableModel(new Object[][]{}, new String[]{"Added player to invite", "Socket-ID"});

    JScrollPane scroll= new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JScrollPane playerListScroll = new JScrollPane(playerListTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollPane addedPlayerToInviteScroll= new JScrollPane(playerListTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }

    public Gui () {
        showCards.setVisible(false);
        showCards.setBounds(10,200,560,560); //showing the cards
        showCards.setOpaque(false);
        add(showCards);

        playerListTable.setModel(playerListModel);
        playerListTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        playerListTable.setEnabled(true);
        playerListScroll.setVisible(false);
        playerListScroll.setBounds(575,40, 505, 200);
        playerListScroll.setViewportView(playerListTable);
        add(playerListScroll);

        reloadPlayerList.setVisible(false);
        reloadPlayerList.addActionListener(act);
        reloadPlayerList.setBounds(575, 0, 520, 30);
        add(reloadPlayerList);

        addPlayerToInvite.setVisible(false);
        addPlayerToInvite.setBounds(575, 245, 250, 30);
        addPlayerToInvite.addActionListener(act);
        add(addPlayerToInvite);

        removePlayerToInvite.setVisible(false);
        removePlayerToInvite.setBounds(835, 245, 245, 30);
        removePlayerToInvite.addActionListener(act);
        add(removePlayerToInvite);

        addedPlayerToInviteTable.setModel(addedPlayerToInviteModel);
        addedPlayerToInviteTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        addedPlayerToInviteTable.setEnabled(true);
        addedPlayerToInviteScroll.setVisible(false);
        addedPlayerToInviteScroll.setBounds(575, 280, 505, 200);
        addedPlayerToInviteScroll.setViewportView(addedPlayerToInviteTable);
        add(addedPlayerToInviteScroll);

        scroll.setVisible(false); //scroll bar for game table
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.setEnabled(true);
        table.setPreferredScrollableViewportSize(new Dimension(450,63));
        table.setFillsViewportHeight(true);
        model = (DefaultTableModel) table.getModel();
        scroll.setBounds(10,0,560,200);
        scroll.setViewportView(table);
        add(scroll);

        loginButton.setBounds(250,30,120,30);
        loginButton.setVisible(true); // to log in
        loginButton.addActionListener(act);
        add(loginButton); // button for login

        savaLoginData.setBounds(250,70,120,30);
        savaLoginData.setVisible(true); // to save the username and passwort
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

        setSize(1100,805); //square size
        setVisible(true);
        setResizable(false);

        setIconImage(new ImageIcon("cardimages\\image_icon.png").getImage());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * update gui  with current cards
     * @param game
     */
    public void refresh(Game game) {
        playerHand = game.getPlayers().get(0).getCards();
        initialTopCard = game.getDiscardPile().get(0);
        showCards.repaint();
    }

    public void addDataToPlayerListModel(Object... data){
        playerListModel.addRow(data);
    }

    public void addDataToAddedPlayerModel(Object... data){
        addedPlayerToInviteModel.addRow(data);
    }

    public void addUserData(){
        playerListModel.setRowCount(0);

        String userdata;

        try {
            userdata = rest.requestWithReturn(Constants.GET_USER_CONNECTIONS.get(), Main.getToken(), RequestType.GET);
            System.out.println(userdata);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        String[] usernames = new String[0];
        String[] socketIds = new String[0];
        JsonNode jsonNode = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(userdata);

            usernames = new String[jsonNode.size()];
            socketIds = new String[jsonNode.size()];

            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode node = jsonNode.get(i);
                usernames[i] = node.get("username").asText();
                socketIds[i] = node.get("socketId").asText();
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        for(int i = 0; i < jsonNode.size(); i++){
            Gui.getInstance().addDataToPlayerListModel(usernames[i], socketIds[i]);
        }
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

    public List<Card> getPlayerHand() {
        return playerHand;
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
    public JScrollPane getPlayerListScroll(){return playerListScroll;}

    public JButton getReloadPlayerList() {
        return reloadPlayerList;
    }

    public ShowCards getShowCards() {
        return showCards;
    }

    public JButton getAddPlayerToInvite() {
        return addPlayerToInvite;
    }

    public JButton getRemovePlayerToInvite() {
        return removePlayerToInvite;
    }

    public JButton getInviteChosenPlayer() {
        return inviteChosenPlayer;
    }

    public JScrollPane getAddedPlayerToInviteScroll() {
        return addedPlayerToInviteScroll;
    }

    public JTable getPlayerListTable() {
        return playerListTable;
    }

    public JTable getAddedPlayerToInviteTable() {
        return addedPlayerToInviteTable;
    }
}

