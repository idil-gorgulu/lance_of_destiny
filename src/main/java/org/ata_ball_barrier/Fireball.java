package org.ata_ball_barrier;
import org.domain.Coordinate;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Fireball extends JPanel {
    private Coordinate coordinate;
    private int xVelocity = 3;
    private int yVelocity = 2;
    private int fireballRadius = 15;
    private BufferedImage fireballImage;

    public Fireball() {
        this.coordinate = new Coordinate(200,200);
        try {
            fireballImage = ImageIO.read(new File("assets/200Fireball.png"));
            this.fireballRadius = Math.max(fireballImage.getWidth(), fireballImage.getHeight()) / 2;
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(fireballImage.getWidth(), fireballImage.getHeight()));
        Timer timer = new Timer(10 , e -> repaint());
        timer.start();
    }

    private void moveFireball() {
        // Update position
        coordinate.setX(coordinate.getX() + xVelocity);
        coordinate.setY(coordinate.getY() + yVelocity);

        // Boundary detection and bounce
        if (coordinate.getX() < fireballRadius || coordinate.getX() > getWidth() - fireballRadius) {
            xVelocity *= -1;
        }
        if (coordinate.getY() < fireballRadius || coordinate.getY() > getHeight() - fireballRadius) {
            yVelocity *= -1;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fireballImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Calculate the top-left corner coordinates for centering the image
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
}
