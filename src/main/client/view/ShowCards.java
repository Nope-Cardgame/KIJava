package view;

import gameobjects.cards.NumberCard;

import javax.swing.*;
import java.awt.*;

public class ShowCards extends JPanel{


   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       Graphics2D g2d = (Graphics2D)g;
       int start = 0;
       for (NumberCard card: Gui.getInstance().getOwnCards()){
       ImageLoader imageLoader = new ImageLoader(card);
       g.drawImage(imageLoader.image, start,0,23,23,null);
       start+=25;
       }
   }
}
