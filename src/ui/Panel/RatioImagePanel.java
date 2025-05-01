package ui.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RatioImagePanel extends JPanel {
    private BufferedImage image;
    private double aspectRatio; // 比例，例如 16.0/9

    public RatioImagePanel(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setImage(BufferedImage img) {
        this.image = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        double panelRatio = (double) panelWidth / panelHeight;

        int drawWidth, drawHeight;

        if (panelRatio > aspectRatio) {
            drawHeight = panelHeight;
            drawWidth = (int) (drawHeight * aspectRatio);
        } else {
            drawWidth = panelWidth;
            drawHeight = (int) (drawWidth / aspectRatio);
        }

        int x = (panelWidth - drawWidth) / 2;
        int y = (panelHeight - drawHeight) / 2;

        g.drawImage(image, x, y, drawWidth, drawHeight, this);
    }
}
