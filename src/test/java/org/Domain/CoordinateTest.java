package org.Domain;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CoordinateTest {


    // Black-box test: Checking if getX() returns the correct x or y value
    @Test
    public void testGetCoordinate() {
        Coordinate c = new Coordinate(3, 4);
        assertEquals(3, c.getX());
        assertEquals(4, c.getY());
    }

    // Glass-box test: Checking if setX() sets the correct x or y value
    @Test
    public void testSetCoordinate() {
        Coordinate c = new Coordinate(3, 4);
        c.setX(10);
        c.setY(5);
        assertEquals(10, c.getX());
        assertEquals(5, c.getY());
    }

    // Glass-box test: Checking if repOk() returns true for valid coordinates
    @Test
    public void testRepOk() {
        Coordinate c = new Coordinate(3, 4);
        assertTrue(c.repOk());
    }

    // Black-box test: Checking if repOk() returns false for x or y out of bounds
    @Test
    public void testCoordinateOutOfBounds() {
        Coordinate c1 = new Coordinate(1001, 4);
        Coordinate c2 = new Coordinate(4, 501);
        assertFalse(c1.repOk());
        assertFalse(c2.repOk());
    }

    // Black-box test: Checking if repOk() returns false for negative x or y value
    @Test
    public void testNegativeCoordinate() {
        Coordinate c1 = new Coordinate(-1, 4);
        Coordinate c2 = new Coordinate(1, -4);
        assertFalse(c1.repOk());
        assertFalse(c2.repOk());
    }

    // Black-box test: Checking if repOk() returns true for edge case coordinates
    @Test
    public void testEdgeCases() {
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(1000, 500);
        assertTrue(c1.repOk());
        assertTrue(c2.repOk());
    }
}
