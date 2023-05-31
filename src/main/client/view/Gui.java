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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class Gui extends JFrame {

    /**
     * enum for deciding which type the components belong to
     */
    public enum ComponentType {
        LOGIN,
        GAME
    }

    private static final Logger LOG = NopeLogger.getLogger(Gui.class.getSimpleName()); // logger of the class
    private static Gui INSTANCE; // stores the instance of the gui

    private final ComponentPainter componentPainter = new ComponentPainter();// jpanel showing the cards with pictures

    private Card initialTopCard; // stores the card on top of the discard pile
    private List<Card> playerHand = new ArrayList<>(); // the cards on the hand of the player

    private final JTextField usernameTextfield = new JTextField(); // the user can type in his name
    private final JPasswordField passwordTextfield = new JPasswordField(); //passwordfield to not show pw

    private final JLabel usernameLabel = new JLabel("User name:"); // label added to the textfield usernameTextfield
    private final JLabel passwortLabel = new JLabel("Password:"); // label added to the textfield passworddTextfield
    private final JLabel yourConnectionLabel = new JLabel(); // label to display your connection on the bottom of the gui
    private final JLabel noActionCardsLabel = new JLabel("Do you want to deactivate actioncards?"); // label added to the combobox noActionCardsComboBox
    private final JLabel noWildCardsLabel = new JLabel("Do you want to deactivate wildcards??"); // label added to the combobox noWildCardsComboBox
    private final JLabel oneMoreStartCardLabel = new JLabel("Do you want to start with one extra card?"); // label added to the combobox oneMoreStartCardComboBox

    private final JComboBox<Boolean> noActionCardsComboBox = new JComboBox<>(new Boolean[]{ true, false }); // combobox to set up the value of the setting noActionCards
    private final JComboBox<Boolean> noWildCardsComboBox = new JComboBox<>(new Boolean[]{ true, false }); // combobox to set up the value of the setting noWildCards
    private final JComboBox<Boolean> oneMoreStartCardComboBox = new JComboBox<>(new Boolean[]{ true, false }); // combobox to set up the value of the setting oneMoreStartCard

    private final JButton loginButton = new JButton("Log in"); // button for login
    private final JButton savaLoginDataButton = new JButton("Save Data"); // button to save login data in .txt document userdata.txt
    private final JButton reloadPlayerListButton = new JButton("Reload player list"); // button to reload the displayed list of connected players
    private final JButton addPlayerToInviteButton = new JButton("Add marked player to list"); // button to add the marked player to a new table below
    private final JButton removePlayerToInviteButton = new JButton("Remove marked player from list"); // button to remove the marked player(s) from the table
    private final JButton inviteChosenPlayerButton = new JButton("Invite players to game"); // button to invite the chosen player

    private final JTable gameTable = new JTable(); // shows all actions of game in a list
    private final JTable playerListTable = new JTable(); // shows all current connected users (except your connection)
    private final JTable addedPlayerToInviteTable = new JTable(); // shows all players added to your invite-request

    private DefaultTableModel gameModel = new DefaultTableModel(new Object[][]{}, new String[]{"Nr.", "Player", "Action", "Cards"}); // model of the table gameTable and the jscrollpane gameTable
    private final DefaultTableModel playerListModel = new DefaultTableModel(new Object[][]{}, new String[]{"Playername", "Socket-ID"}); // model of the table playerListTable and the jscrollpane playerListTable
    private final DefaultTableModel addedPlayerToInviteModel = new DefaultTableModel(new Object[][]{}, new String[]{"Added player to invite", "Socket-ID"}); // model of the table addedPlayerToInviteTable and the jscrollpane addedPlayerToInvitewScroll

    JScrollPane gameScroll = new JScrollPane(gameTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JScrollPane playerListScroll = new JScrollPane(playerListTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollPane addedPlayerToInviteScroll= new JScrollPane(addedPlayerToInviteTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    private int counter = 1; // counter to display the number of the current action

    private final ArrayList<JComponent> loginComponents = new ArrayList<>(); // Arraylist to store all components needed for the login
    private final ArrayList<JComponent> gameComponents = new ArrayList<>(); // Arraylist to store all components to display the game

    /**
     * Singleton-pattern for creating only one instance of the gui
     * @return INSTANCE
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

        // sets the table up for displaying the current connected clients
        playerListTable.getTableHeader().setReorderingAllowed(false);
        playerListTable.setModel(playerListModel);
        playerListTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        playerListTable.setEnabled(true);
        playerListScroll.setViewportView(playerListTable);

        // sets the table up for displaying the players selected for the invite request
        addedPlayerToInviteTable.getTableHeader().setReorderingAllowed(false);
        addedPlayerToInviteTable.setModel(addedPlayerToInviteModel);
        addedPlayerToInviteTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        addedPlayerToInviteTable.setEnabled(true);
        addedPlayerToInviteScroll.setViewportView(addedPlayerToInviteTable);

        // sets up the table for display the played action of each started game
        gameScroll.setVisible(false);
        gameTable.setModel(gameModel);
        gameTable.getColumnModel().getColumn(0).setPreferredWidth(5);
        gameTable.setEnabled(true);
        gameTable.setPreferredScrollableViewportSize(new Dimension(450,63));
        gameTable.setFillsViewportHeight(true);
        gameTable.setEnabled(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        gameTable.setDefaultRenderer(String.class, centerRenderer);
        gameTable.getColumnModel().getColumn(0).setWidth(50);
        gameTable.getColumnModel().getColumn(1).setWidth(100);
        gameTable.getColumnModel().getColumn(2).setMinWidth(250);
        gameTable.getColumnModel().getColumn(3).setMaxWidth(50);
        gameModel = (DefaultTableModel) gameTable.getModel();
        gameScroll.setBounds(10,0,560,200);
        gameScroll.setViewportView(gameTable);
        add(gameScroll);

        //sets up all components and the gui itself
        setupLoginComponents(act);

        setupGameComponents(act);

        setupGUI();
    }

    /**
     * update gui with current cards
     * @param game
     */
    public void refresh(Game game) {
        if(game != null) {
            if (game.getCurrentPlayer().getUsername().equals(Main.getUsername_global())) {
                playerHand = game.getCurrentPlayer().getCards();
            }
            initialTopCard = game.getDiscardPile().get(0);
            componentPainter.repaint();
            refreshGameTable(game);
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
    public void getConnectedUserData(){
        playerListModel.setRowCount(0);

        String userdata;

        try {
            userdata = Rest.requestWithReturn(Constants.GET_USER_CONNECTIONS.get(), Main.getToken(), RequestType.GET);
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
                addDataToPlayerListModel(usernames.get(i), socketIds.get(i));
            }
        }
    }

    /**
     * adds all component needed for the login or for (dis)playing the game depending on the parameter to the list
     * @param componentType
     */
    public void componentsToList(ComponentType componentType){
        if(componentType.toString().equals("LOGIN")){
            loginComponents.add(loginButton);
            loginComponents.add(savaLoginDataButton);
            loginComponents.add(usernameTextfield);
            loginComponents.add(usernameLabel);
            loginComponents.add(passwordTextfield);
            loginComponents.add(passwortLabel);
        } else if(componentType.toString().equals("GAME")) {
            gameComponents.add(componentPainter);
            gameComponents.add(addedPlayerToInviteScroll);
            gameComponents.add(playerListScroll);
            gameComponents.add(reloadPlayerListButton);
            gameComponents.add(addPlayerToInviteButton);
            gameComponents.add(removePlayerToInviteButton);
            gameComponents.add(inviteChosenPlayerButton);
            gameComponents.add(yourConnectionLabel);
            gameComponents.add(gameScroll);
            gameComponents.add(noActionCardsLabel);
            gameComponents.add(noActionCardsComboBox);
            gameComponents.add(noWildCardsLabel);
            gameComponents.add(noWildCardsComboBox);
            gameComponents.add(oneMoreStartCardLabel);
            gameComponents.add(oneMoreStartCardComboBox);
            gameComponents.add(new JPanel());
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
     * add a actionhandler to the components needed for the login or for playing the game depending on the parameter
     * @param componentType
     * @param act
     */
    public void addComponentsActionHandler(ComponentType componentType, ActionListener act){
            if(componentType.toString().equals("LOGIN")){
                loginButton.addActionListener(act);
                savaLoginDataButton.addActionListener(act);
            } else if(componentType.toString().equals("GAME")){
                reloadPlayerListButton.addActionListener(act);
                addPlayerToInviteButton.addActionListener(act);
                removePlayerToInviteButton.addActionListener(act);
                inviteChosenPlayerButton.addActionListener(act);
            }
    }

    /**
     * sets the bounds of the components needed for the login or for (dis)playing the game depending on the parameter
     * @param componentType
     */
    public void setComponentsBounds(ComponentType componentType){
        if(componentType.toString().equals("LOGIN")){
            loginButton.setBounds(250,30,120,30);
            savaLoginDataButton.setBounds(250,70,120,30);
            usernameTextfield.setBounds(120,30,120,30);
            passwordTextfield.setBounds(120,70,120,30);
            passwortLabel.setBounds(10,70,120,30);
            usernameLabel.setBounds(10,30,120,30);
        } else if(componentType.toString().equals("GAME")){
            noWildCardsComboBox.setBounds(830, 555, 100, 30);
            componentPainter.setBounds(10,200,560,560);
            playerListScroll.setBounds(575,40, 505, 200);
            addedPlayerToInviteScroll.setBounds(575, 280, 505, 200);
            reloadPlayerListButton.setBounds(575, 0, 525, 30);
            addPlayerToInviteButton.setBounds(575, 245, 250, 30);
            removePlayerToInviteButton.setBounds(835, 245, 245, 30);
            inviteChosenPlayerButton.setBounds(575, 485, 520, 30);
            yourConnectionLabel.setBounds(575, 735, 520,30);
            gameScroll.setBounds(10,0,560,200);
            noActionCardsLabel.setBounds(575, 520, 250, 30);
            noActionCardsComboBox.setBounds(830, 520, 100, 30);
            noWildCardsLabel.setBounds(575, 555, 250, 30);
            oneMoreStartCardLabel.setBounds(575, 590, 250, 30);
            oneMoreStartCardComboBox.setBounds(830, 590, 100, 30);
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
        componentsToList(ComponentType.LOGIN);
        setVisabilityComponents(ComponentType.LOGIN,true);
        addComponentsActionHandler(ComponentType.LOGIN, act);
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
        componentsToList(ComponentType.GAME);
        setVisabilityComponents(ComponentType.GAME, false);
        addComponentsActionHandler(ComponentType.GAME, act);
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
        setTitle("Nope-Client Java (disconnected)");

        LOG.info("Setting up the GUI is done.");
    }

    /**
     * refreshes the table which displays the played turns
     * @param game
     */
    public void refreshGameTable(Game game) {
        gameModel.addRow(new Object[]{
                String.valueOf(counter),
                game.getCurrentPlayer().getUsername(),
                game.getLastAction().getType(),
                String.valueOf(game.getCurrentPlayer().getCardAmount())});
        counter++;
    }

    /**
     * clears the entries of the table which displays the played turns
     */
    public void resetGameTable(){
        while(gameModel.getRowCount()>0) {gameModel.removeRow(0);}
        counter = 1;
    }

    /**
     *  checks if the given addedPlayer and the addedSocketId are already in the given table
     *
     * @param table
     * @param addedPlayer
     * @param addedSocketId
     * @return false (if not in table), true (if in list)
     */
    public boolean isInTable(JTable table, String addedPlayer, String addedSocketId) {
        boolean output = false;

        for (int i = 0; i < table.getRowCount(); i++) {
            String addedPlayerTemp = (String) table.getValueAt(i, 0);
            String addedSocketIdTemp = (String) table.getValueAt(i, 0);
            if(addedPlayerTemp.equals(addedPlayer) || addedSocketIdTemp.equals(addedSocketId)){
                output = true;
            }
        }
        return output;
    }

    // GETTER AND SETTER
    public List<Card> getPlayerHand() {
        return playerHand;
    }
    public Card getInitialTopCard(){return initialTopCard;}
    public String getPasswort(){return passwordTextfield.getText();}
    public String getUsername(){return usernameTextfield.getText();}
    public JPasswordField getPasswordTextfield(){return passwordTextfield;}
    public JTextField getUsernameTextfield(){return usernameTextfield;}
    public JButton getLoginButton(){return loginButton;}
    public JButton getSavaLoginDataButton(){return savaLoginDataButton;}
    public JButton getReloadPlayerListButton() {
        return reloadPlayerListButton;
    }
    public JButton getAddPlayerToInviteButton() {
        return addPlayerToInviteButton;
    }
    public JButton getRemovePlayerToInviteButton() {
        return removePlayerToInviteButton;
    }
    public JButton getInviteChosenPlayerButton() {
        return inviteChosenPlayerButton;
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
    public JComboBox<Boolean> getNoActionCardsComboBox() {
        return noActionCardsComboBox;
    }
    public JComboBox<Boolean> getNoWildCardsComboBox() {
        return noWildCardsComboBox;
    }
    public JComboBox<Boolean> getOneMoreStartCardComboBox() {
        return oneMoreStartCardComboBox;
    }

    public void setUsernameTextfield(String userName){
        getUsernameTextfield().setText(userName);
    }
    public void setPasswordTextfield(String password){
        getPasswordTextfield().setText(password);
    }
}

