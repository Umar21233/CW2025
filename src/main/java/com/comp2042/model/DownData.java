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

    public DownData(ClearRow clearRow, ViewData viewData) {
        this(clearRow, viewData, null);
    }

    public DownData(ClearRow clearRow, ViewData viewData, int[][] board) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.board = board;
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
}
