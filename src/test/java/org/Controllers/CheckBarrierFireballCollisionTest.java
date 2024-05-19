package org.Controllers;
import org.Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class CheckBarrierFireballCollisionTest {

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

    // Black-box test: Checks collision with a barrier at the fireball's initial position.
    @Test
    public void testCollisionWithBarrier() {
        fireball.setCoordinate(new Coordinate(100, 100));
        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(2, barriers.size()); // Barrier should not be destroyed in this case.
        assertEquals(0.0, fireball.getxVelocity()); // X velocity should not change.
        assertEquals(-2.0, fireball.getyVelocity()); // Y velocity should reverse.
    }

    // Black-box test: Checks no collision scenario.
    @Test
    public void testNoCollision() {
        fireball.setCoordinate(new Coordinate(200, 200));
        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(2, barriers.size()); // No barriers should be removed.
    }

    // Glass-box test: Checks collision with a barrier moving in the same direction.
    @Test
    public void testSameDirectionCollision() {
        fireball.setCoordinate(new Coordinate(100, 100));
        barrier1.setVelocity(2);
        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(2.0, fireball.getxVelocity()); // Fireball should gain velocity.
    }

    // Glass-box test: Checks collision with a barrier moving in the opposite direction.
    @Test
    public void testOppositeDirectionCollision() {
        fireball.setCoordinate(new Coordinate(100, 100));
        barrier1.setVelocity(-2);
        checkBarrierFireballCollision(barriers, fireball);
        assertEquals(-2.0, fireball.getxVelocity()); // Fireball should reverse its X velocity.
    }

    // Glass-box test: Checks multiple barrier collisions.
    @Test
    public void testMultipleBarriersCollision() {
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
