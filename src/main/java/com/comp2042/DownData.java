package com.comp2042;

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
