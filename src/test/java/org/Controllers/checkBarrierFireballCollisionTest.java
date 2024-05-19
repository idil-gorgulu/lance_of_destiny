/**
 * Checks for collisions between the fireball and barriers in the game.
 *
 * Requires:
 * - barriers and fireball must not be null.
 * - barriers must contain a non-null ArrayList of Barrier objects.
 * - fireball must be a non-null Fireball object.
 *
 * Modifies:
 * - Fireball's velocity and lastCollided properties.
 * - The list of barriers.
 *
 * Effects:
 * - If a collision is detected between the fireball and a barrier:
 *   - Updates the fireball's velocity based on the collision logic.
 *   - Removes the barrier if it is hit.
 */

package org.Controllers;

import org.Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class checkBarrierFireballCollisionTest {

    private Fireball fireball;
    private Barrier barrier1, barrier2;
    private ArrayList<Barrier> barriers;

    @BeforeEach
    public void setUp() {
        fireball = new Fireball();
        barrier1 = new Barrier(new Coordinate(90, 90), BarrierType.REINFORCED);
        barrier2 = new Barrier(new Coordinate(150, 150), BarrierType.SIMPLE);

        fireball.setxVelocity(0.0);
        fireball.setyVelocity(2.0);
        fireball.setCoordinate(new Coordinate(100, 100));

        barriers = new ArrayList<>();
        barriers.add(barrier1);
        barriers.add(barrier2);
    }

    public void checkBarrierFireballCollision(ArrayList<Barrier> barriers, Fireball fireball) {
        ArrayList<Barrier> toRemove = new ArrayList<>();

        double xVelocity = fireball.getxVelocity();
        double yVelocity = fireball.getyVelocity();

        Rectangle2D.Double fireballRectangle = new Rectangle2D.Double(
                fireball.getCoordinate().getX() - fireball.getFireballRadius(),
                fireball.getCoordinate().getY() - fireball.getFireballRadius(),
                2 * fireball.getFireballRadius(),
                2 * fireball.getFireballRadius()
        );

        for (Barrier br : barriers) {
            Rectangle brRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY(), (int) br.getPreferredSize().getWidth(), (int) br.getPreferredSize().getHeight());

            if (brRect.intersects(fireballRectangle)) {

                if (br == fireball.getLastCollided()) return;

                fireball.setLastCollided(br);
                if (!fireball.isOverwhelming()) {
                    Rectangle sideLRect = new Rectangle(br.getCoordinate().getX(), br.getCoordinate().getY() + 5, 1, 5);
                    Rectangle sideRRect = new Rectangle(br.getCoordinate().getX() + 50, br.getCoordinate().getY() + 5, 1, 5);

                    if (sideLRect.intersects(fireballRectangle) || sideRRect.intersects(fireballRectangle)) {
                        fireball.setxVelocity(-xVelocity);
                    } else {
                        if (xVelocity * br.getVelocity() > 0) {
                            fireball.setxVelocity(xVelocity + Math.signum(xVelocity) * 0.5);
                        } else if (xVelocity * br.getVelocity() < 0) {
                            fireball.setxVelocity(-xVelocity);
                        }
                        fireball.setyVelocity(-yVelocity);
                    }
                    if (hitBarrier(br, 1)) {
                        toRemove.add(br);
                    }
                } else {
                    if (hitBarrier(br, 10)) {
                        toRemove.add(br);
                    }
                }
            }
        }
        barriers.removeAll(toRemove);
    }

    public boolean hitBarrier(Barrier barrier, int hitTimes) {
        barrier.setnHits(barrier.getnHits() - hitTimes);
        if (barrier.getnHits() <= 0) {
            barrier.destroy();
            return true;
        }
        return false;
    }

    @Test
    public void testCollisionWithBarrier() { // Black-Box Test
        fireball.setCoordinate(new Coordinate(100, 100));
        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(2, barriers.size());
        assertEquals(0.0, fireball.getxVelocity());
        assertEquals(-2.0, fireball.getyVelocity());
    }


    // I can show this
    @Test
    public void testNoCollision() { // Black-Box Test
        fireball.setCoordinate(new Coordinate(200, 200));
        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(2, barriers.size());
    }

    @Test
    public void testSameDirectionCollision() { // Glass-Box Test
        fireball.setCoordinate(new Coordinate(100, 100));
        barrier1.setVelocity(2);
        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(2.0, fireball.getxVelocity());
    }

    @Test
    public void testOppositeDirectionCollision() { // Glass-Box Test
        fireball.setCoordinate(new Coordinate(100, 100));
        barrier1.setVelocity(-2);
        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(-2.0, fireball.getxVelocity());
    }

    @Test
    public void testMultipleBarriersCollision() { // Glass-Box Test
        barrier1.setCoordinates(new Coordinate(100, 100));
        barrier2.setCoordinates(new Coordinate(105, 100));
        fireball.setCoordinate(new Coordinate(100, 100));
        fireball.setLastCollided(null);

        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(1, barriers.size());
        assertEquals(0.0, Math.abs(fireball.getxVelocity()));
        assertEquals(2.0, fireball.getyVelocity());

        fireball.setCoordinate(new Coordinate(105, 100));
        fireball.setLastCollided(null);

        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(1, barriers.size());
    }
}
