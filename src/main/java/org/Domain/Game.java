package org.Domain;

import org.Controllers.RunningModeController;

public class Game {

    private Fireball fireball;
    private MagicalStaff magicalStaff;

    // This will be a list
    private Barrier Barrier;
    private Chance chance;
    private Score score;
    public Game(){
        this.fireball = new Fireball();
        this.magicalStaff = new MagicalStaff();
        // Think about how to initialize it, from constuctor maybe?
        this.Barrier = new Barrier(new Coordinate(450, 600), 0);

        this.chance= new Chance(3);
        this.score= new Score();
    }

    public Fireball getFireball() {
        return fireball;
    }

    public MagicalStaff getMagicalStaff() {
        return magicalStaff;
    }

    public Barrier getBarrier() { return Barrier;}

    public Chance getChance() { return chance;}
    public Score getScore(){return score;}
}
