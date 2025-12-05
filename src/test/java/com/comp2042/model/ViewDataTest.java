package com.comp2042.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    @Test
    void constructorStoresPositionsAndCopiesArrays() {
        int[][] brick = {
                {1, 0},
                {0, 1}
        };
        int[][] next = {
                {2, 2},
                {0, 0}
        };

        ViewData viewData = new ViewData(brick, 3, 5, next, 10);

        assertEquals(3, viewData.getxPosition());
        assertEquals(5, viewData.getyPosition());
        assertEquals(10, viewData.getGhostYPosition());

        int[][] brickCopy = viewData.getBrickData();
        int[][] nextCopy = viewData.getNextBrickData();

        assertArrayEquals(brick[0], brickCopy[0]);
        assertArrayEquals(brick[1], brickCopy[1]);

        assertArrayEquals(next[0], nextCopy[0]);
        assertArrayEquals(next[1], nextCopy[1]);

        brick[0][0] = 9;
        next[0][0] = 8;

        int[][] brickCopyAfterMutation = viewData.getBrickData();
        int[][] nextCopyAfterMutation = viewData.getNextBrickData();

        assertEquals(1, brickCopyAfterMutation[0][0],
                "Mutating original brick array must not affect ViewData");
        assertEquals(2, nextCopyAfterMutation[0][0],
                "Mutating original next array must not affect ViewData");
    }

    @Test
    void gettersReturnDefensiveCopiesEachTime() {
        int[][] brick = {
                {1, 1},
                {1, 1}
        };
        int[][] next = {
                {2, 2},
                {2, 2}
        };

        ViewData viewData = new ViewData(brick, 0, 0, next, 0);

        int[][] firstBrick = viewData.getBrickData();
        int[][] secondBrick = viewData.getBrickData();

        int[][] firstNext = viewData.getNextBrickData();
        int[][] secondNext = viewData.getNextBrickData();

        assertNotSame(firstBrick, secondBrick, "Each call to getBrickData should return a new array");
        assertNotSame(firstNext, secondNext, "Each call to getNextBrickData should return a new array");

        firstBrick[0][0] = 9;
        firstNext[0][0] = 7;

        assertEquals(1, secondBrick[0][0],
                "Modifying one brick data copy must not affect subsequent copies");
        assertEquals(2, secondNext[0][0],
                "Modifying one next data copy must not affect subsequent copies");
    }
}
