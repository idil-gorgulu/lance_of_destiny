package org.Controllers;

import org.Domain.MagicalStaff;
import org.Views.RunningModePage;
import org.Domain.Game;

public class MagicalStaffController {
    private Game game;
    private RunningModePage runningModePage;
    MagicalStaff magicalStaff;

    public MagicalStaffController(RunningModePage runningModePage){
        this.magicalStaff = new MagicalStaff();
    }

}
