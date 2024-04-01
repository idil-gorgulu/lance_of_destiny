package org.ata_ball_barrier;

import org.domain.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MagicalStaff extends JPanel {
    private BufferedImage magicalStaffImage;
    private double angle;
    private Coordinate coordinate;

    public MagicalStaff() {
        this.coordinate = new Coordinate(200,200);
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

    public void slideMagicalStaff(MagicalStaff magicalStaff, int dx, int dy) {
        Point currentLocation = magicalStaff.getLocation();
        int newX = currentLocation.x + dx;
        int newY = currentLocation.y + dy;
        magicalStaff.setLocation(newX, newY);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(Coordinate coordinate) {this.coordinate = coordinate;}
}
