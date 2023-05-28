package view;

import event_handling.ServerEventHandler;
import gameobjects.cards.Card;

import javax.swing.*;
import java.awt.*;

public class ComponentPainter extends JPanel{

    private static boolean eliminated = false;

    /**
    * this JPanel shows the pictures  of all hand cards and the top cards of the stack
    */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Your cards:", 115,130);
        int start = 115; // the position of each card in a row
        int xSize = 75;// width of card
        int ySize = 100; // height of card

        int cardCount = 0;

        ImageLoader background = new ImageLoader("background.png");

        g.drawImage(background.image, 0, 0, 565, 565, null);

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

        g.drawString("Round: " + ServerEventHandler.getRoundCounter() / ServerEventHandler.getPlayerCount(),350,400);
        g.drawString("Current player: " + ServerEventHandler.getCurrentPlayer(), 300, 425);

        if(eliminated) {
            ImageLoader eliminatedImage = new ImageLoader("eliminated.png");
            g.drawImage(eliminatedImage.image, 180, 488, 200, 60, null);
        }
   }

    public static void setEliminated(boolean eliminatedBoolean) {
        eliminated = eliminatedBoolean;
    }
}
