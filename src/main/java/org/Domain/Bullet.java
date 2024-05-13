package org.Domain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bullet extends JComponent {
    public BufferedImage bulletImage;
    private Coordinate coordinate;
    private int yVelocity = -3;

    public Bullet(Coordinate coordinate) {
        this.coordinate = new Coordinate(coordinate.getX(), coordinate.getY());
        try {
            this.bulletImage = ImageIO.read(new File(setImageDirectory()));
            bulletImage = resizeImage(bulletImage, 20,20);

        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(bulletImage.getWidth(), bulletImage.getHeight()));
        setSize(getPreferredSize());
        setLocation(coordinate.getX() - bulletImage.getWidth() / 2, coordinate.getY() - bulletImage.getHeight() / 2);
    }

    private String setImageDirectory() {
        return "assets/bullet.png";
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bulletImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - bulletImage.getWidth()) / 2;
            int y = (getHeight() - bulletImage.getHeight()) / 2;
            g2d.drawImage(bulletImage,x ,y , (ImageObserver) this);
            g2d.dispose();
        }
    }

    public void moveUp() {
        this.coordinate.setY(this.coordinate.getY() + yVelocity);
        this.setBounds(this.coordinate.getX() - bulletImage.getWidth() / 2,
                this.coordinate.getY() - bulletImage.getHeight() / 2,
                bulletImage.getWidth(),
                bulletImage.getHeight());
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setbulletImage(BufferedImage bulletImage) {
        this.bulletImage = bulletImage;
    }

    public BufferedImage getbulletImage() {
        return bulletImage;
    }
}
