package com.comp2042.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//this test ensures that placing a brick outside boundaries returns true

class MatrixOperationsOutOfBoundsTest {

    @Test
    void testIntersectionOutOfBounds() {
        int[][] board = new int[4][4];

        int[][] shape = {
                {1, 1},
                {1, 1}
        };

        // Position where shape exceeds bottom-right boundary
        boolean intersects = MatrixOperations.intersect(board, shape, 3, 3);

        assertTrue(intersects);
    }
}

