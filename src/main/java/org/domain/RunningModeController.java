package org.domain;

import org.ata_ball_barrier.MagicalStaff;
import org.oguz_swing_ui.RunningModePage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RunningModeController {
    private RunningModePage runningModePage;
    private Game gameSession;

    public RunningModeController(RunningModePage runningModePage){
        this.runningModePage = runningModePage;
        this.gameSession = new Game(this);
    }

    /*
    @Override
    public void keyPressed(KeyEvent e) {
        MagicalStaff magicalStaff = this.runningModePage.getMagicalStaff();
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT:
                slideMagicalStaff(magicalStaff, -5, 0); // Move left
                break;
            case KeyEvent.VK_RIGHT:
                slideMagicalStaff(magicalStaff,5, 0); // Move right
                break;
            case KeyEvent.VK_A:
                rotateMagicalStaff(magicalStaff, -Math.toRadians(5)); // Rotate left
                break;
            case KeyEvent.VK_D:
                rotateMagicalStaff(magicalStaff, Math.toRadians(5)); // Rotate right
                break;
        }
    }
     */



    public void rotateMagicalStaff(MagicalStaff magicalStaff, double dTheta) {
        magicalStaff.rotate(dTheta);
    }

    public Game getGameSession() {
        return gameSession;
    }
}
