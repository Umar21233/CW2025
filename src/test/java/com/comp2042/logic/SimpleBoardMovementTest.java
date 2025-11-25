package com.comp2042.logic;

import com.comp2042.model.ViewData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardMovementTest {

    @Test
    void testMoveLeftStopsAtBoundary() {
        SimpleBoard board = new SimpleBoard(10, 20);
        board.createNewBrick();
        ViewData dataBefore = board.getViewData();

        boolean movedLeft = true;
        int safetyCounter = 50;

        while (movedLeft && safetyCounter-- > 0) {
            movedLeft = board.moveBrickLeft();
        }

        ViewData dataAfter = board.getViewData();

        // After moving left until blocked, x must be 0
        assertEquals(0, dataAfter.getxPosition());
        assertNotEquals(dataBefore.getxPosition(), dataAfter.getxPosition());
    }
}

