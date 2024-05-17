package org.Controllers;
import org.Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveBarrierTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = Game.createNewGame();
    }

    @Test
    public void testRemoveSimpleBarrier() {
        Coordinate coord = new Coordinate(50, 20);
        game.addBarrier(coord, BarrierType.SIMPLE);
        assertEquals(1, game.getNumSimpleBarrier());

        game.removeBarrier(coord, BarrierType.SIMPLE);
        assertEquals(0, game.getNumSimpleBarrier());
        assertNull(game.getBarrierBoard()[1][1]);
    }

    @Test
    public void testRemoveBarrierFromEmptyPosition() {
        Coordinate coord = new Coordinate(100, 40);
        game.removeBarrier(coord, BarrierType.SIMPLE);
        assertEquals(0, game.getNumSimpleBarrier());
        assertEquals(0, game.getNumFirmBarrier());
        assertEquals(0, game.getNumExplosiveBarrier());
        assertEquals(0, game.getNumrewardingBarrier());
    }

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

    @Test
    public void testRemoveOnlyBarrier() {
        Coordinate coord = new Coordinate(250, 100);
        game.addBarrier(coord, BarrierType.REWARDING);
        assertEquals(1, game.getNumrewardingBarrier());

        game.removeBarrier(coord, BarrierType.REWARDING);
        assertEquals(0, game.getNumrewardingBarrier());
        assertNull(game.getBarrierBoard()[5][5]);
    }
}

