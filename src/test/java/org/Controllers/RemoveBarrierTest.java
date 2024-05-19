package org.Controllers;
import org.Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveBarrierTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = Game.createNewGame();
    }

    /*This test checks if a simple barrier can be removed from the game by first
    adding a simple barrier to the board and then removing it. This is a glass-box
    test because the test relies on the internal knowledge of how barriers are stored.*/
    @Test
    public void testRemoveSimpleBarrier() {
        Coordinate coord = new Coordinate(50, 20);
        game.addBarrier(coord, BarrierType.SIMPLE);
        assertEquals(1, game.getNumSimpleBarrier());

        game.removeBarrier(coord, BarrierType.SIMPLE);
        assertEquals(0, game.getNumSimpleBarrier());
        assertNull(game.getBarrierBoard()[1][1]);
    }

    /*The following test is for the game's response when attempting to remove a barrier
    from an initially empty position in the board by ensuring the barrier count does not
    change. Since it does not depend on any internal structure, this is a black-box test.*/
    @Test
    public void testRemoveBarrierFromEmptyPosition() {
        Coordinate coord = new Coordinate(100, 40);
        game.removeBarrier(coord, BarrierType.SIMPLE);
        assertEquals(0, game.getNumSimpleBarrier());
        assertEquals(0, game.getNumFirmBarrier());
        assertEquals(0, game.getNumExplosiveBarrier());
        assertEquals(0, game.getNumrewardingBarrier());
    }

    /*This test ensures that a moving barrier can be safely removed from the grid. Since it
    requires internal information such as the mobility of the barriers, this is a glass-box
    test.*/
    @Test
    public void testRemoveMovingBarrier() {
        Coordinate coord = new Coordinate(200, 80);
        game.addBarrier(coord, BarrierType.EXPLOSIVE);
        Barrier barrier = game.getBarriers().get(0);
        barrier.setMoving(true);
        assertEquals(1, game.getNumExplosiveBarrier());

        game.removeBarrier(coord, BarrierType.EXPLOSIVE);
        assertEquals(0, game.getNumExplosiveBarrier());
        assertNull(game.getBarrierBoard()[4][4]);
    }

    /*The following test assures that a single barrier can be added to and then removed from
    an initially empty game board. This test is considered as glass-box because it checks the
   state of the grid.*/
    @Test
    public void testRemoveOnlyBarrier() {
        Coordinate coord = new Coordinate(250, 100);
        game.addBarrier(coord, BarrierType.REWARDING);
        assertEquals(1, game.getNumrewardingBarrier());

        game.removeBarrier(coord, BarrierType.REWARDING);
        assertEquals(0, game.getNumrewardingBarrier());
        assertNull(game.getBarrierBoard()[5][5]);
    }

    /*This glass-box test checks if the game can accurately handle the case where it is desired to
    remove one specific barrier when multiple barriers are present. It makes sure that other barriers
    remain unaffected.*/
    @Test
    public void testRemoveOneBarrierAmongMultiple() {
        Coordinate coord1 = new Coordinate(50, 20);
        Coordinate coord2 = new Coordinate(100, 40);
        Coordinate coord3 = new Coordinate(150, 60);

        game.addBarrier(coord1, BarrierType.SIMPLE);
        game.addBarrier(coord2, BarrierType.REINFORCED);
        game.addBarrier(coord3, BarrierType.EXPLOSIVE);

        assertEquals(1, game.getNumSimpleBarrier());
        assertEquals(1, game.getNumFirmBarrier());
        assertEquals(1, game.getNumExplosiveBarrier());
        assertEquals(3, game.getNumTotal());

        game.removeBarrier(coord2, BarrierType.REINFORCED);
        assertEquals(1, game.getNumSimpleBarrier());
        assertEquals(0, game.getNumFirmBarrier());
        assertEquals(1, game.getNumExplosiveBarrier());
        assertEquals(2, game.getNumTotal());

        assertNull(game.getBarrierBoard()[2][2]);
        assertEquals("s", game.getBarrierBoard()[1][1]);
        assertEquals("x", game.getBarrierBoard()[3][3]);
    }

    /*The following test examines the game's behavior when we want to remove a barrier of a specific type,
     but the barrier at the desired location is of a different type than expected. It ensures the barrier
     at the location remains unaffected. This is a glass-box test as it depends on the internal structure of
     the code.*/
    @Test
    public void testRemoveNonExistentBarrierTypeAtPosition() {
        Coordinate coord = new Coordinate(50, 20);
        game.addBarrier(coord, BarrierType.SIMPLE);

        int initialSimpleCount = game.getNumSimpleBarrier();
        int initialExplosiveCount = game.getNumExplosiveBarrier();

        game.removeBarrier(coord, BarrierType.EXPLOSIVE);
        assertEquals(Integer.toString(initialSimpleCount), Integer.toString(game.getNumSimpleBarrier()));
        assertEquals(Integer.toString(initialExplosiveCount), Integer.toString(game.getNumExplosiveBarrier()));
    }


}

