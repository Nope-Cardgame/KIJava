package command;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import logic.Main;
import logic.Rest;
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
             }
         }
         if(src == Gui.getInstance().getReloadPlayerList()){
             Gui.getInstance().addUserData();
         }
         if(src == Gui.getInstance().getAddPlayerToInvite()){
             int[] row = Gui.getInstance().getPlayerListTable().getSelectedRows();
             System.out.println(row);
             for (int j = row[0]; j < row[row.length]; j++) {
                 String addedPlayer = (String) Gui.getInstance().getPlayerListTable().getValueAt(j, 0);
                 String addedSocketId = (String) Gui.getInstance().getPlayerListTable().getValueAt(j, 1);
                 Gui.getInstance().addDataToAddedPlayerModel(addedPlayer, addedSocketId);
                 Gui.getInstance().getPlayerListTable().remove(j);
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
}