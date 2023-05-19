package view;

import gameobjects.cards.NumberCard;

import javax.swing.*;
import java.awt.*;

public class ShowCards extends JPanel{


   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       Graphics2D g2d = (Graphics2D)g;
       g.drawString("your Cards", 0,50);
       int start = 0;
       int xSize = 100;
       int ySize = 130;
       int cardCount = Gui.getInstance().getOwnCards().size();
       if (cardCount>5){
           xSize = 500/cardCount;
           ySize = (int) (xSize*1.3);

       }
       for (NumberCard card: Gui.getInstance().getOwnCards()){
           ImageLoader imageLoader = new ImageLoader(card);
           g.drawImage(imageLoader.image, start,70 ,xSize,ySize,null);
           start+=xSize;
       }

       ImageLoader imageLoader = new ImageLoader(Gui.getInstance().getFrontCard());
       g.drawString("your Cards", 0,150); //show the card on the stack top
       g.drawImage(imageLoader.image, 0,170 ,100,130,null);
   }
}