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
                runningModeController.slideMagicalStaff(-8); // Move left
                break;
            case KeyEvent.VK_RIGHT:
                runningModeController.slideMagicalStaff(+8); // Move right
                break;
            case KeyEvent.VK_A:
                runningModeController.rotateMagicalStaff(-3); // Rotate counterclockwise (degrees)
                break;
            case KeyEvent.VK_D:
                runningModeController.rotateMagicalStaff(+3); // Rotate clockwise (degrees)
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                runningModeController.slideMagicalStaff(0); // Move left
                break;
            case KeyEvent.VK_RIGHT:
                runningModeController.slideMagicalStaff(0); // Move right
                break;
            case KeyEvent.VK_A:
                runningModeController.rotateMagicalStaff(0); // Rotate counterclockwise
                break;
            case KeyEvent.VK_D:
                runningModeController.rotateMagicalStaff(0); // Rotate clockwise
                break;

                //Temporarily here -melih
            case KeyEvent.VK_Q:
                runningModeController.useSpell1();
                break;
            case KeyEvent.VK_W:
                runningModeController.useSpell2();
                break;
            case KeyEvent.VK_S:
                runningModeController.redoSpell2();
                break;
        }
    }
}
