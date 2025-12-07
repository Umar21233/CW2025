package com.comp2042.model;

/**
 - Immutable value object returned after a downward movement
   (soft drop or hard drop). Contains:
   - the result of any line clearing,
   - the updated ViewData for rendering,
   - and an optional copy of the board (used for hard drop only).
 - This class does not expose any mutable state and acts purely
  as a data transfer object (DTO) between the logic and UI layers.
 */


public final class DownData {

    /** Information about any rows that were cleared as a result of the downward movement. Can be null. */
    private final ClearRow clearRow;
    /** The updated view data required for rendering the game state after the movement. */
    private final ViewData viewData;
    /** An optional copy of the game board matrix, typically provided after a hard drop. Can be null. */
    private final int[][] board;   //may be null for normal soft drops
    /** Indicates whether the falling piece landed and was locked in place during this downward movement. */
    private final boolean pieceLanded;

    /**
     * Constructs a DownData object with information about row clearing, view updates, and piece landing status.
     * The board matrix is implicitly null for this constructor.
     *
     * @param clearRow Information about cleared rows, or null if none were cleared.
     * @param viewData The updated view data.
     * @param pieceLanded True if the piece landed and was locked, false otherwise.
     */
    public DownData(ClearRow clearRow, ViewData viewData, boolean pieceLanded) {
        this(clearRow, viewData, null, pieceLanded);
    }

    /**
     * Constructs a DownData object with comprehensive information including cleared rows,
     * view updates, an optional board matrix, and piece landing status.
     *
     * @param clearRow Information about cleared rows, or null if none were cleared.
     * @param viewData The updated view data.
     * @param board An optional copy of the game board matrix.
     * @param pieceLanded True if the piece landed and was locked, false otherwise.
     */
    public DownData(ClearRow clearRow, ViewData viewData, int[][] board, boolean pieceLanded) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.board = board;
        this.pieceLanded = pieceLanded;
    }

    //The following constructors are for convenience and will be removed in the future
    /**
     * Constructs a DownData object (for convenience, to be removed) with basic information.
     * Assumes no piece landed and no board matrix is provided.
     *
     * @param clearRow Information about cleared rows, or null.
     * @param viewData The updated view data.
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this(clearRow, viewData, null, false);
    }

    /**
     * Constructs a DownData object (for convenience, to be removed) with cleared rows,
     * view data, and board matrix, assuming no piece landed.
     *
     * @param clearRow Information about cleared rows, or null.
     * @param viewData The updated view data.
     * @param board An optional copy of the game board matrix.
     */
    public DownData(ClearRow clearRow, ViewData viewData, int[][] board) {
        this(clearRow, viewData, board, false);
    }

    /**
     * Returns the ClearRow object containing details about any cleared lines.
     *
     * @return The {@code ClearRow} object, or null if no lines were cleared.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Returns the ViewData object containing the current state of the game for rendering.
     *
     * @return The {@code ViewData} object.
     */
    public ViewData getViewData() {
        return viewData;
    }

    /**
     * Returns an optional copy of the game board matrix.
     * This is primarily used after a hard drop to update the background.
     *
     * @return A 2D integer array representing the game board, or null if not provided.
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Indicates whether the piece landed and was locked into the board during the downward movement.
     *
     * @return True if the piece landed, false otherwise.
     */
    public boolean isPieceLanded() {
        return pieceLanded;
    }
}
