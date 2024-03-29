package org.ata_ball_barrier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Fireball extends JPanel {
    private int x, y;
    private int xVelocity = 3;
    private int yVelocity = 2;
    private int fireballRadius = 15;
    private BufferedImage fireballImage;
    private Timer timer;

    public Fireball() {
        // Starting position
        this.x = 0;
        this.y = 0;
        
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
        x += xVelocity;
        y += yVelocity;

        // Boundary detection and bounce
        if (x < fireballRadius || x > getWidth() - fireballRadius) {
            xVelocity *= -1;
        }
        if (y < fireballRadius || y > getHeight() - fireballRadius) {
            yVelocity *= -1;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fireballImage != null) {
            // Center the image at (x, y)
            g.drawImage(fireballImage, x - fireballRadius, y - fireballRadius, this);
        } else {
            // Fallback to a simple red circle if image is unavailable
            g.setColor(Color.RED);
            g.fillOval(x - fireballRadius, y - fireballRadius, fireballRadius * 2, fireballRadius * 2);
        }
    }

}
