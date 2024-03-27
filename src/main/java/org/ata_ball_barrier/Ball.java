package org.ata_ball_barrier;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class Ball {
    private int x, y, diameter;
    private int xSpeed = 3;
    private int ySpeed = 2;
    private JPanel canvas;
    private BufferedImage ballImage;

    public Ball(int x, int y, int diameter, BufferedImage ballImage, JPanel canvas) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.ballImage = ballImage;
        this.canvas = canvas;
    }

    public void move(StaffPanel lineImage) {
        if (x + diameter >= canvas.getWidth() || x <= 0) {
            xSpeed *= -1;
        }
        if (y + diameter >= canvas.getHeight() || y <= 0) {
            ySpeed *= -1;
        }

        // Check collision with the line
        if (collidesWithLine(lineImage)) {
            xSpeed *= -1;
            ySpeed *= -1;
        }

        x += xSpeed;
        y += ySpeed;
    }

    private boolean collidesWithLine(StaffPanel lineImage) {
        // Calculate line coordinates after rotation
        double x1Rotated = lineImage.x1 + (lineImage.getWidth() / 2.0) * Math.cos(lineImage.angle) - (lineImage.getHeight() / 2.0) * Math.sin(lineImage.angle);
        double y1Rotated = lineImage.y1 + (lineImage.getWidth() / 2.0) * Math.sin(lineImage.angle) + (lineImage.getHeight() / 2.0) * Math.cos(lineImage.angle);
        double x2Rotated = lineImage.x2 + (lineImage.getWidth() / 2.0) * Math.cos(lineImage.angle) - (lineImage.getHeight() / 2.0) * Math.sin(lineImage.angle);
        double y2Rotated = lineImage.y2 + (lineImage.getWidth() / 2.0) * Math.sin(lineImage.angle) + (lineImage.getHeight() / 2.0) * Math.cos(lineImage.angle);

        // Calculate distance from the ball's center to the line
        double distance = Math.abs((y2Rotated - y1Rotated) * x - (x2Rotated - x1Rotated) * y + x2Rotated * y1Rotated - y2Rotated * x1Rotated) / Math.sqrt(Math.pow(y2Rotated - y1Rotated, 2) + Math.pow(x2Rotated - x1Rotated, 2));

        // Check if the distance is less than the sum of ball radius and half the line width
        return distance <= diameter / 2 + Math.max(lineImage.getWidth(), lineImage.getHeight()) / 2;
    }

    public void draw(Graphics g) {
        g.drawImage(ballImage, x, y, null);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}