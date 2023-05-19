package command;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.Gui;

public class ActionHandler implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();

         String password = Gui.getInstance().getPasswort();
         String username = Gui.getInstance().getUsername();
         if(src == Gui.getInstance().getJButtonLogin()) {


         }
         if(src == Gui.getInstance().getjButtonLoginInFromFile()) {

         }
         if(src == Gui.getInstance().getjButtonRegister()) {

         }
    }
}