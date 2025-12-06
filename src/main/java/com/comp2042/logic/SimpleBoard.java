package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import com.comp2042.model.ViewData;

public class SimpleBoard implements Board {

    private final GameBoard gameBoard;
    private final PieceManager pieceManager;
    private final Score score;

    public SimpleBoard(int width, int height) {
        this.gameBoard = new GameBoard(width, height);
        this.pieceManager = new PieceManager(gameBoard);
        this.score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        return pieceManager.moveDown();
    }

    @Override
    public boolean moveBrickLeft() {
        return pieceManager.moveLeft();
    }

    @Override
    public boolean moveBrickRight() {
        return pieceManager.moveRight();
    }

    @Override
    public boolean rotateLeftBrick() {
        return pieceManager.rotateLeft();
    }

    @Override
    public boolean createNewBrick() {
        return pieceManager.spawnNewBrick();
    }

    @Override
    public int[][] getBoardMatrix() {
        return gameBoard.getGameMatrix();
    }

    @Override
    public ViewData getViewData() {
        return pieceManager.getViewData();
    }

    @Override
    public void mergeBrickToBackground() {
        pieceManager.mergeBrickToBackground();
    }

    @Override
    public ClearRow clearRows() {
        return gameBoard.clearRows();
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        gameBoard.reset();
        score.reset();
        createNewBrick();
    }
}
