package org.Domain;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class initialPopulationTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = Game.createNewGame();
    }

    // BB tests
    @Test
    public void testInitialPopulationWithSufficientSpace() {
        assertTrue(game.initialPopulation(10, 10, 10, 10));
        assertEquals(10, game.getNumSimpleBarrier());
        assertEquals(10, game.getNumFirmBarrier());
        assertEquals(10, game.getNumExplosiveBarrier());
        assertEquals(10, game.getNumrewardingBarrier());
    }

    @Test
    public void testInitialPopulationWithInsufficientSpace() {
        game.initialPopulation(100, 100, 100, 100);
        assertFalse(game.initialPopulation(1, 1, 1, 1));
    }

    @Test
    public void testInitialPopulationWithSomeBarriersAlreadyPlaced() {
        game.initialPopulation(50, 50, 50, 50);
        assertTrue(game.initialPopulation(10, 10, 10, 10));
        assertEquals(60, game.getNumSimpleBarrier());
        assertEquals(60, game.getNumFirmBarrier());
        assertEquals(60, game.getNumExplosiveBarrier());
        assertEquals(60, game.getNumrewardingBarrier());
    }

    // GB tests
    @Test
    public void testInitialPopulationRandomPlacement() {
        assertTrue(game.initialPopulation(5, 5, 5, 5));
        int count = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (game.getBarrierBoard()[i][j] != null) {
                    count++;
                }
            }
        }
        assertEquals(20, count); // 5 + 5 + 5 + 5 = 20
    }

    @Test
    public void testInitialPopulationExactBarrierCounts() {
        assertTrue(game.initialPopulation(10, 10, 10, 10));
        assertEquals(10, game.getNumSimpleBarrier());
        assertEquals(10, game.getNumFirmBarrier());
        assertEquals(10, game.getNumExplosiveBarrier());
        assertEquals(10, game.getNumrewardingBarrier());

        int simpleCount = 0, firmCount = 0, explosiveCount = 0, rewardingCount = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ("s".equals(game.getBarrierBoard()[i][j])) simpleCount++;
                if ("f".equals(game.getBarrierBoard()[i][j])) firmCount++;
                if ("x".equals(game.getBarrierBoard()[i][j])) explosiveCount++;
                if ("r".equals(game.getBarrierBoard()[i][j])) rewardingCount++;
            }
        }
        assertEquals(10, simpleCount);
        assertEquals(10, firmCount);
        assertEquals(10, explosiveCount);
        assertEquals(10, rewardingCount);
    }

    @Test
    public void testInitialPopulationWithZeroBarriers() {
        assertTrue(game.initialPopulation(0, 0, 0, 0));
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                assertNull(game.getBarrierBoard()[i][j]);
            }
        }
    }
}
