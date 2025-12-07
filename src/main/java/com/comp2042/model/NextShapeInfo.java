package com.comp2042.model;

import com.comp2042.logic.MatrixOperations;

/**
 - Represents the next rotation state of a brick.
 - Contains the rotated 2D shape matrix and the rotation index.
 - Immutable and safely copied to prevent external mutation.
 */


public final class NextShapeInfo {

    /** The 2D integer matrix representing the shape of the next rotation state. */
    private final int[][] shape;
    /** The index of the next rotation state. */
    private final int position;

    /**
     * Constructs a new NextShapeInfo object.
     *
     * @param shape A 2D integer array representing the brick's shape for the next rotation.
     * @param position The index corresponding to this next rotation state.
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Returns a deep copy of the 2D integer array representing the shape of the next rotation state.
     *
     * @return A deep copy of the brick's shape matrix for the next rotation.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Returns the index of the next rotation state.
     *
     * @return The integer index of the next shape.
     */
    public int getPosition() {
        return position;
    }
}
