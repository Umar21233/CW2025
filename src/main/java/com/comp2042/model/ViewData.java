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

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    //ghost Y position (same X as normal piece)
    private final int ghostYPosition;

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

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    //where the ghost should sit vertically (row index)
    public int getGhostYPosition() {
        return ghostYPosition;
    }
}
