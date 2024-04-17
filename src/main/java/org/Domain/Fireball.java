package org.Domain;
import org.Domain.Coordinate;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Fireball extends JPanel {
    // Deneme
    private Coordinate coordinate;
    private int xVelocity = 6;
    private int yVelocity = 6;
    private int fireballRadius = 15;
    private BufferedImage fireballImage;

    public Fireball() {
        this.coordinate = new Coordinate(200, 200);

//        try {
//            fireballImage = ImageIO.read(new File("assets/200Fireball.png"));
//            this.fireballRadius = Math.max(fireballImage.getWidth(), fireballImage.getHeight()) / 2;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        setPreferredSize(new Dimension(getFireballRadius() * 2,getFireballRadius() * 2));
//
//

        try {
            // Load the original fireball image
            BufferedImage originalImage = ImageIO.read(new File("assets/200Fireball.png"));
            // Resize the image to 16x16 pixels
            fireballImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = fireballImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, 16, 16, null);
            g2d.dispose();
            // Update the fireball radius
            this.fireballRadius = 8;
        } catch (IOException e) {
            e.printStackTrace();

            setPreferredSize(new Dimension(fireballRadius * 2, fireballRadius * 2));
        }
    }
    public void moveFireball(){
        // change this so that coordinate will hande the update of the location
        this.getCoordinate().setX(this.getCoordinate().getX() + this.xVelocity);
        this.getCoordinate().setY(this.getCoordinate().getY() + this.yVelocity);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fireballImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            int x = (getWidth() - fireballImage.getWidth()) / 2;
            int y = (getHeight() - fireballImage.getHeight()) / 2;
            g2d.drawImage(fireballImage, x, y, this);
            g2d.dispose();
        }
    }


    public Coordinate getCoordinate() {return coordinate;}

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getFireballRadius() {
        return fireballRadius;
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public int getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }


}
