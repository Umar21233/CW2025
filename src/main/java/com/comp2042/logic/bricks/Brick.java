package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Defines the contract for a generic brick in the game.
 * It specifies the ability to retrieve the different shapes (rotation states)
 * of the brick.
 */
public interface Brick {

    /**
     * Returns a list of 2D integer arrays, where each array represents
     * a possible rotation state (shape) of the brick.
     *
     * @return A list of integer matrices, each defining a shape of the brick.
     */
    List<int[][]> getShapeMatrix();
}
