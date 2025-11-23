package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;

public final class GhostCalculator {

    private GhostCalculator() { }

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
