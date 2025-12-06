package com.comp2042.logic;

import com.comp2042.model.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style tests for PieceManager using the real GameBoard,
 * RandomBrickGenerator, BrickRotator, GhostCalculator and ViewData.
 */
class PieceManagerTest {

    private GameBoard gameBoard;
    private PieceManager pieceManager;

    @BeforeEach
    void setUp() {
        // Standard 10x20 Tetris board
        gameBoard = new GameBoard(10, 20);
        pieceManager = new PieceManager(gameBoard);
    }

    @Test
    void spawnNewBrick_onEmptyBoard_hasNoCollisionAndProvidesViewData() {
        boolean collided = pieceManager.spawnNewBrick();

        assertFalse(collided, "Spawning a brick on an empty board should not collide");

        ViewData viewData = pieceManager.getViewData();
        assertNotNull(viewData, "ViewData should not be null after spawning a brick");
        assertNotNull(viewData.getBrickData(), "Brick matrix should not be null");
    }

    @Test
    void moveDown_movesPieceUntilItReachesBottom() {
        pieceManager.spawnNewBrick();

        ViewData viewData = pieceManager.getViewData();
        int previousY = viewData.getyPosition();

        boolean movedAtLeastOnce = false;
        boolean eventuallyStopped = false;

        // Safety cap to avoid infinite loop if something goes very wrong
        int limit = gameBoard.getHeight() + 5;

        for (int i = 0; i < limit; i++) {
            boolean moved = pieceManager.moveDown();
            viewData = pieceManager.getViewData();

            if (moved) {
                assertTrue(
                        viewData.getyPosition() > previousY,
                        "Y position should increase when moving down"
                );
                movedAtLeastOnce = true;
                previousY = viewData.getyPosition();
            } else {
                eventuallyStopped = true;
                break;
            }
        }

        assertTrue(movedAtLeastOnce, "Piece should move down at least once");
        assertTrue(eventuallyStopped, "Piece should eventually stop when reaching bottom or another brick");
    }


    @Test
    void rotateLeft_succeedsWhenThereIsNoCollision() {
        pieceManager.spawnNewBrick();

        boolean rotated = pieceManager.rotateLeft();

        assertTrue(rotated, "Rotation should succeed when there is space");
        // We don’t assert the exact matrix (depends on random brick),
        // only that the call behaves as expected.
    }

    @Test
    void mergeBrickToBackground_writesCurrentPieceIntoGameBoard() {
        pieceManager.spawnNewBrick();

        // Drop the piece all the way down
        int safetyLimit = gameBoard.getHeight() + 5;
        for (int i = 0; i < safetyLimit && pieceManager.moveDown(); i++) {
            // keep moving down
        }

        // Merge the piece into the board
        pieceManager.mergeBrickToBackground();

        int[][] matrix = gameBoard.getGameMatrix();
        boolean hasNonZeroCell = false;

        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell != 0) {
                    hasNonZeroCell = true;
                    break;
                }
            }
            if (hasNonZeroCell) break;
        }

        assertTrue(hasNonZeroCell, "After merging, at least one cell on the board should be non-zero");
    }

    @Test
    void getViewData_containsValidGhostPosition() {
        pieceManager.spawnNewBrick();
        ViewData viewData = pieceManager.getViewData();

        int currentY = viewData.getyPosition();
        int ghostY = viewData.getGhostYPosition();

        assertTrue(
                ghostY >= currentY,
                "Ghost piece should never be above the current piece"
        );
        assertTrue(
                ghostY < gameBoard.getHeight(),
                "Ghost Y position must be within board height"
        );
    }
}
