package org.ata_ball_barrier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StaffPanel extends JPanel implements KeyListener {
    private BufferedImage lineImage;
    public int x1, y1, x2, y2;
    public double angle;


    public StaffPanel() {
        this.x1 = 50; // initial position of the line
        this.y1 = 300;
        this.x2 = 150;
        this.y2 = 400;
        this.angle = 0;

        try {
            lineImage = ImageIO.read(new File("assets/200Player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        Timer timer = new Timer(10 , e -> {
            repaint();
        });
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (lineImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
            at.rotate(angle, lineImage.getWidth() / 2.0, lineImage.getHeight() / 2.0);
            g2d.drawImage(lineImage, at, null);
            g2d.dispose();
        }
    }

    public void moveLine(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
        repaint();
    }

    public void rotateLine(double dTheta) {
        angle += dTheta;
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT:
                this.moveLine(-5, 0); // Move left
                break;
            case KeyEvent.VK_RIGHT:
                this.moveLine(5, 0); // Move right
                break;
            case KeyEvent.VK_A:
                rotateLine(-Math.toRadians(5)); // Rotate left
                break;
            case KeyEvent.VK_D:
                rotateLine(Math.toRadians(5)); // Rotate right
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Line Control");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new StaffPanel(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }


}
