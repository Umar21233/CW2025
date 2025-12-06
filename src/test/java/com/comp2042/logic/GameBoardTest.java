package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(10, 20);
    }

    @Test
    void constructor_initializesEmptyMatrixWithGivenWidthAndHeight() {
        assertEquals(10, gameBoard.getWidth());
        assertEquals(20, gameBoard.getHeight());

        int[][] matrix = gameBoard.getGameMatrix();
        assertEquals(20, matrix.length);
        assertEquals(10, matrix[0].length);

        for (int[] row : matrix) {
            for (int cell : row) {
                assertEquals(0, cell);
            }
        }
    }

    @Test
    void getGameMatrix_returnsDefensiveCopy() {
        int[][] matrix1 = gameBoard.getGameMatrix();
        matrix1[0][0] = 7;

        int[][] matrix2 = gameBoard.getGameMatrix();
        assertEquals(0, matrix2[0][0], "External modifications must not affect internal board state");
    }

    @Test
    void isCollision_detectsCollisionWithExistingBlocks() {
        int[][] shape = {
                {1, 1},
                {1, 1}
        };

        // First merge a brick into the board
        gameBoard.mergeBrick(new Point(0, 0), shape);

        // Now trying to place another brick overlapping the same area should collide
        boolean collision = gameBoard.isCollision(new Point(0, 0), shape);
        assertTrue(collision);
    }

    @Test
    void mergeBrick_addsShapeToBoard() {
        int[][] shape = {
                {1, 0},
                {0, 1}
        };

        gameBoard.mergeBrick(new Point(2, 3), shape);
        int[][] matrix = gameBoard.getGameMatrix();

        assertEquals(1, matrix[3][2]);
        assertEquals(1, matrix[4][3]);
    }

    @Test
    void clearRows_removesFullRowsAndReturnsClearRow() {
        int width = gameBoard.getWidth();
        int[] fullRow = new int[width];
        for (int i = 0; i < width; i++) fullRow[i] = 1;

        // Put a full row at the bottom of the board
        int[][] brick = new int[][] { fullRow };
        gameBoard.mergeBrick(new Point(0, gameBoard.getHeight() - 1), brick);

        ClearRow clearRow = gameBoard.clearRows();
        int[][] newMatrix = gameBoard.getGameMatrix();

        // Bottom row should now be empty
        for (int cell : newMatrix[newMatrix.length - 1]) {
            assertEquals(0, cell);
        }

        assertNotNull(clearRow, "ClearRow result should never be null");
        assertNotNull(clearRow.getNewMatrix(), "ClearRow must contain a new matrix");
    }

    @Test
    void reset_clearsAllCells() {
        int[][] shape = {
                {1, 1},
                {1, 1}
        };

        gameBoard.mergeBrick(new Point(0, 0), shape);
        gameBoard.reset();

        int[][] matrix = gameBoard.getGameMatrix();
        for (int[] row : matrix) {
            for (int cell : row) {
                assertEquals(0, cell);
            }
        }
    }
}
