package org.Controllers;

import org.Domain.Game;
import org.Domain.MagicalStaff;
import org.Views.RunningModePage;

public class RunningModeController {
    private RunningModePage runningModePage;
    private Game gameSession;

    public RunningModeController(RunningModePage runningModePage){
        this.runningModePage = runningModePage;
        this.gameSession = new Game();
    }

    public void rotateMagicalStaff(MagicalStaff magicalStaff, double dTheta) {
        magicalStaff.rotate(dTheta);
    }

    public Game getGameSession() {
        return gameSession;
    }
}
