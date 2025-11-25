package com.comp2042.logic;

import com.comp2042.logic.MatrixOperations;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testCopyMatrix() {
        int[][] matrix = {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };

        int[][] copiedMatrix = MatrixOperations.copy(matrix);

        assertEquals(matrix[0][0], copiedMatrix[0][0]);
        assertEquals(matrix[1][1], copiedMatrix[1][1]);
        assertEquals(matrix[2][2], copiedMatrix[2][2]);
    }

    @Test
    void testMatrixMerge() {
        int[][] board = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        int[][] shape = {
                {1, 1},
                {1, 1}
        };

        // Merge shape into board at position (1, 1)
        board = MatrixOperations.merge(board, shape, 1, 1);

        // Test if the matrix updated correctly after the merge
        assertEquals(1, board[1][1]);
        assertEquals(1, board[1][2]);
        assertEquals(1, board[2][1]);
        assertEquals(1, board[2][2]);
    }

    @Test
    void testMatrixIntersection() {
        int[][] board = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        int[][] shape = {
                {1, 1},
                {1, 1}
        };

        boolean intersect = MatrixOperations.intersect(board, shape, 0, 0);
        assertFalse(intersect);

        // Add a piece to the board
        board[0][0] = 1;
        intersect = MatrixOperations.intersect(board, shape, 0, 0);
        assertTrue(intersect);
    }
}



