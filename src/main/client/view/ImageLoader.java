package view;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * this class is needed to load an images showing a card on the gui
 */
public class ImageLoader {
    BufferedImage image;
    public ImageLoader(Card card) {
        if(card == null){
            try {
                image = ImageIO.read(new File("cardimages\\error.png"));
            } catch (IOException ignored){
            }
        } else {
            try {
                image = ImageIO.read(new File(card.getSpritePath())); //load by url from card
            } catch (IOException ignored) {
            }
        }
    }

    public ImageLoader(String relativePath){
        if(!relativePath.equals("")){
            try {
                image = ImageIO.read(new File("cardimages\\" + relativePath));
            } catch (IOException ignored){
            }
        }
    }
}
