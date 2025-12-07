package com.comp2042.logic.bricks;

import com.comp2042.logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the I-shaped brick (straight or "I-Tetromino") in the game.
 * It provides the different rotation states for this brick type.
 */
final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Initializes the I-shaped brick with its predefined rotation states.
     * The brick has two rotation states.
     */
    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    /**
     * Returns a deep copy of the list of 2D integer arrays representing
     * all possible rotation states of the I-brick.
     *
     * @return A list of integer matrices, each defining a shape of the I-brick.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}
