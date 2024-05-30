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
                runningModeController.getGameSession().getMagicalStaff().setReleased(false); //TODO dont talk to strangers
                break;
            case KeyEvent.VK_D:
                runningModeController.rotateMagicalStaff(+3); // Rotate clockwise (degrees)
                runningModeController.getGameSession().getMagicalStaff().setReleased(false);
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
                runningModeController.rotateMagicalStaff(3); // Rotate counterclockwise
                runningModeController.getGameSession().getMagicalStaff().setReleased(true);//TODO dont talk to strangers
                break;
            case KeyEvent.VK_D:
                runningModeController.rotateMagicalStaff(-3); // Rotate clockwise
                runningModeController.getGameSession().getMagicalStaff().setReleased(true);
                break;
            case KeyEvent.VK_Q:
                runningModeController.useFelixFelicis();
                break;
            case KeyEvent.VK_W:
                runningModeController.useMSExpansion();
                break;
            case KeyEvent.VK_E:
                runningModeController.useOverwhelmingFB();
                break;
            case KeyEvent.VK_T:
                runningModeController.useHex();
                break;
            case KeyEvent.VK_P:
                runningModeController.volume(1);
                break;
            case KeyEvent.VK_O:
                runningModeController.volume(-1);
                break;
        }

    }
}
