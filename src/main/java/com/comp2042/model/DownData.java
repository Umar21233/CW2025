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

    private final ClearRow clearRow;
    private final ViewData viewData;
    private final int[][] board;   //may be null for normal soft drops
    private final boolean pieceLanded;

    public DownData(ClearRow clearRow, ViewData viewData, boolean pieceLanded) {
        this(clearRow, viewData, null, pieceLanded);
    }

    public DownData(ClearRow clearRow, ViewData viewData, int[][] board, boolean pieceLanded) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.board = board;
        this.pieceLanded = pieceLanded;
    }

    //The following constructors are for convenience and will be removed in the future
    public DownData(ClearRow clearRow, ViewData viewData) {
        this(clearRow, viewData, null, false);
    }

    public DownData(ClearRow clearRow, ViewData viewData, int[][] board) {
        this(clearRow, viewData, board, false);
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isPieceLanded() {
        return pieceLanded;
    }
}
