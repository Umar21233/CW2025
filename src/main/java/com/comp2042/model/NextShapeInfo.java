package com.comp2042.model;

import com.comp2042.logic.MatrixOperations;

/**
 - Represents the next rotation state of a brick.
 - Contains the rotated 2D shape matrix and the rotation index.
 - Immutable and safely copied to prevent external mutation.
 */


public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    public int getPosition() {
        return position;
    }
}
