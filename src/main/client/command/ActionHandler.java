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

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
         if(src == Gui.getInstance().getLoginButton()) {
             boolean validLogin = Main.connect(Gui.getInstance().getUsername(), Gui.getInstance().getPasswort());

             if(validLogin) {
                     Gui.getInstance().setVisabilityComponents(Gui.ComponentType.LOGIN, false);

                 Gui.getInstance().getConnectedUserData();

                 try {
                     Gui.getInstance().getYourConnectionLabel().setText("Own Connection: Username: " + Main.getUsername_global() + "; SocketID: " + Main.findMySocketID());
                 } catch (JSONException ex) {
                     throw new RuntimeException(ex);
                 }

                 Gui.getInstance().setVisabilityComponents(Gui.ComponentType.GAME, true);
             }
         }
         if(src == Gui.getInstance().getReloadPlayerList()){
             Gui.getInstance().getConnectedUserData();
         }
         if(src == Gui.getInstance().getAddPlayerToInvite()){
             int[] row = Gui.getInstance().getPlayerListTable().getSelectedRows();

             for (int j = row[0]; j <= row[row.length-1]; j++) {
                 String addedPlayer = (String) Gui.getInstance().getPlayerListTable().getValueAt(j, 0);
                 String addedSocketId = (String) Gui.getInstance().getPlayerListTable().getValueAt(j, 1);
                 if(!isInTable(Gui.getInstance().getAddedPlayerToInviteTable(), addedPlayer, addedSocketId)) {
                     Gui.getInstance().addDataToAddedPlayerModel(addedPlayer, addedSocketId);
                 }
             }
         }
         if(src == Gui.getInstance().getInviteChosenPlayer()){
             Rest rest = new Rest();
             Gui.getInstance().resetGameTable();
             ComponentPainter.setEliminated(false);
             String[] playernames = new String[Gui.getInstance().getAddedPlayerToInviteTable().getRowCount()];
             String[] socketIDs = new String[Gui.getInstance().getAddedPlayerToInviteTable().getRowCount()];
             for(int i = 0; i <= playernames.length-1; i++) {
                playernames[i] = (String) Gui.getInstance().getAddedPlayerToInviteTable().getValueAt(i, 0);
                socketIDs[i] = (String) Gui.getInstance().getAddedPlayerToInviteTable().getValueAt(i, 1);
             }
             try {
                 if(playernames.length > 0) rest.invitePlayer(playernames, socketIDs);
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
                 writer.write(Gui.getInstance().getUsername()+"\n"+Gui.getInstance().getPasswort());
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