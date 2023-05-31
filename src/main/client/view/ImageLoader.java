package view;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * this class is needed to load an images on the gui
 */
public class ImageLoader {
    BufferedImage image;
    public ImageLoader(Card card) {
        if(card == null){
            try {
                image = ImageIO.read(new File("sprites\\error.png")); // loads error card
            } catch (IOException ignored){
            }
        } else {
            try {
                image = ImageIO.read(new File(card.getSpritePath())); // loads card by return value of the method
            } catch (IOException ignored) {
            }
        }
    }

    public ImageLoader(String relativePath){
        if(!relativePath.equals("")){
            try {
                image = ImageIO.read(new File("sprites\\" + relativePath)); // load card by given path
            } catch (IOException ignored){
            }
        }
    }
}
