package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.model.NextShapeInfo;

/**
 - Handles rotation state of the current falling brick.
 - Provides controlled access to rotation transitions while
 keeping the internal rotation index encapsulated.
 */

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Calculates and returns information about the next rotation state of the current brick.
     * This method does not change the current state of the brick.
     *
     * @return A {@code NextShapeInfo} object containing the matrix and index of the next shape.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();

        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Returns a copy of the matrix representing the current shape of the brick.
     *
     * @return A 2D integer array representing the current brick shape.
     */
    public int[][] getCurrentShape() {
        return MatrixOperations.copy(brick.getShapeMatrix().get(currentShape));
    }

    /**
     * Sets the current rotation index of the brick.
     * This method is package-private.
     *
     * @param currentShape The new rotation index to set.
     */
    void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick to be managed by this rotator and resets its rotation index to 0.
     *
     * @param brick The brick to set.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        this.currentShape = 0;
    }

    /**
     * Returns the current rotation index of the brick.
     *
     * @return The current rotation index.
     */
    public int getCurrentShapeIndex() {
        return currentShape;
    }


    /**
     * Returns the brick currently being managed by this rotator.
     *
     * @return The current Brick object.
     */
    public Brick getBrick() {
        return brick;
    }

}

