package com.comp2042.logic.bricks;

import com.comp2042.logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the T-shaped brick ("T-Tetromino") in the game.
 * It provides the different rotation states for this brick type.
 */
public final class TBrick implements Brick { //made it public so can be tested

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Initializes the T-shaped brick with its predefined rotation states.
     * The brick has four rotation states.
     */
    public TBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {6, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {0, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 6, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 0, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Returns a deep copy of the list of 2D integer arrays representing
     * all possible rotation states of the T-brick.
     *
     * @return A list of integer matrices, each defining a shape of the T-brick.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
