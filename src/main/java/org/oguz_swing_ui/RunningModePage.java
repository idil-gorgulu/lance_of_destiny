package org.oguz_swing_ui;

import org.ata_ball_barrier.Fireball;
import org.ata_ball_barrier.MagicalStaff;
import org.domain.RunningModeController;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RunningModePage extends Page implements KeyListener {

    private Fireball fireball;
    private MagicalStaff magicalStaff;

    private RunningModeController runningModeController;
    public RunningModePage() {
        super();
        this.runningModeController = new RunningModeController(this);
        initUI();
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
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

                // Initializing MagicalStaff
                magicalStaff = runningModeController.getGameSession().getMagicalStaff();
                int magicalStaffWidth = magicalStaff.getPreferredSize().width;
                int magicalStaffHeight = magicalStaff.getPreferredSize().height;
                int magicalStaffPositionX = (screenWidth - magicalStaffWidth) / 2;
                int magicalStaffPositionY = (screenHeight - magicalStaffHeight - 50);
                magicalStaff.getCoordinate().setX(magicalStaffPositionX);
                magicalStaff.getCoordinate().setY(magicalStaffPositionY);
                magicalStaff.setBounds(magicalStaff.getCoordinate().getX(), magicalStaff.getCoordinate().getY(), magicalStaff.getPreferredSize().width, magicalStaff.getPreferredSize().height);
                add(magicalStaff);

                // Initializing Fireball
                fireball = runningModeController.getGameSession().getFireball();
                int fireballWidth = fireball.getPreferredSize().width;
                int fireballPositionX = (screenWidth - fireballWidth) / 2;
                int fireballHeight = fireball.getPreferredSize().height;
                int fireballPositionY = (screenHeight - fireballHeight - 100);
                fireball.getCoordinate().setX(fireballPositionX);
                fireball.getCoordinate().setY(fireballPositionY);
                fireball.setBounds(fireball.getCoordinate().getX(), fireball.getCoordinate().getY(), fireballWidth, fireball.getPreferredSize().height);
                add(fireball);

                repaint();
                revalidate();
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
                runningModeController.getGameSession().getMagicalStaff().slideMagicalStaff(magicalStaff, -5, 0); // Move left
                break;
            case KeyEvent.VK_RIGHT:
                runningModeController.getGameSession().getMagicalStaff().slideMagicalStaff(magicalStaff, +5, 0); // Move left
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
}
