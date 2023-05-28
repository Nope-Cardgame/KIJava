package command;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import logic.Main;
import logic.Rest;
import org.json.JSONException;
import view.Gui;

public class ActionHandler implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
         if(src == Gui.getInstance().getLoginButton()) {
             boolean validLogin = Main.connect(Gui.getInstance().getUsername(), Gui.getInstance().getPasswort());

             if(validLogin) {
                 Gui.getInstance().getScroll().setVisible(true);
                 Gui.getInstance().getLoginButton().setVisible(false);
                 Gui.getInstance().getSavaLoginData().setVisible(false);
                 Gui.getInstance().getUsernameTextfield().setVisible(false);
                 Gui.getInstance().getPasswortLabel().setVisible(false);
                 Gui.getInstance().getPasswordfield().setVisible(false);
                 Gui.getInstance().getUsernameLabel().setVisible(false);

                 Gui.getInstance().addUserData();
                 Gui.getInstance().getPlayerListScroll().setVisible(true);
                 Gui.getInstance().getShowCards().setVisible(true);
                 Gui.getInstance().getReloadPlayerList().setVisible(true);
                 Gui.getInstance().getAddPlayerToInvite().setVisible(true);
                 Gui.getInstance().getRemovePlayerToInvite().setVisible(true);
                 Gui.getInstance().getInviteChosenPlayer().setVisible(true);
                 Gui.getInstance().getAddedPlayerToInviteScroll().setVisible(true);
                 Gui.getInstance().getInviteChosenPlayer().setVisible(true);
             }
         }
         if(src == Gui.getInstance().getReloadPlayerList()){
             Gui.getInstance().addUserData();
         }
         if(src == Gui.getInstance().getAddPlayerToInvite()){
             int[] row = Gui.getInstance().getPlayerListTable().getSelectedRows();

             if(row[row.length - 1] - row[0] <= 1){
                 return;
             }

             for (int j = row[0]; j <= row[row.length-1]; j++) {
                 String addedPlayer = (String) Gui.getInstance().getPlayerListTable().getValueAt(j, 0);
                 String addedSocketId = (String) Gui.getInstance().getPlayerListTable().getValueAt(j, 1);
                 if(!isInTable(Gui.getInstance().getAddedPlayerToInviteTable(), addedPlayer, addedSocketId)) {
                     if(!addedPlayer.equals(Main.getUsername_global())) {
                         Gui.getInstance().addDataToAddedPlayerModel(addedPlayer, addedSocketId);
                     } else {
                         // TODO grafisch noch darstellen
                         System.out.println("You can't invite yourself.");
                     }
                 }
             }
         }
         if(src == Gui.getInstance().getInviteChosenPlayer()){
             Rest rest = new Rest();
             String[] playernames = new String[Gui.getInstance().getAddedPlayerToInviteTable().getRowCount()];
             String[] socketIDs = new String[Gui.getInstance().getAddedPlayerToInviteTable().getRowCount()];
             for(int i = 0; i <= playernames.length-1; i++) {
                playernames[i] = (String) Gui.getInstance().getAddedPlayerToInviteTable().getValueAt(i, 0);
                socketIDs[i] = (String) Gui.getInstance().getAddedPlayerToInviteTable().getValueAt(i, 1);
             }

             try {
                 rest.invitePlayer(playernames, socketIDs);
             }catch (IOException | JSONException ex) {
                 throw new RuntimeException(ex);
            }
         }
         if(src == Gui.getInstance().getRemovePlayerToInvite()){
             int[] row = Gui.getInstance().getAddedPlayerToInviteTable().getSelectedRows();
             try {
                 for (int j = row[0]; j <= row[row.length - 1]; j++) {
                     ((DefaultTableModel) Gui.getInstance().getAddedPlayerToInviteTable().getModel()).removeRow(row[0]);
                 }
             } catch (ArrayIndexOutOfBoundsException ignored){
             }
         }

         if(src == Gui.getInstance().getSavaLoginData()) {
             try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\main\\client\\userdata.txt"))) {
                 writer.write(Gui.getInstance().getPasswort()+"\n"+Gui.getInstance().getUsername());
                 System.out.println("Der String wurde erfolgreich in die Datei geschrieben.");
             } catch (IOException ioException) {
                 System.out.println("Fehler beim Schreiben der Datei: " + ioException.getMessage());
             }
         }
    }

    private boolean isInTable(JTable table, String addedPlayer, String addedSocketId) {
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
}