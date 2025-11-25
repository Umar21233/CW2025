package com.comp2042.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// this test verifies that merging a shape onto filled positions behaves correctly

class MatrixOperationsMergeCollisionTest {

    @Test
    void testMergeDoesNotOverwriteFilledCells() {
        int[][] board = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        int[][] shape = {
                {1}
        };

        int[][] result = MatrixOperations.merge(board, shape, 1, 1);

        // Ensure original board cell stays filled
        assertEquals(1, result[1][1]);
    }
}

