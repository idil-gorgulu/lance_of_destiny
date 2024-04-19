package org.Domain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Debris extends JPanel {

    private BufferedImage debrisImage;

    private Coordinate coordinate;

    private int xVelocity = 3;
    private int yVelocity = 3;
    public Debris(Coordinate coordinate) {
        this.coordinate = coordinate;
        try {
            this.debrisImage = ImageIO.read(new File(setImageDirectory()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(debrisImage.getWidth(), debrisImage.getHeight()));
    }

    private String setImageDirectory() {
            return "assets/debris.png";
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (debrisImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - debrisImage.getWidth()) / 2;
            int y = (getHeight() - debrisImage.getHeight()) / 2;
            g2d.drawImage(debrisImage,x ,y , (ImageObserver) this);
            g2d.dispose();
        }
    }

    public void moveDebris() {
        int newY = getCoordinate().getY() + 3;
        getCoordinate().setY(newY);
        repaint();
        revalidate();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
