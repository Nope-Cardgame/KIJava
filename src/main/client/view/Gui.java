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
import java.util.Objects;

public final class Gui extends JFrame {
    private static Gui INSTANCE;
    private final Rest rest = new Rest();

    private Card initialTopCard = null;
    private List<Card> playerHand = new ArrayList<Card>(); // the cards on the hand
    private final JTextField usernameTextfield = new JTextField(); // the user can type in his name
    private final JLabel usernameLabel = new JLabel("User name:"); // label to descibe
    private final JLabel passwortLabel = new JLabel("Password:");// label to descibe
    private final JLabel yourConnectionLabel = new JLabel();
    private final JPasswordField passwordfield = new JPasswordField(); //passwordfield to not show pw
    private final JButton loginButton = new JButton("Log in"); // button for login
    private final JButton savaLoginData = new JButton("Save Data"); //button to save login data in txt document
    private final JButton reloadPlayerList = new JButton("Reload player list");
    private final JButton addPlayerToInvite = new JButton("Add marked player to list");
    private final JButton removePlayerToInvite = new JButton("Remove marked player from list");
    private final JButton inviteChosenPlayer = new JButton("Invite players to game");
    private final JTable table = new JTable(); //shows all actions of game in a list
    private final JTable playerListTable = new JTable();
    private final JTable addedPlayerToInviteTable = new JTable();
    private final ComponentPainter componentPainter = new ComponentPainter();// jpanel showing the cards with picutes
    private final DefaultTableModel playerListModel = new DefaultTableModel(new Object[][]{}, new String[]{"Playername", "Socket-ID"});
    private final DefaultTableModel addedPlayerToInviteModel = new DefaultTableModel(new Object[][]{}, new String[]{"Added player to invite", "Socket-ID"});

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
        // for the buttons
        ActionHandler act = new ActionHandler();

        componentPainter.setVisible(false);
        componentPainter.setBounds(10,200,560,560); //showing the cards
        componentPainter.setOpaque(false);
        add(componentPainter);

        playerListTable.getTableHeader().setReorderingAllowed(false);
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

        addedPlayerToInviteTable.getTableHeader().setReorderingAllowed(false);
        addedPlayerToInviteTable.setModel(addedPlayerToInviteModel);
        addedPlayerToInviteTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        addedPlayerToInviteTable.setEnabled(true);
        addedPlayerToInviteScroll.setVisible(false);
        addedPlayerToInviteScroll.setBounds(575, 280, 505, 200);
        addedPlayerToInviteScroll.setViewportView(addedPlayerToInviteTable);
        add(addedPlayerToInviteScroll);

        inviteChosenPlayer.setBounds(575, 485, 520, 30);
        inviteChosenPlayer.setVisible(false);
        inviteChosenPlayer.addActionListener(act);
        add(inviteChosenPlayer);

        yourConnectionLabel.setVisible(false);
        yourConnectionLabel.setBounds(575, 735, 520,30);
        add(yourConnectionLabel);

        scroll.setVisible(false); //scroll bar for game table
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nr.", "Player", "Action"});
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
        if(game != null) {
            if (game.getCurrentPlayer().getUsername().equals(Main.getUsername_global())) {
                playerHand = game.getCurrentPlayer().getCards();
            }
            initialTopCard = game.getDiscardPile().get(0);
            componentPainter.repaint();
        }
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
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> socketIds = new ArrayList<>();
        JsonNode jsonNode = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(userdata);

            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode node = jsonNode.get(i);
                if(!node.get("username").asText().equals(Main.getUsername_global())){
                    usernames.add(node.get("username").asText());
                    socketIds.add(node.get("socketId").asText());
                }
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        for(int i = 0; i < Objects.requireNonNull(jsonNode).size()-1; i++){
            Gui.getInstance().addDataToPlayerListModel(usernames.get(i), socketIds.get(i));
        }
    }

    public void setUsernameTextfield(String userName){
        getUsernameTextfield().setText(userName);
    }

    public void setPasswordfield(String password){
        getPasswordfield().setText(password);
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

    public ComponentPainter getShowCards() {
        return componentPainter;
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

    public JLabel getYourConnectionLabel() {
        return yourConnectionLabel;
    }
}

