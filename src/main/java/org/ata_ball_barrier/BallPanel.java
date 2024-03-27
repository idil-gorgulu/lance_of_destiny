package org.ata_ball_barrier;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

public class BallPanel extends JPanel {
    private int delay = 10;
    private Timer timer;

    private int x = 0;
    private int y = 0;
    private int dx = 4;
    private int dy = 4;

    private int radius = 15;

    public BallPanel()
    {

        timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                repaint();
            }

        });

        timer.start();
    }

    public void ballMovement() {
        if (x < radius) {
            dx = Math.abs(dx);
        }
        if (x > getWidth() - radius){
            dx = -Math.abs(dx);
        }
        if (y < radius){
            dy = Math.abs(dy);
        }
        if (y > getHeight() - radius){
            dy = -Math.abs(dy);
        }

        x += dx;
        y += dy;
    }


    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        g.setColor(Color.red);


        g.fillOval(x - radius, y - radius, radius*2, radius*2);
    }



}