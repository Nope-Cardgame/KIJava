package view;
import gameobjects.cards.Card;
import gameobjects.cards.NumberCard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class loads the images of the cards from a given path. The class is used by
 * the ComponentPainter
 */

/**
 * this class is needed to load an images on the gui
 */
public class ImageLoader {
    BufferedImage image;

    /**
     * constructor of the class with an object of the class Card as parameter
     *
     * @param card
     */
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

    /**
     * constructor of the class with string parameter as path
     *
     * @param relativePath
     */
    public ImageLoader(String relativePath){
        if(!relativePath.equals("")){
            try {
                image = ImageIO.read(new File("sprites\\" + relativePath)); // load card by given path
            } catch (IOException ignored){
            }
        }
    }
}
