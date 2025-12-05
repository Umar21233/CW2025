package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GhostCalculatorTest {

    private static class MockBrick implements Brick {
        private final int[][] shape = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        @Override
        public List<int[][]> getShapeMatrix() {
            return Collections.singletonList(shape);
        }
    }

    private static class MatrixOperations {
        public static boolean intersect(int[][] board, int[][] shape, int x, int y) {
            for (int r = 0; r < shape.length; r++) {
                for (int c = 0; c < shape[0].length; c++) {
                    if (shape[r][c] != 0) {
                        int boardR = y + r;
                        int boardC = x + c;

                        if (boardR >= board.length || boardC < 0 || boardC >= board[0].length) {
                            return true;
                        }
                        if (boardR >= 0 && board[boardR][boardC] != 0) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    @Test
    void class_cannot_be_instantiated() throws NoSuchMethodException {
        Constructor<GhostCalculator> constructor = GhostCalculator.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        assertDoesNotThrow(() -> constructor.newInstance((Object[]) null));
    }

    @Test
    void computeGhostY_drops_to_bottom_of_empty_board() {
        int boardWidth = 10;
        int boardHeight = 25;
        int[][] emptyBoard = new int[boardHeight][boardWidth];
        Brick mockBrick = new MockBrick();

        int expectedY = 23;

        int ghostY = GhostCalculator.computeGhostY(
                emptyBoard,
                mockBrick,
                3,
                0,
                boardHeight,
                0
        );

        assertEquals(expectedY, ghostY);
    }

    @Test
    void computeGhostY_stops_above_filled_line() {
        int boardWidth = 10;
        int boardHeight = 25;
        int[][] board = new int[boardHeight][boardWidth];
        Brick mockBrick = new MockBrick();

        for (int c = 0; c < boardWidth; c++) {
            board[23][c] = 9;
            board[24][c] = 9;
        }

        int expectedY = 21;

        int ghostY = GhostCalculator.computeGhostY(
                board,
                mockBrick,
                3,
                0,
                boardHeight,
                0
        );

        assertEquals(expectedY, ghostY);
    }

    @Test
    void computeGhostY_stops_above_another_brick() {
        int boardWidth = 10;
        int boardHeight = 25;
        int[][] board = new int[boardHeight][boardWidth];
        Brick mockBrick = new MockBrick();

        board[18][5] = 9;

        int expectedY = 16;

        int ghostY = GhostCalculator.computeGhostY(
                board,
                mockBrick,
                5,
                0,
                boardHeight,
                0
        );

        assertEquals(expectedY, ghostY);
    }
}