package view;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * this class is needed to load an images showing a card on the gui
 */
public class ImageLoader {
    BufferedImage image;
    public ImageLoader(NumberCard card)  {
        try {
           image = ImageIO.read(new File(card.getSpritePath()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
