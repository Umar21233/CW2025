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

    public NextShapeInfo getNextShape() {
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();

        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return MatrixOperations.copy(brick.getShapeMatrix().get(currentShape));
    }

    //this setter is package-private.
    void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        this.currentShape = 0;
    }

    public int getCurrentShapeIndex() {
        return currentShape;
    }


    //added so getbrick works in SimpleBoard
    public Brick getBrick() {
        return brick;
    }

}

