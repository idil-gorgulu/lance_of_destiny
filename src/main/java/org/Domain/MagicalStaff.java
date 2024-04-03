package org.Domain;

import org.Domain.Coordinate;

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
        Dimension newSize = calculateRotatedDimensions(angle);
        setPreferredSize(newSize);
        revalidate();
        repaint();
    }

    public void slideMagicalStaff(int dx, int dy) {
        this.getCoordinate().setX(this.getCoordinate().getX() + dx);
        this.getCoordinate().setY(this.getCoordinate().getY() + dy);
        repaint();
    }

    private Dimension calculateRotatedDimensions(double angle) {
        double radians = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.floor(magicalStaffImage.getWidth() * cos + magicalStaffImage.getHeight() * sin);
        int newHeight = (int) Math.floor(magicalStaffImage.getHeight() * cos + magicalStaffImage.getWidth() * sin);
        return new Dimension(newWidth, newHeight);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(Coordinate coordinate) {this.coordinate = coordinate;}

    public double getAngle() {
        return angle;
    }
}
