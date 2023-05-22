package view;

import javax.swing.*;
import java.awt.*;

class BackgroundImagePanel extends JPanel {
    private Image image;
    private boolean fitImage;

    /**
     * Erzeugt ein BackgroundImagePanel, das sich zunächst wie ein "normales" JPanel verhält.
     */
    BackgroundImagePanel() {
        super();
    }

    /**
     * Nimmt das zu zeichnende Bild entgegen und nimmt Einstellungen vor, die das Hintergrundbild im Originalformat
     * und in den Original-Dimensionen ausgeben werden.
     *
     * @param image das zu zeichnende Bild
     */
    void setImage(Image image) {
        this.setImage(image, false);
    }

    /**
     * Nimmt das zu zeichnende Bild entgegen und nimmt Einstellungen vor, die das Hintergrundbild im Originalformat
     * <b>oder in Format und Dimension des Panels ausgeben werden.</b>
     *
     * @param image das zu zeichnende Bild
     * @param fitImage bei Übergabe von true wird das Bild immer an die Größe des Panel angepasst (kann zu Verzerrung führen),
     * ansonsten wird immer das Original gezeichnet.
     */
    void setImage(Image image, boolean fitImage) {
        this.image = image;
        this.fitImage = fitImage;
        validate();
        repaint();
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        if(image != null) {
            return new Dimension(image.getWidth(this), image.getHeight(this));
        }
        return super.getPreferredSize();
    }

    /**
     * @see javax.swing.JComponent#paintComponent(Graphics g)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null) {
            Dimension size = this.getSize();
            g.drawImage(image, 5, 1, 560, 560, this);
        }
    }
}