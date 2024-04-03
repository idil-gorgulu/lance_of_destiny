package org.Views;

import org.Controllers.MagicalStaffController;
import org.Domain.Barrier;
import org.Domain.Fireball;
import org.Domain.MagicalStaff;
import org.Controllers.RunningModeController;
import org.Domain.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RunningModePage extends Page implements KeyListener {

    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private Barrier barrier;
    //I dont think this is a good way
    private RunningModeController runningModeController;

    private MagicalStaffController magicalStaffController;

    public RunningModePage() {
        super();
        this.runningModeController = new RunningModeController(this);
        this.magicalStaffController = new MagicalStaffController(this);
        initUI();
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        setupTimer();
    }

    private void setupTimer() {
        int delay = 16; // Roughly 60 FPS, adjust as needed
        Timer timer = new Timer(delay, e -> updateGame());
        timer.start();
    }

    private void updateGame() {
        checkCollision();
        this.runningModeController.getGameSession().getFireball().moveFireball();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                magicalStaff = runningModeController.getGameSession().getMagicalStaff();
                magicalStaff.setBounds(magicalStaff.getCoordinate().getX(), magicalStaff.getCoordinate().getY(), magicalStaff.getPreferredSize().width, magicalStaff.getPreferredSize().height);
                fireball = runningModeController.getGameSession().getFireball();
                fireball.setBounds(fireball.getCoordinate().getX(), fireball.getCoordinate().getY(), fireball.getWidth(), fireball.getPreferredSize().height);
                barrier = runningModeController.getGameSession().getBarrier();
                barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getWidth(), barrier.getHeight());
                //System.out.println(barrier.getCoordinates().getX());
                //System.out.println(barrier.getCoordinates().getY());
                repaint();
            }
        });
    }

    @Override
    protected void initUI() {
        setLayout(null);
        initializeGameObjects();
    }

    private void initializeGameObjects() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int screenWidth = getWidth();
                int screenHeight = getHeight();

                // Initializing Fireball
                fireball = runningModeController.getGameSession().getFireball();
                int fireballWidth = fireball.getPreferredSize().width;
                int fireballPositionX = (screenWidth - fireballWidth) / 2;
                int fireballHeight = fireball.getPreferredSize().height;
                int fireballPositionY = (screenHeight - fireballHeight - 200);
                fireball.getCoordinate().setX(fireballPositionX);
                fireball.getCoordinate().setY(fireballPositionY);
                fireball.setBounds(fireball.getCoordinate().getX(), fireball.getCoordinate().getY(), fireballWidth, fireball.getPreferredSize().height);
                fireball.setBackground(Color.red);
                add(fireball);
                //System.out.println(fireball.getCoordinate().getX());
                //System.out.println(fireball.getCoordinate().getY());

                // Initializing MagicalStaff
                magicalStaff = runningModeController.getGameSession().getMagicalStaff();
                int magicalStaffWidth = magicalStaff.getPreferredSize().width;
                int magicalStaffHeight = magicalStaff.getPreferredSize().height;
                int magicalStaffPositionX = (screenWidth - magicalStaffWidth) / 2;
                int magicalStaffPositionY = (screenHeight - magicalStaffHeight - 50);
                magicalStaff.getCoordinate().setX(magicalStaffPositionX);
                magicalStaff.getCoordinate().setY(magicalStaffPositionY);
                magicalStaff.setBounds(magicalStaff.getCoordinate().getX(), magicalStaff.getCoordinate().getY(), magicalStaff.getPreferredSize().width, magicalStaff.getPreferredSize().height);
                magicalStaff.setBackground(Color.green);
                requestFocus();
                add(magicalStaff);


                barrier = runningModeController.getGameSession().getBarrier();
                barrier.setBounds(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), barrier.getPreferredSize().width, barrier.getPreferredSize().height);
                barrier.setBackground(Color.blue);
                add(barrier);
                repaint();
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                runningModeController.getGameSession().getMagicalStaff().slideMagicalStaff(-5, 0); // Move left
                break;
            case KeyEvent.VK_RIGHT:
                runningModeController.getGameSession().getMagicalStaff().slideMagicalStaff(+5, 0); // Move left
                break;
            case KeyEvent.VK_A:
                runningModeController.getGameSession().getMagicalStaff().rotate(-Math.toRadians(5)); // Rotate left
                break;
            case KeyEvent.VK_D:
                runningModeController.getGameSession().getMagicalStaff().rotate(+Math.toRadians(5)); // Rotate left
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void checkCollision() {
        int fireballX = fireball.getCoordinate().getX();
        int fireballY = fireball.getCoordinate().getY();
        int fireballRadius = fireball.getFireballRadius();

        int magicalStaffX = magicalStaff.getCoordinate().getX();
        int magicalStaffY = magicalStaff.getCoordinate().getY();
        int magicalStaffWidth = magicalStaff.getPreferredSize().width;
        int magicalStaffHeight = magicalStaff.getPreferredSize().height;
        double magicalStaffAngle = magicalStaff.getAngle();


        int xVelocity = fireball.getxVelocity();
        int yVelocity = fireball.getyVelocity();
        double normalAngle = (magicalStaffAngle + 90) % 360;

        Rectangle staffRect = new Rectangle(magicalStaffX, magicalStaffY, magicalStaffWidth, magicalStaffHeight);
        Rectangle fireballRect = new Rectangle(fireballX - fireballRadius, fireballY - fireballRadius, fireballRadius * 2, fireballRadius * 2);
        Rectangle barrierRect = new Rectangle(barrier.getCoordinates().getX(), barrier.getCoordinates().getY(), (int) barrier.getPreferredSize().getWidth(), (int) barrier.getPreferredSize().getHeight());

        if (staffRect.intersects(fireballRect)) {
            // The collision formula: Vnew = b * (-2*(V dot N)*N + V)
            // b: 1 for elastic collision, 0 for 100% moment loss
            // V: previous velocity vector
            // N: normal vector of the surface collided with
            double b = 1.0; // b = 1 for a perfect elastic collision
            double normalAngleRadians = Math.toRadians(normalAngle);
            Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
            Vector velocity = new Vector(xVelocity, yVelocity);
            Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);
            fireball.setxVelocity((int) vNew.getX());
            fireball.setyVelocity((int) vNew.getY());
        } else if (barrierRect.intersects(fireballRect)) {

            double b = 1.0; // b = 1 for a perfect elastic collision
            double normalAngleRadians = Math.toRadians((double) (90%360));
            Vector normal = new Vector(Math.cos(normalAngleRadians), Math.sin(normalAngleRadians));
            Vector velocity = new Vector(xVelocity, yVelocity);
            Vector vNew = velocity.subtract(normal.scale(2 * velocity.dot(normal))).scale(b);
            fireball.setxVelocity((int) vNew.getX());
            fireball.setyVelocity((int) vNew.getY());
        }

        int containerWidth = getWidth();
        int containerHeight = getHeight();

        // Check collision with left and right boundaries
        if (fireballX - fireballRadius <= 0 || fireballX + fireballRadius >= containerWidth) {
            xVelocity *= -1; // Reverse X velocity
            fireball.setxVelocity(xVelocity);
        }

        // Check collision with top and bottom boundaries
        if (fireballY - fireballRadius <= 0 || fireballY + fireballRadius >= containerHeight) {
            yVelocity *= -1; // Reverse Y velocity
            fireball.setyVelocity(yVelocity);
        }
    }


}

