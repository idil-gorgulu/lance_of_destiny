package org.Views;

import org.Controllers.RunningModeController;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyKeyListener extends KeyAdapter {
    private RunningModeController runningModeController;  //will be staff controller

    public MyKeyListener(RunningModeController controller) {
        this.runningModeController = controller;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
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
}
