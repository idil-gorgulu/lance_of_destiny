package org.Domain;

import org.Controllers.RunningModeController;

public class Game {

    private Fireball fireball;
    private MagicalStaff magicalStaff;

    // This will be a list
    private Barrier Barrier;
    public Game(){
        this.fireball = new Fireball();
        this.magicalStaff = new MagicalStaff();
        // Think about how to initialize it, from constuctor maybe?
        this.Barrier = new Barrier(new Coordinate(500, 500), 0);
    }

    public Fireball getFireball() {
        return fireball;
    }

    public MagicalStaff getMagicalStaff() {
        return magicalStaff;
    }

    public Barrier getBarrier() { return Barrier;}
}
