package gameobjects.command;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gameobjects.view.Gui;

public class ActionHandler implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        JButton src = (JButton) e.getSource();
        String password = Gui.getInstance().getPasswortInput().getText();
        String username = Gui.getInstance().getUsernameInput().getText();
        if(src==Gui.getInstance().getLogin()) {

        }
    }
}