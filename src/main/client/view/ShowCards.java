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
       Graphics2D g2d = (Graphics2D)g;
       g.drawString("your cards", 0,45);
       int start = 0; // the position of each card in a row
       int xSize = 75;//width of card
       int ySize = 100; // hight of card
       int cardCount = Gui.getInstance().getDiscardPile().size();
       if (cardCount>6){
           xSize = 500/cardCount; // set the size according to card amount to not overfill row of cards
           ySize = (int) (xSize*1.33); // each card with same frame

       }
       for (Card card: Gui.getInstance().getDiscardPile()){
           ImageLoader imageLoader = new ImageLoader(card); // load picture
           g.drawImage(imageLoader.image, start,50 ,xSize,ySize,null); //show picture
           g.drawRect(start,50 ,xSize,ySize); //black frame
           start+=xSize; //update position of each card
       }

       ImageLoader imageLoader = new ImageLoader(Gui.getInstance().getInitialTopCard()); //show card
       g.drawString("Front Cards", 0,175); //show the card on the stack top
       g.drawImage(imageLoader.image, 0,180 ,100,130,null);
       g.drawRect(0,180 ,100,130);//frame

   }
}
