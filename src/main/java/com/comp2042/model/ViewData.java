package com.comp2042.model;

import com.comp2042.logic.MatrixOperations;

/**
 - Immutable data snapshot passed from the game logic layer to the UI.
 - Contains the active brick matrix, its position, the next brick preview,
   and the ghost piece vertical position.
 - This class performs defensive copies to prevent accidental mutation
   of internal game state by the UI.
 */


public final class ViewData {

    /** The 2D integer array representing the active falling brick's shape. */
    private final int[][] brickData;
    /** The X-coordinate (column) of the active falling brick's top-left corner on the board. */
    private final int xPosition;
    /** The Y-coordinate (row) of the active falling brick's top-left corner on the board. */
    private final int yPosition;
    /** The 2D integer array representing the shape of the next brick to fall. */
    private final int[][] nextBrickData;

    /** The Y-coordinate (row) of the ghost piece, indicating where the active brick would land. */
    private final int ghostYPosition;

    /**
     * Constructs a new ViewData object, creating defensive copies of array data.
     *
     * @param brickData The 2D integer array representing the active falling brick.
     * @param xPosition The X-coordinate of the active brick.
     * @param yPosition The Y-coordinate of the active brick.
     * @param nextBrickData The 2D integer array representing the next brick.
     * @param ghostYPosition The Y-coordinate of the ghost piece.
     */
    public ViewData(int[][] brickData,
                    int xPosition,
                    int yPosition,
                    int[][] nextBrickData,
                    int ghostYPosition) {

        //make defensive copies so UI can't mutate board state by accident
        this.brickData = MatrixOperations.copy(brickData);
        this.nextBrickData = MatrixOperations.copy(nextBrickData);

        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ghostYPosition = ghostYPosition;
    }

    /**
     * Returns a deep copy of the 2D integer array representing the active falling brick's shape.
     *
     * @return A deep copy of the active brick's shape matrix.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns the X-coordinate (column) of the active falling brick's top-left corner.
     *
     * @return The X-position of the active brick.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Returns the Y-coordinate (row) of the active falling brick's top-left corner.
     *
     * @return The Y-position of the active brick.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Returns a deep copy of the 2D integer array representing the shape of the next brick to fall.
     *
     * @return A deep copy of the next brick's shape matrix.
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    //where the ghost should sit vertically (row index)
    /**
     * Returns the Y-coordinate (row) of the ghost piece, indicating where the active brick would land.
     *
     * @return The Y-position of the ghost piece.
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }
}
