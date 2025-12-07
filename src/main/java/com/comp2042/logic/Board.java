package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import com.comp2042.model.ViewData;

/**
 * Defines the contract for a game board in a Tetris-like game.
 * It specifies the operations related to brick movement, rotation,
 * board state, score management, and game flow.
 */
public interface Board {

    /**
     * Attempts to move the current brick down by one unit.
     *
     * @return True if the brick was successfully moved down, false otherwise (e.g., collision).
     */
    boolean moveBrickDown();

    /**
     * Attempts to move the current brick left by one unit.
     *
     * @return True if the brick was successfully moved left, false otherwise (e.g., collision).
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current brick right by one unit.
     *
     * @return True if the brick was successfully moved right, false otherwise (e.g., collision).
     */
    boolean moveBrickRight();

    /**
     * Attempts to rotate the current brick to the left.
     *
     * @return True if the brick was successfully rotated, false otherwise (e.g., collision).
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick and places it on the board.
     *
     * @return True if a new brick was successfully created, false if the game is over (no space for new brick).
     */
    boolean createNewBrick();

    /**
     * Returns the current state of the game board as a 2D integer array.
     * Each integer represents a block type or empty space.
     *
     * @return A 2D array representing the game board matrix.
     */
    int[][] getBoardMatrix();

    /**
     * Retrieves the current data needed for rendering the game view.
     * This includes the board matrix and the current brick's position and shape.
     *
     * @return A ViewData object containing all necessary information for display.
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the background of the game board.
     * This typically happens when a brick lands and can no longer move down.
     */
    void mergeBrickToBackground();

    /**
     * Checks for and clears any completed rows on the board.
     * Updates the score based on the number of rows cleared.
     *
     * @return A ClearRow object indicating the cleared rows and their count.
     */
    ClearRow clearRows();

    /**
     * Retrieves the current score object for the game.
     *
     * @return The Score object.
     */
    Score getScore();

    /**
     * Resets the game board and prepares for a new game.
     * This typically involves clearing the board, resetting the score, and generating a new starting brick.
     */
    void newGame();
}
