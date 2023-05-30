package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.ActionHandler;
import gameobjects.Game;
import gameobjects.cards.Card;
import logging.NopeLogger;
import logic.Constants;
import logic.Main;
import logic.RequestType;
import logic.Rest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class Gui extends JFrame {

    public enum ComponentType {
        LOGIN,
        GAME
    }

    private static final Logger LOG = NopeLogger.getLogger(Gui.class.getSimpleName());
    private static Gui INSTANCE;
    private final Rest rest = new Rest();

    private Card initialTopCard = null;
    private List<Card> playerHand = new ArrayList<Card>(); // the cards on the hand
    private final JTextField usernameTextfield = new JTextField(); // the user can type in his name
    private final JLabel usernameLabel = new JLabel("User name:"); // label to descibe
    private final JLabel passwortLabel = new JLabel("Password:");// label to descibe
    private final JLabel yourConnectionLabel = new JLabel();
    private final JPasswordField passwordTextfield = new JPasswordField(); //passwordfield to not show pw
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

    private final ArrayList<JComponent> loginComponents = new ArrayList<>();
    private final ArrayList<JComponent> gameComponents = new ArrayList<>();

    /**
     * Singleton-pattern for creating only one instance of the gui
     * @return
     */
    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
            LOG.info("Creating Gui instance.");
        }
        return INSTANCE;
    }

    /**
     * Constructor of the class
     * sets up the gui for displaying
     */
    public Gui () {
        // for the buttons
        ActionHandler act = new ActionHandler();

        componentPainter.setOpaque(false);

        playerListTable.getTableHeader().setReorderingAllowed(false);
        playerListTable.setModel(playerListModel);
        playerListTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        playerListTable.setEnabled(true);
        playerListScroll.setViewportView(playerListTable);

        addedPlayerToInviteTable.getTableHeader().setReorderingAllowed(false);
        addedPlayerToInviteTable.setModel(addedPlayerToInviteModel);
        addedPlayerToInviteTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        addedPlayerToInviteTable.setEnabled(true);
        addedPlayerToInviteScroll.setViewportView(addedPlayerToInviteTable);

        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"Nr.", "Player", "Action"});
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.setEnabled(true);
        table.setPreferredScrollableViewportSize(new Dimension(450,63));
        table.setFillsViewportHeight(true);
        model = (DefaultTableModel) table.getModel();
        scroll.setViewportView(table);

        setupLoginComponents(act);

        setupGameComponents(act);

        setupGUI();
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

    /**
     * adds the given data to the Model playerListModel
     * @param data
     */
    public void addDataToPlayerListModel(Object... data){
        playerListModel.addRow(data);
    }

    /**
     * adds the given data to the Model addedPlayerToInviteModel
     * @param data
     */
    public void addDataToAddedPlayerModel(Object... data){
        addedPlayerToInviteModel.addRow(data);
    }

    /**
     * does a POST-request to the server and asks for the currently connected clients
     * adds them (except your connection) to the table for displaying
     */
    public void getConnectUserData(){
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
        } catch (IOException ignored) {
        }

        assert jsonNode != null;
        if(jsonNode.size() != 0) {
            for (int i = 0; i < Objects.requireNonNull(jsonNode).size() - 1; i++) {
                Gui.getInstance().addDataToPlayerListModel(usernames.get(i), socketIds.get(i));
            }
        }
    }

    /**
     * adds all component needed for the login or for (dis)playing the game depending on the parameter to the list
     * @param componentType
     */
    public void ComponentsToList(ComponentType componentType){
        if(componentType.toString().equals("LOGIN")){
            loginComponents.add(loginButton);
            loginComponents.add(savaLoginData);
            loginComponents.add(usernameTextfield);
            loginComponents.add(usernameLabel);
            loginComponents.add(passwordTextfield);
            loginComponents.add(passwortLabel);
        } else if(componentType.toString().equals("GAME")) {
            gameComponents.add(componentPainter);
            gameComponents.add(playerListScroll);
            gameComponents.add(reloadPlayerList);
            gameComponents.add(addPlayerToInvite);
            gameComponents.add(removePlayerToInvite);
            gameComponents.add(addedPlayerToInviteTable);
            gameComponents.add(inviteChosenPlayer);
            gameComponents.add(yourConnectionLabel);
            gameComponents.add(scroll);
        }
    }

    /**
     * sets the visibility of the components needed for the login or for playing the game depending on the parameter
     * to visible (true)
     * or not visible (false)
     * @param componentType
     * @param bool
     */
    public void setVisabilityComponents(ComponentType componentType, boolean bool){
        if(componentType.toString().equals("LOGIN")){
            for (JComponent loginComponent : loginComponents) {
                loginComponent.setVisible(bool);
            }
        } else if(componentType.toString().equals("GAME")) {
            for (JComponent gameComponent : gameComponents) {
                gameComponent.setVisible(bool);
            }
        }
    }

    /**
     * add a listener to the components needed for the login or for playing the game depending on the parameter
     * @param componentType
     * @param act
     */
    public void addComponentsActionListeners(ComponentType componentType, ActionListener act){
            if(componentType.toString().equals("LOGIN")){
                loginButton.addActionListener(act);
                savaLoginData.addActionListener(act);
            } else if(componentType.toString().equals("GAME")){
                reloadPlayerList.addActionListener(act);
                addPlayerToInvite.addActionListener(act);
                removePlayerToInvite.addActionListener(act);
                inviteChosenPlayer.addActionListener(act);
            }
    }

    /**
     * sets the bounds of the components needed for the login or for (dis)playing the game depending on the parameter
     * @param componentType
     */
    public void setComponentsBounds(ComponentType componentType){
        if(componentType.toString().equals("LOGIN")){
            loginButton.setBounds(250,30,120,30);
            savaLoginData.setBounds(250,70,120,30);
            usernameTextfield.setBounds(120,30,120,30);
            passwordTextfield.setBounds(120,70,120,30);
            passwortLabel.setBounds(10,70,120,30);
            usernameLabel.setBounds(10,30,120,30);
        } else if(componentType.toString().equals("GAME")){
            componentPainter.setBounds(10,200,560,560); //showing the cards
            playerListScroll.setBounds(575,40, 505, 200);
            reloadPlayerList.setBounds(575, 0, 520, 30);
            addPlayerToInvite.setBounds(575, 245, 250, 30);
            removePlayerToInvite.setBounds(835, 245, 245, 30);
            inviteChosenPlayer.setBounds(575, 485, 520, 30);
            yourConnectionLabel.setBounds(575, 735, 520,30);
            scroll.setBounds(10,0,560,200);
        }
    }

    /**
     * adds all components to the JFrame
     */
    public void addComponents(){
        for(JComponent loginComponent : loginComponents) {
            add(loginComponent);
        }

        for(JComponent gameComponent : gameComponents){
            add(gameComponent);
        }
    }

    /**
     * sets up the components needed for the login
     * sets the visibility to false
     * adds a listener
     * sets the bounds
     * @param act
     */
    public void setupLoginComponents(ActionListener act){
        ComponentsToList(ComponentType.LOGIN);
        setVisabilityComponents(ComponentType.LOGIN,true);
        addComponentsActionListeners(ComponentType.LOGIN, act);
        setComponentsBounds(ComponentType.LOGIN);

        LOG.info("Finished setting up the components for the login.");
    }

    /**
     * * sets up the components needed for (dis)playing the game
     * * sets the visibility to false
     * * adds a listener
     * * sets the bounds
     * @param act
     */
    public void setupGameComponents(ActionListener act){
        ComponentsToList(ComponentType.GAME);
        setVisabilityComponents(ComponentType.GAME, false);
        addComponentsActionListeners(ComponentType.GAME, act);
        setComponentsBounds(ComponentType.GAME);

        LOG.info("Finished setting up the components for the game.");
    }

    /**
     * * sets up the gui for displaying
     */
    public void setupGUI(){

        addComponents();

        setSize(1100,805);
        setVisible(true);
        setResizable(false);

        setIconImage(new ImageIcon("sprites\\image_icon.png").getImage());

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        LOG.info("Setting up the GUI is done.");
    }

    public void setUsernameTextfield(String userName){
        getUsernameTextfield().setText(userName);
    }
    public void setPasswordTextfield(String password){
        getPasswordTextfield().setText(password);
    }
    public List<Card> getPlayerHand() {
        return playerHand;
    }
    public Card getInitialTopCard(){return initialTopCard;}
    public String getPasswort(){return passwordTextfield.getText();}
    public String getUsername(){return usernameTextfield.getText();}
    public JPasswordField getPasswordTextfield(){return passwordTextfield;}
    public JTextField getUsernameTextfield(){return usernameTextfield;}
    public JButton getLoginButton(){return loginButton;}
    public JButton getSavaLoginData(){return savaLoginData;}
    public JButton getReloadPlayerList() {
        return reloadPlayerList;
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

