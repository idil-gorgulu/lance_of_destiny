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

    public void rotateMagicalStaff(MagicalStaff magicalStaff, double dTheta) {
        magicalStaff.rotate(dTheta);
    }

    public Game getGameSession() {
        return gameSession;
    }
}
