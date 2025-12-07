package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import java.awt.Point;

/**
 * Represents the game board logic for a Tetris-like game.
 * Manages the grid, detects collisions, merges bricks, and clears completed rows.
 */
public class GameBoard {

    private final int width;
    private final int height;
    private int[][] gameMatrix;

    /**
     * Constructs a new GameBoard with the specified width and height.
     * Initializes an empty game matrix.
     *
     * @param width The width of the game board.
     * @param height The height of the game board.
     */
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.gameMatrix = new int[height][width];
    }

    /**
     * Returns a copy of the current 2D integer array representing the game board's state.
     * Each integer in the matrix represents a block type or an empty space.
     *
     * @return A deep copy of the game matrix.
     */
    public int[][] getGameMatrix() {
        return MatrixOperations.copy(gameMatrix);
    }

    /**
     * Checks if a given brick shape, placed at a specific offset, would collide with
     * existing blocks on the game board or go out of bounds.
     *
     * @param offset The (x, y) coordinates for the top-left corner of the brick.
     * @param shape The 2D integer array representing the brick's shape.
     * @return True if a collision is detected, false otherwise.
     */
    public boolean isCollision(Point offset, int[][] shape) {
        return MatrixOperations.intersect(gameMatrix, shape, (int) offset.getX(), (int) offset.getY());
    }

    /**
     * Merges a given brick shape into the game board's matrix at the specified offset.
     * This operation modifies the game board permanently.
     *
     * @param offset The (x, y) coordinates for the top-left corner where the brick will be merged.
     * @param shape The 2D integer array representing the brick's shape to merge.
     */
    public void mergeBrick(Point offset, int[][] shape) {
        gameMatrix = MatrixOperations.merge(gameMatrix, shape, (int) offset.getX(), (int) offset.getY());
    }

    /**
     * Checks the game board for any completed rows, removes them, and shifts
     * the remaining blocks down.
     *
     * @return A {@code ClearRow} object containing information about the cleared rows
     *         and the new state of the game board.
     */
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(gameMatrix);
        gameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    /**
     * Resets the game board to an empty state.
     * All cells in the game matrix will be reinitialized to zero.
     */
    public void reset() {
        this.gameMatrix = new int[height][width];
    }

    /**
     * Returns the height of the game board.
     *
     * @return The height of the game board in number of rows.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of the game board.
     *
     * @return The width of the game board in number of columns.
     */
    public int getWidth() {
        return width;
    }
}
