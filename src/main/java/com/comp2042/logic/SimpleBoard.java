package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import com.comp2042.model.ViewData;

/**
 * An implementation of the {@code Board} interface, providing the core
 * game logic for a Tetris-like game. It orchestrates interactions between
 * the {@code GameBoard}, {@code PieceManager}, and {@code Score} components.
 */
public class SimpleBoard implements Board {

    /** The underlying game board grid. */
    private final GameBoard gameBoard;
    /** Manages the active falling brick and its operations. */
    private final PieceManager pieceManager;
    /** Tracks and manages the player's score, lines, and level. */
    private final Score score;

    /**
     * Constructs a new SimpleBoard with a specified width and height.
     * Initializes the game board, piece manager, and score components.
     *
     * @param width The width of the game board.
     * @param height The height of the game board.
     */
    public SimpleBoard(int width, int height) {
        this.gameBoard = new GameBoard(width, height);
        this.pieceManager = new PieceManager(gameBoard);
        this.score = new Score();
    }

    /**
     * Attempts to move the active brick down by one unit.
     *
     * @return True if the brick was successfully moved down, false otherwise.
     */
    @Override
    public boolean moveBrickDown() {
        return pieceManager.moveDown();
    }

    /**
     * Attempts to move the active brick left by one unit.
     *
     * @return True if the brick was successfully moved left, false otherwise.
     */
    @Override
    public boolean moveBrickLeft() {
        return pieceManager.moveLeft();
    }

    /**
     * Attempts to move the active brick right by one unit.
     *
     * @return True if the brick was successfully moved right, false otherwise.
     */
    @Override
    public boolean moveBrickRight() {
        return pieceManager.moveRight();
    }

    /**
     * Attempts to rotate the active brick to the left.
     *
     * @return True if the brick was successfully rotated, false otherwise.
     */
    @Override
    public boolean rotateLeftBrick() {
        return pieceManager.rotateLeft();
    }

    /**
     * Creates a new brick and places it on the board.
     *
     * @return True if a new brick was successfully created, false if the game is over.
     */
    @Override
    public boolean createNewBrick() {
        return pieceManager.spawnNewBrick();
    }

    /**
     * Returns the current state of the game board as a 2D integer array.
     *
     * @return A 2D array representing the game board matrix.
     */
    @Override
    public int[][] getBoardMatrix() {
        return gameBoard.getGameMatrix();
    }

    /**
     * Retrieves the current data needed for rendering the game view.
     *
     * @return A {@code ViewData} object containing all necessary information for display.
     */
    @Override
    public ViewData getViewData() {
        return pieceManager.getViewData();
    }

    /**
     * Merges the current active brick into the background of the game board.
     */
    @Override
    public void mergeBrickToBackground() {
        pieceManager.mergeBrickToBackground();
    }

    /**
     * Checks for and clears any completed rows on the board.
     *
     * @return A {@code ClearRow} object indicating the cleared rows and their count.
     */
    @Override
    public ClearRow clearRows() {
        return gameBoard.clearRows();
    }

    /**
     * Retrieves the current score object for the game.
     *
     * @return The {@code Score} object.
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Resets the game board and prepares for a new game.
     * Clears the board, resets the score, and spawns a new brick.
     */
    @Override
    public void newGame() {
        gameBoard.reset();
        score.reset();
        createNewBrick();
    }
}
