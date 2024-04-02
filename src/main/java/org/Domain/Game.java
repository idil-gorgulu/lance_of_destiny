package org.Domain;

import org.Controllers.RunningModeController;

public class Game {

    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private RunningModeController runningModeController;
    public Game(RunningModeController runningModeController){
        this.runningModeController = runningModeController;
        this.fireball = new Fireball();
        this.magicalStaff = new MagicalStaff();
    }

    public Fireball getFireball() {
        return fireball;
    }

    public MagicalStaff getMagicalStaff() {
        return magicalStaff;
    }
}
