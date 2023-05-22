package view;

import gameobjects.cards.Card;

import javax.swing.*;
import java.awt.*;

public class ShowCards extends JPanel{
/**
 * this JPanel shows the pictures  of all hand cards and the top cards of the stack
 * */
   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       g.drawString("Your cards:", 0,20);
       int start = 0; // the position of each card in a row
       int xSize = 75;// width of card
       int ySize = 100; // height of card

       int cardCount = 0;

       for (Card card: Gui.getInstance().getDiscardPile()){
           //update position of each card
           if(cardCount < 4) {
               ImageLoader imageLoader = new ImageLoader(card); // load picture
               g.drawImage(imageLoader.image, start, 25, xSize, ySize, null); //show picture
               g.drawRect(start, 25, xSize, ySize); //black frame
           } else {
               if(cardCount == 4){
                   start = 0;
               }
               ImageLoader imageLoader = new ImageLoader(card); // load picture
               g.drawImage(imageLoader.image, start, 30 + ySize, xSize, ySize, null); //show picture
               g.drawRect(start, 30 + ySize, xSize, ySize); //black frame
           }
           start += xSize + 5; //update position of each card
           cardCount++;
       }

       int y_offset;

       if(cardCount > 4){
           y_offset = 2 * ySize + 50;
       } else {
           y_offset = ySize + 50;
       }

       ImageLoader imageLoader = new ImageLoader(Gui.getInstance().getInitialTopCard()); //show card
       g.drawString("Front card:", 0,y_offset - 5); //show the card on the stack top
       g.drawImage(imageLoader.image, 0,y_offset ,xSize,ySize,null);
       g.drawRect(0,y_offset ,xSize,ySize);//frame
   }
}
