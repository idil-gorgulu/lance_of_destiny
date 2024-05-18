package org.Controllers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.Domain.*;
public class StaffCollisionTest {

    private Game game;
    private Fireball fireball;
    private MagicalStaff magicalStaff;
    private long lastCollisionTime;
    private static final long COLLISION_COOLDOWN = 1000;

    @BeforeEach
    void setUp() {
        game = Game.createNewGame();
        fireball = game.getFireball();
        magicalStaff = game.getMagicalStaff();
        lastCollisionTime = 0;
    }

    @Test
    void checkBeforeCooldown() {
        lastCollisionTime = System.currentTimeMillis();
        fireball.setCoordinate(new Coordinate(50, 50));

        double beforex=fireball.getxVelocity();
        double beforey=fireball.getxVelocity();

        checkMagicalStaffFireballCollision();

        assertEquals(beforex, fireball.getxVelocity());
        assertEquals(beforey, fireball.getyVelocity());
    }

    @Test
    void reflectAtZeroDegree() {
        fireball.setCoordinate(new Coordinate(50, 50));

        fireball.setxVelocity(3);
        fireball.setxVelocity(3);
        checkMagicalStaffFireballCollision();

        assertEquals(3, fireball.getxVelocity());
        assertEquals(-3, fireball.getyVelocity());
    }

    @Test
    void reflectAt45Degree() {
        fireball.setCoordinate(new Coordinate(50, 50));
        fireball.setxVelocity(3);
        fireball.setyVelocity(0);

        magicalStaff.setAngVelocity(0);
        magicalStaff.setAngle(45);

        checkMagicalStaffFireballCollision();

        assertEquals(0, fireball.getxVelocity());
        assertEquals(3, fireball.getyVelocity());
    }

    @Test
    void checkNotIntersects() {
        fireball.setCoordinate(new Coordinate(500, 500));
        fireball.setxVelocity(5);
        fireball.setyVelocity(15);


        magicalStaff.setAngle(0);

        checkMagicalStaffFireballCollision();

        assertEquals(5, fireball.getxVelocity());
        assertEquals(15, fireball.getyVelocity());
    }

    @Test
    void reflectAtNeg45Degree() {
        fireball.setCoordinate(new Coordinate(450, 90));
        fireball.setxVelocity(10);
        fireball.setyVelocity(10);


        magicalStaff.setAngle(45);

        checkMagicalStaffFireballCollision();

        assertEquals(10, fireball.getxVelocity());
        assertEquals(10, fireball.getyVelocity());
    }


    private void checkMagicalStaffFireballCollision() {
    }
}