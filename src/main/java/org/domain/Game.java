package org.domain;

import org.ata_ball_barrier.Fireball;
import org.ata_ball_barrier.MagicalStaff;

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
