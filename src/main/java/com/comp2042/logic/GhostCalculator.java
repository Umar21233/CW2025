package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;

/**
 * A utility class responsible for calculating the position of the "ghost" piece
 * (the projected landing spot of the current falling brick) on the game board.
 */
public final class GhostCalculator {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GhostCalculator() { }

    /**
     * Computes the final Y-coordinate where the current falling brick would land
     * if it were to drop instantly (the "ghost" piece position).
     *
     * @param boardMatrix The current state of the game board matrix.
     * @param brick The falling brick object.
     * @param brickX The current X-coordinate of the falling brick's top-left corner.
     * @param brickY The current Y-coordinate of the falling brick's top-left corner.
     * @param boardHeight The height of the game board.
     * @param currentShapeIndex The index of the current shape of the brick.
     * @return The Y-coordinate of the ghost piece.
     */
    public static int computeGhostY(
            int[][] boardMatrix,
            Brick brick,
            int brickX,
            int brickY,
            int boardHeight,
            int currentShapeIndex
    ) {

        int ghostY = brickY;
        int[][] shape = brick.getShapeMatrix().get(currentShapeIndex);

        while (ghostY + 1 < boardHeight &&
                !MatrixOperations.intersect(boardMatrix, shape, brickX, ghostY + 1)) {
            ghostY++;
        }

        return ghostY;
    }
}
