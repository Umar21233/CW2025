package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import java.awt.Point;

public class GameBoard {

    private final int width;
    private final int height;
    private int[][] gameMatrix;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.gameMatrix = new int[height][width];
    }

    public int[][] getGameMatrix() {
        return MatrixOperations.copy(gameMatrix);
    }

    public boolean isCollision(Point offset, int[][] shape) {
        return MatrixOperations.intersect(gameMatrix, shape, (int) offset.getX(), (int) offset.getY());
    }

    public void mergeBrick(Point offset, int[][] shape) {
        gameMatrix = MatrixOperations.merge(gameMatrix, shape, (int) offset.getX(), (int) offset.getY());
    }

    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(gameMatrix);
        gameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    public void reset() {
        this.gameMatrix = new int[height][width];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
