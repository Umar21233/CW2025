package com.comp2042.logic;

import com.comp2042.model.ClearRow;
import com.comp2042.model.DownData;
import com.comp2042.model.MoveEvent;
import com.comp2042.model.ViewData;
import com.comp2042.ui.GuiController;

/**
 - Acts as the controller for core gameplay actions.
 - Receives input events from the UI and delegates all game logic
  to the Board implementation. Ensures UI never directly modifies
  game state, maintaining clean separation of responsibilities.
 */


public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(10, 25);
    private final GuiController viewGuiController;
    private int previousLevel = 1;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());

        //Bind current score + high score to sidebar labels
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindHighScore(board.getScore().highScoreProperty());
        viewGuiController.bindLevel(board.getScore().levelProperty());

        //Set initial game speed
        viewGuiController.updateGameSpeed(Constants.LEVEL_SPEED[0]);
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            if (clearRow != null) {
                int lines = clearRow.getLinesRemoved();
                if (lines > 0) {
                    board.getScore().add(clearRow.getScoreBonus());
                    board.getScore().addLines(lines);

                    viewGuiController.recordCombo(lines);

                    checkLevelChange();
                }
            }

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }

        return new DownData(clearRow, board.getViewData(), !canMove);
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public DownData onHardDrop(MoveEvent event) {
        // Move down until blocked
        while (board.moveBrickDown()) {
            //just keep dropping
        }

        //Lock the piece
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            board.getScore().addLines(clearRow.getLinesRemoved());

            viewGuiController.recordCombo(clearRow.getLinesRemoved());

            checkLevelChange();
        }

        //Spawn new piece, check game over
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        //Update background for locked piece
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        //For hard drop we also pass the board matrix (if GUI ever wants it)
        return new DownData(clearRow, board.getViewData(), board.getBoardMatrix(), true);
    }

    @Override
    public void createNewGame() {
        board.newGame();
        this.previousLevel = 1;
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        viewGuiController.updateGameSpeed(Constants.LEVEL_SPEED[0]);
    }

    @Override
    public void onGameExit() {
        board.getScore().add(0);
    }

    private void checkLevelChange() {
        int currentLevel = board.getScore().getLevel();
        if (currentLevel != previousLevel) {
            previousLevel = currentLevel;
            //Update game speed based on new level (level 1 = index 0)
            int speedIndex = Math.min(currentLevel - 1, Constants.LEVEL_SPEED.length - 1);
            viewGuiController.updateGameSpeed(Constants.LEVEL_SPEED[speedIndex]);
        }
    }
}
