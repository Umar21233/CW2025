package com.comp2042.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DownDataTest {

    @Test
    void fullConstructorStoresAllFields() {
        ClearRow clearRow = new ClearRow(2, new int[][]{{1}}, 300);
        ViewData viewData = null; // we don't care about internals here
        int[][] board = {
                {1, 0},
                {0, 1}
        };

        DownData downData = new DownData(clearRow, viewData, board, true);

        assertSame(clearRow, downData.getClearRow());
        assertNull(downData.getViewData(), "ViewData should be whatever was passed in (null here)");
        assertSame(board, downData.getBoard());
        assertTrue(downData.isPieceLanded());
    }

    @Test
    void constructorWithoutBoardSetsBoardNullAndStoresPieceLanded() {
        ClearRow clearRow = new ClearRow(1, new int[][]{{0}}, 100);
        ViewData viewData = null;

        DownData downData = new DownData(clearRow, viewData, true);

        assertSame(clearRow, downData.getClearRow());
        assertNull(downData.getViewData());
        assertNull(downData.getBoard(), "Board should be null when not provided");
        assertTrue(downData.isPieceLanded());
    }

    @Test
    void convenienceConstructorWithClearRowAndViewDataDefaultsPieceLandedFalseAndBoardNull() {
        ClearRow clearRow = new ClearRow(0, new int[][]{{0}}, 0);
        ViewData viewData = null;

        DownData downData = new DownData(clearRow, viewData);

        assertSame(clearRow, downData.getClearRow());
        assertNull(downData.getViewData());
        assertNull(downData.getBoard());
        assertFalse(downData.isPieceLanded(), "Convenience constructor should default pieceLanded to false");
    }

    @Test
    void convenienceConstructorWithBoardDefaultsPieceLandedFalse() {
        ClearRow clearRow = new ClearRow(0, new int[][]{{0}}, 0);
        ViewData viewData = null;
        int[][] board = {
                {1, 2},
                {3, 4}
        };

        DownData downData = new DownData(clearRow, viewData, board);

        assertSame(clearRow, downData.getClearRow());
        assertNull(downData.getViewData());
        assertSame(board, downData.getBoard());
        assertFalse(downData.isPieceLanded(), "Convenience constructor with board should default pieceLanded to false");
    }
}
