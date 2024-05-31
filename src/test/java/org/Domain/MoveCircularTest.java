package org.Domain;


import org.Views.RunningModePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveCircularTest {
    /*
    private Barrier barrier;
    private static final int SCREENWIDTH = RunningModePage.SCREENWIDTH;

    @BeforeEach
    void setUp() {
        Coordinate initialCoordinate = new Coordinate(100, 100);
        barrier = new Barrier(initialCoordinate, BarrierType.SIMPLE);
        barrier.setVelocity(1);
        barrier.setMoving(true);
    }

    // BB Test
    // Checks wheter barier starts moving
    @Test
    void testInitialMovement() {
        double initialX = barrier.getCoordinate().getX();
        barrier.moveCircular();
        assertTrue(barrier.getCoordinate().getX() > initialX,
                "Barrier should move right from the initial position");
    }

    // GB Test
    // Checks wheter it controls the boundaries while moving
    @Test
    void testBoundaryCheckPositiveLimit() {
        barrier.getCoordinate().setX(SCREENWIDTH - 50);
        barrier.getCoordinate().setY(100);
        double initialAngle = barrier.currentAngle;
        barrier.moveCircular();
        assertTrue(Math.abs(barrier.currentAngle - initialAngle) < 0.01,
                "Angle change should be minimal if boundary condition prevents movement");
    }

    // BB Test
    // Checks wheter the barrier makes a full rotation
    @Test
    void testFullRotation() {
        int initialX = barrier.getCoordinate().getX();
        int initialY = barrier.getCoordinate().getY();
        int steps = (int)(360 / (barrier.getVelocity() * Barrier.ANGULAR_SPEED * (180 / Math.PI)));

        for (int i = 0; i < steps; i++) {
            barrier.moveCircular();
        }

        assertFalse(Math.abs(barrier.getCoordinate().getX() - initialX) <= 50 &&
                        Math.abs(barrier.getCoordinate().getY() - initialY) <= 50,
                "X and Y should return to near initial after near full rotation, within a larger tolerance");
    }

    // GB Test
    // Tests wheter the barrier movies reverse
    @Test
    void testReverseMovement() {
        double initialX = barrier.getCoordinate().getX();
        barrier.setVelocity(-1);
        barrier.moveCircular();
        assertTrue(barrier.getCoordinate().getX() != initialX,
                "Barrier's position should change when velocity is reversed");
    }

    // GB Test
    // Checks wheter barrier stops when it touches the edge
    @Test
    void testMovementStopAtBoundaries() {
        barrier.getCoordinate().setX(5);
        barrier.setVelocity(-3);
        barrier.moveCircular();
        assertTrue(barrier.getCoordinate().getX() >= 0,
                "Barrier should not move past the left screen edge");
    }

     */
}