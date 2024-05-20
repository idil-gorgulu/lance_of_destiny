package org.Controllers;

import org.Domain.Barrier;
import org.Domain.BarrierType;
import org.Domain.Coordinate;
import org.Views.RunningModePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class moveBarrierTest {
    private Barrier barrier;

    @BeforeEach
    void setUp() {
        // Setting up a non-moving, non-explosive barrier by default.
        barrier = new Barrier(new Coordinate(100, 100), BarrierType.SIMPLE);
        barrier.setMoving(false);
    }

    @Test
    void testMoveNonExplosiveBarrier() {
        // This test checks if a non-explosive barrier moves correctly to the right when set to move.
        // This is an example of black box testing, as it tests functionality without knowing internal implementations.

        barrier.setVelocity(5);
        barrier.setMoving(true); // Allow barrier to move.
        barrier.moveBarrier();
        assertEquals(105, barrier.getCoordinate().getX(), "Barrier should move to the right by 5 units.");
    }

    @Test
    void testNonExplosiveBarrierReverseAtBoundary() {
        // This test verifies that a non-explosive barrier reverses direction when it hits a boundary.
        // This is an example of glass box testing, as it depends on knowledge of boundary handling and velocity inversion.

        barrier.setVelocity(5);
        barrier.setMoving(true);
        // Simulating barrier at the right edge of the screen.
        barrier.getCoordinate().setX(RunningModePage.SCREENWIDTH - barrier.getPreferredSize().width - 5);
        barrier.moveBarrier();
        assertEquals(-5, barrier.getVelocity(), "Barrier should reverse direction at screen edge.");
    }

    @Test
    void testMoveExplosiveBarrierCircular() {
        // Testing if an explosive barrier moves in a circular pattern as intended.
        // This is a black box test, focusing on the expected movement behavior without internal state details.

        // Setting up an explosive barrier that moves.
        Barrier explosiveBarrier = new Barrier(new Coordinate(100, 100), BarrierType.EXPLOSIVE);
        explosiveBarrier.setMoving(true);
        explosiveBarrier.setVelocity(3);
        double initialX = explosiveBarrier.getCoordinate().getX();
        double initialY = explosiveBarrier.getCoordinate().getY();
        explosiveBarrier.moveBarrier();
        // Check that the barrier moved in a circle (simple test for different coordinates).
        assertNotEquals(initialX, explosiveBarrier.getCoordinate().getX(), "Explosive barrier should move in x-axis.");
        assertNotEquals(initialY, explosiveBarrier.getCoordinate().getY(), "Explosive barrier should move in y-axis.");
    }

    @Test
    void testExplosiveBarrierStayWithinBoundaries() {
        // This test ensures that an explosive barrier does not move outside the predetermined boundaries.
        // It is a glass box test as it requires knowledge about how boundary conditions are implemented in the system.

        // Setup an explosive barrier at the edge of its movement limit.
        Barrier explosiveBarrier = new Barrier(new Coordinate(100, 100), BarrierType.EXPLOSIVE);
        explosiveBarrier.setMoving(true);
        explosiveBarrier.setVelocity(3);
        explosiveBarrier.getCoordinate().setX(0); // Placing it at the edge.
        explosiveBarrier.moveBarrier();
        // Assuming boundary checks prevent going negative.
        assertTrue(explosiveBarrier.getCoordinate().getX() >= 0, "Barrier should not move beyond screen boundary.");
    }
}
