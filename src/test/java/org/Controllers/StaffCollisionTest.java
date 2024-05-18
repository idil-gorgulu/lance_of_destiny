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
        lastCollisionTime = 0;
        runningModePage=new RunningModePage();
        runningModeController= new RunningModeController(runningModePage);
    }

    @Test
    public void collisionBeforeCooldown() {
        runningModeController.setLastCollisionTime(System.currentTimeMillis());
        Fireball fireball = game.getFireball();
        double beforex = fireball.getxVelocity();
        double beforey = fireball.getyVelocity();
        runningModeController.checkMagicalStaffFireballCollision();

        assertEquals(beforex, fireball.getxVelocity());
        assertEquals(beforey, fireball.getyVelocity());
    }

    @Test
    public void noIntersect() {
        runningModeController.setLastCollisionTime(0);
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
    @Test
    public void horizontalReflection() {
        runningModeController.setLastCollisionTime(0);
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

    @Test
    public void angledReflection() {
        runningModeController.setLastCollisionTime(0);
        Fireball fireball = game.getFireball();
        MagicalStaff magicalStaff = game.getMagicalStaff();

        fireball.setCoordinate(new Coordinate(90, 90));

        magicalStaff.setTopLeftCornerOfMagicalStaff(80, -330);
        magicalStaff.setAngle(45);
        magicalStaff.setAngVelocity(0);

        fireball.setxVelocity(3);
        fireball.setyVelocity(0);

        runningModeController.checkMagicalStaffFireballCollision();

        assertEquals(0, fireball.getxVelocity(), 0.01);
        assertEquals(3, fireball.getyVelocity(),0.01);
    }

    @Test
    public void diffVelocity() {
        runningModeController.setLastCollisionTime(0);
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