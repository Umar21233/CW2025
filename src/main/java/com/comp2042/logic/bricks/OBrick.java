package com.comp2042.logic.bricks;

import com.comp2042.logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the O-shaped brick (square or "O-Tetromino") in the game.
 * This brick does not rotate, having only one shape state.
 */
final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Initializes the O-shaped brick with its single predefined shape.
     * The O-brick has only one rotation state.
     */
    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Returns a deep copy of the list of 2D integer arrays representing
     * the single shape of the O-brick.
     *
     * @return A list of integer matrices, defining the shape of the O-brick.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}
