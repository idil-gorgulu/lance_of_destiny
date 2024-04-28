package org.Domain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Debris extends JComponent {
    public BufferedImage debrisImage;
    private Coordinate coordinate;
    private int yVelocity = 3;

    public Debris(Coordinate coordinate) {
        this.coordinate = new Coordinate(coordinate.getX(), coordinate.getY());
        try {
            this.debrisImage = ImageIO.read(new File(setImageDirectory()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(debrisImage.getWidth(), debrisImage.getHeight()));
        setSize(getPreferredSize());
        setLocation(coordinate.getX() - debrisImage.getWidth() / 2, coordinate.getY() - debrisImage.getHeight() / 2);
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

    public void moveDown() {
        this.coordinate.setY(this.coordinate.getY() + yVelocity);
        this.setBounds(this.coordinate.getX() - debrisImage.getWidth() / 2,
                this.coordinate.getY() - debrisImage.getHeight() / 2,
                debrisImage.getWidth(),
                debrisImage.getHeight());
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

}
