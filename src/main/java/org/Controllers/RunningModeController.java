package org.Controllers;

import org.Domain.Game;
import org.Views.RunningModePage;
import org.Domain.MagicalStaff;


public class RunningModeController {
    private RunningModePage runningModePage;
    private Game gameSession;

    public RunningModeController(RunningModePage runningModePage){
        this.runningModePage = runningModePage;
        this.gameSession = Game.getInstance();
    }


    public void rotateMagicalStaff(MagicalStaff magicalStaff, double dTheta) {
        magicalStaff.rotate(dTheta);
    }

    public Game getGameSession() {
        return gameSession;
    }
}
