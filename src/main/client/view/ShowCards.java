package view;

import gameobjects.cards.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ShowCards extends JPanel{

    /**
    * this JPanel shows the pictures  of all hand cards and the top cards of the stack
    */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Your cards:", 115,135);
        int start = 115; // the position of each card in a row
        int xSize = 75;// width of card
        int ySize = 100; // height of card

        int cardCount = 0;

        ImageLoader backgroundImage = new ImageLoader(null);

        g.drawImage(backgroundImage.image, 0, 0, 565, 565, null);

        for (Card card: Gui.getInstance().getPlayerHand()){
            //update position of each card
            if(cardCount < 4) {
                ImageLoader imageLoader = new ImageLoader(card); // load picture
                g.drawImage(imageLoader.image, start, 140, xSize, ySize, null); //show picture
                g.drawRect(start, 140, xSize, ySize); //black frame
            } else {
                if(cardCount == 4){
                    start = 115;
                }
                ImageLoader imageLoader = new ImageLoader(card); // load picture
                g.drawImage(imageLoader.image, start, 145 + ySize, xSize, ySize, null); //show picture
                g.drawRect(start, 145 + ySize, xSize, ySize); //black frame
            }
            start += xSize + 5; //update position of each card
            cardCount++;
        }

        ImageLoader imageLoader = new ImageLoader(Gui.getInstance().getInitialTopCard()); //show card
        g.drawString("Front card:", 115,350); //show the card on the stack top
        g.drawImage(imageLoader.image, 115,355 ,xSize,ySize,null);
        g.drawRect(115,355 ,xSize,ySize);//frame
   }
}
