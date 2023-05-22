package command;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;

import logic.Main;
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