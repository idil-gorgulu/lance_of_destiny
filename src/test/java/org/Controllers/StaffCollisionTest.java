package org.Controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.Views.RunningModePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.Domain.*;
public class StaffCollisionTest {

    private Game game;
    private RunningModePage runningModePage;
    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private long lastCollisionTime;
    private static final long COLLISION_COOLDOWN = 1000;
    RunningModeController runningModeController;

    @BeforeEach
    void setUp() {
        game = Game.createNewGame();
        fireball = game.getFireball();
        magicalStaff = game.getMagicalStaff();
        runningModePage=new RunningModePage();
        runningModeController= new RunningModeController(runningModePage);
    }

    //Black Box
    //there shouldnt be a collision before the cooldown, this test compares the before and after the "collision"
    @Test
    public void collisionBeforeCooldown() {
        game.setLastCollisionTime(System.currentTimeMillis());
        Fireball fireball = game.getFireball();
        double beforex = fireball.getxVelocity();
        double beforey = fireball.getyVelocity();
        runningModeController.checkMagicalStaffFireballCollision();

        assertEquals(beforex, fireball.getxVelocity());
        assertEquals(beforey, fireball.getyVelocity());
    }

    // Glass box
    //Checking the case where the ball and staff dont intersect, fireball velocities should not change
    @Test
    public void noIntersect() {
        game.setLastCollisionTime(0);
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();

        fireball.setCoordinate(new Coordinate(500, 500));
        magicalStaff.setTopLeftCornerOfMagicalStaff(0, 0);

        double beforeX = fireball.getxVelocity();
        double beforeY = fireball.getyVelocity();

       runningModeController.checkMagicalStaffFireballCollision();

        assertEquals(beforeX, fireball.getxVelocity());
        assertEquals(beforeY, fireball.getyVelocity());
    }

    //Glass Box
    //Collision when staff is horizontal (angle=0). Only the y component should change to -y.
    @Test
    public void horizontalReflection() {
        game.setLastCollisionTime(0);
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();

        fireball.setCoordinate(new Coordinate(90, 90));
        magicalStaff.setTopLeftCornerOfMagicalStaff(90, -360);
        magicalStaff.setAngle(0);

        fireball.setxVelocity(3);
        fireball.setyVelocity(3);

        runningModeController.checkMagicalStaffFireballCollision();

        assertEquals(3, fireball.getxVelocity());
        assertEquals(-3, fireball.getyVelocity());
    }

    //Glass Box
    //Collision when staff angle=45. x,y --> y,x
    @Test
    public void angledReflection() {
        game.setLastCollisionTime(0);
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();

        fireball.setCoordinate(new Coordinate(90, 90));

        magicalStaff.setTopLeftCornerOfMagicalStaff(80, -330);
        magicalStaff.setAngle(45);
        magicalStaff.setAngVelocity(0);

        fireball.setxVelocity(3);
        fireball.setyVelocity(5);

        runningModeController.checkMagicalStaffFireballCollision();

        assertEquals(5, fireball.getxVelocity(), 0.01);
        assertEquals(3, fireball.getyVelocity(),0.01);
    }

    //Glass box
    // Checking collisions at unexpected speeds.
    @Test
    public void diffVelocity() {
        game.setLastCollisionTime(0);
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();

        fireball.setCoordinate(new Coordinate(90, 90));
        magicalStaff.setTopLeftCornerOfMagicalStaff(90, -360);
        magicalStaff.setAngle(0);

        fireball.setxVelocity(30);
        fireball.setyVelocity(-15);

        runningModeController.checkMagicalStaffFireballCollision();

        assertEquals(30, fireball.getxVelocity());
        assertEquals(15, fireball.getyVelocity());
    }
}