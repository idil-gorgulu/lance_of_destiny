package org.Domain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cannon extends JComponent {
    private Coordinate lcoord;
    private Coordinate rcoord;
    private long lastShotTime=-1;
    private BufferedImage cannonImage;

    public Cannon(Coordinate lcoord){
        this.lcoord = lcoord;


        try {
            cannonImage = ImageIO.read(new File("assets/cannon.png"));
            // Resize the image to 100x20 pixels
            cannonImage = resizeImage(cannonImage, 20  ,20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(20, 20));
        this.setOpaque(false);
        this.setVisible(true);
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

}
