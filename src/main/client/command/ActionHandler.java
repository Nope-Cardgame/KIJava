package command;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import logic.Main;
import logic.Rest;
import org.json.JSONException;
import view.ComponentPainter;
import view.Gui;

public class ActionHandler implements ActionListener {

    /**
     * method to check actions on the gui
     *
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

         if(src == Gui.getInstance().getLoginButton()) {
             boolean validLogin = Main.connect(Gui.getInstance().getUsername(), Gui.getInstance().getPasswort());

             if(validLogin) {
                 Gui.getInstance().setVisabilityComponents(Gui.ComponentType.LOGIN, false); // sets the components of the gui needed for login to invisible

                 Gui.getInstance().getConnectedUserData(); // adds the connected users to the playerListTable

                 try {
                     Gui.getInstance().getYourConnectionLabel().setText("Own Connection: Username: " + Main.getUsername_global() + "; SocketID: " + Main.findMySocketID());
                 } catch (JSONException ex) {
                     throw new RuntimeException(ex);
                 }

                 Gui.getInstance().setVisabilityComponents(Gui.ComponentType.GAME, true); // sets the components of the gui needed for game to visible
             }
         }

         if(src == Gui.getInstance().getReloadPlayerListButton()){
             Gui.getInstance().getConnectedUserData(); // reloads the table with the connected users
         }

         if(src == Gui.getInstance().getAddPlayerToInviteButton()){ // adds the selected players in the table playerListTable to the model addedPlayerToInviteModel
             int[] row = Gui.getInstance().getPlayerListTable().getSelectedRows();

             for (int j = row[0]; j <= row[row.length-1]; j++) {
                 String addedPlayer = (String) Gui.getInstance().getPlayerListTable().getValueAt(j, 0);
                 String addedSocketId = (String) Gui.getInstance().getPlayerListTable().getValueAt(j, 1);
                 if(!Gui.getInstance().isInTable(Gui.getInstance().getAddedPlayerToInviteTable(), addedPlayer, addedSocketId)) {
                     Gui.getInstance().addDataToAddedPlayerModel(addedPlayer, addedSocketId);
                 }
             }
         }

        if(src == Gui.getInstance().getRemovePlayerToInviteButton()){ // removes the selected players from the table addedPlayerToInviteModel
            int[] row = Gui.getInstance().getAddedPlayerToInviteTable().getSelectedRows();
            try {
                for (int j = row[0]; j <= row[row.length - 1]; j++) {
                    ((DefaultTableModel) Gui.getInstance().getAddedPlayerToInviteTable().getModel()).removeRow(row[0]);
                }
            } catch (ArrayIndexOutOfBoundsException ignored){
            }
        }

         if(src == Gui.getInstance().getInviteChosenPlayerButton()){ // invites the player from the addedPlayerToInviteModel to a new game
             Gui.getInstance().resetGameTable();
             ComponentPainter.setEliminated(false);
             String[] playernames = new String[Gui.getInstance().getAddedPlayerToInviteTable().getRowCount()];
             String[] socketIDs = new String[Gui.getInstance().getAddedPlayerToInviteTable().getRowCount()];
             for(int i = 0; i <= playernames.length-1; i++) {
                playernames[i] = (String) Gui.getInstance().getAddedPlayerToInviteTable().getValueAt(i, 0);
                socketIDs[i] = (String) Gui.getInstance().getAddedPlayerToInviteTable().getValueAt(i, 1);
             }
             try {
                 if(playernames.length > 0) Rest.invitePlayer(playernames, socketIDs);
             }catch (IOException | JSONException ex) {
                 throw new RuntimeException(ex);
            }
         }

         if(src == Gui.getInstance().getSavaLoginDataButton()) { // saves the data in the usernameTextfield and passwordTextfield to the file userdata.txt
             try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\main\\client\\userdata.txt"))) {
                 writer.write(Gui.getInstance().getUsername()+"\n"+Gui.getInstance().getPasswort());
                 System.out.println("Der String wurde erfolgreich in die Datei geschrieben.");
             } catch (IOException ioException) {
                 System.out.println("Fehler beim Schreiben der Datei: " + ioException.getMessage());
             }
         }
    }
}