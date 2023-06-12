package view;

import event_handling.ServerEventHandler;
import gameobjects.cards.Card;
import javax.swing.*;
import java.awt.*;

/**
 * This class uses the Image Loader to show the Card assets of the upper card of the discard pile
 * and the current player´s cards
 */
public class ComponentPainter extends JPanel{

    private static boolean eliminated = false; // value if the player is eliminated in a round
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
        int rowSeparator = 4; // usually new row after 4 cards
        if (Gui.getInstance().getPlayerHand().size() > 8){
            xSize = 300/((int)((Gui.getInstance().getPlayerHand().size()+1)/2));
            ySize = (int)(xSize/0.75);
            rowSeparator = (Gui.getInstance().getPlayerHand().size()+1)/2;
        }
        ImageLoader background = new ImageLoader("background.png"); //decoration only

        g.drawImage(background.image, 0, 0, 565, 565, null); // draws the colored background

        if(!eliminated) { // displays the cards if the player is not eliminated
            for (Card card : Gui.getInstance().getPlayerHand()) {
                // update position of each card
                if (cardCount < rowSeparator) {
                    ImageLoader imageLoader = new ImageLoader(card); // load picture
                    g.drawImage(imageLoader.image, start, 140, xSize, ySize, null); // show picture
                    g.drawRect(start, 140, xSize, ySize); // black frame
                } else {
                    if (cardCount == rowSeparator) {
                        start = 115;
                    }
                    ImageLoader imageLoader = new ImageLoader(card); // load picture
                    g.drawImage(imageLoader.image, start, 145 + ySize, xSize, ySize, null); // show picture
                    g.drawRect(start, 145 + ySize, xSize, ySize); // black frame
                }
                start += xSize + 5; // update position of each card
                cardCount++;
            }
        }
        ImageLoader imageLoader = new ImageLoader(Gui.getInstance().getInitialTopCard()); // show top card
        g.drawString("Front card:", 115,350); // show the card on the stack top
        g.drawImage(imageLoader.image, 115,355 ,xSize,ySize,null);
        g.drawRect(115,355 ,xSize,ySize); // frame

        g.drawString("Round: " + ServerEventHandler.getRoundCounter(),350,400);
        g.drawString("Current player: " + ServerEventHandler.getCurrentPlayer(), 270, 425);

        if(eliminated) { // shows the image "eliminated" when the user of the client is eliminated
            ImageLoader eliminatedImage = new ImageLoader("eliminated.png");
            g.drawImage(eliminatedImage.image, 96, 460, 370, 90, null);
        }
   }

   // GETTER AND SETTER
    public static boolean isEliminated() {
        return eliminated;
    }

    public static void setEliminated(boolean eliminatedBoolean) {
        eliminated = eliminatedBoolean;
    }
}
