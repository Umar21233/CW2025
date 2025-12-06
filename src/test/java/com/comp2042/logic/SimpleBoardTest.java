package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import com.comp2042.model.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for SimpleBoard tying together:
 * GameBoard + PieceManager + Score.
 */
class SimpleBoardTest {

    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(10, 20);
    }

    @Test
    void newGame_initializesBoardAndSpawnsFirstBrick() {
        board.newGame();

        int[][] matrix = board.getBoardMatrix();
        assertNotNull(matrix, "Board matrix should not be null after newGame");
        assertEquals(20, matrix.length);
        assertEquals(10, matrix[0].length);

        ViewData viewData = board.getViewData();
        assertNotNull(viewData, "ViewData should be available after newGame");
        assertNotNull(viewData.getBrickData(), "Active brick data should not be null");
    }

    @Test
    void moveBrickDown_movesPieceUntilStopped() {
        board.newGame();

        ViewData viewData = board.getViewData();
        int prevY = viewData.getyPosition();

        boolean movedAtLeastOnce = false;
        boolean eventuallyStopped = false;

        int limit = board.getBoardMatrix().length + 5;

        for (int i = 0; i < limit; i++) {
            boolean moved = board.moveBrickDown();
            viewData = board.getViewData();

            if (moved) {
                assertTrue(
                        viewData.getyPosition() > prevY,
                        "Y position should increase after moveBrickDown() returns true"
                );
                movedAtLeastOnce = true;
                prevY = viewData.getyPosition();
            } else {
                eventuallyStopped = true;
                break;
            }
        }

        assertTrue(movedAtLeastOnce, "Brick should move down at least once");
        assertTrue(eventuallyStopped, "Brick should eventually stop when reaching bottom or stacked bricks");
    }

    @Test
    void moveBrickLeftAndRight_doNotThrowAndRespectBoardWidth() {
        board.newGame();

        int width = board.getBoardMatrix()[0].length;

        // Move left several times
        for (int i = 0; i < width * 2; i++) {
            board.moveBrickLeft();
        }
        ViewData viewData = board.getViewData();
        assertTrue(
                viewData.getxPosition() >= 0,
                "After repeated left moves, X should not be negative"
        );

        // Move right several times
        for (int i = 0; i < width * 2; i++) {
            board.moveBrickRight();
        }
        viewData = board.getViewData();
        assertTrue(
                viewData.getxPosition() < width,
                "After repeated right moves, X should remain within board width"
        );
    }

    @Test
    void clearRows_onEmptyBoard_returnsZeroLinesRemoved() {
        board.newGame();

        ClearRow clearRow = board.clearRows();
        assertNotNull(clearRow);
        assertEquals(0, clearRow.getLinesRemoved(), "No rows should be cleared on an empty board");
    }

    @Test
    void newGame_resetsScoreState() {
        board.newGame();

        Score score = board.getScore();
        score.add(100);
        score.addLines(5);
        assertTrue(score.getScore() > 0);
        assertTrue(score.getTotalLines() > 0);

        board.newGame(); // should reset score and board

        assertEquals(0, score.getScore(), "Score should reset to 0 on newGame");
        assertEquals(0, score.getTotalLines(), "Total lines should reset to 0 on newGame");
        assertEquals(1, score.getLevel(), "Level should reset to 1 on newGame");
    }
}
