package org.ata_ball_barrier;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MagicalStaff extends JPanel {
    private BufferedImage magicalStaffImage;
    public double angle;

    public MagicalStaff() {
        this.angle = 0;
        try {
            magicalStaffImage = ImageIO.read(new File("assets/200Player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(magicalStaffImage.getWidth(), magicalStaffImage.getHeight()));
        Timer timer = new Timer(10, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (magicalStaffImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            AffineTransform at = AffineTransform.getRotateInstance(angle, getWidth() / 2.0, getHeight() / 2.0);
            g2d.drawImage(magicalStaffImage, at, this);
            g2d.dispose();
        }
    }

    public void rotate(double dTheta) {
        angle += dTheta;
        repaint();
    }
}
