package com.comp2042;

import static com.comp2042.Constants.*;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

        //bind high-score label
        viewGuiController.bindHighScore(board.getScore().highScoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            //piece has landed
            board.mergeBrickToBackground();
            clearRow = board.clearRows();

            int lines = clearRow.getLinesRemoved();
            if (lines > 0) {
                board.getScore().addLineClear(lines);
                board.getScore().addLines(lines); //track for levels
                updateLevelIfNeeded();
            }

            //checks for game over
            if (board.createNewBrick()) {
                //new: update high score before we ever reset the score
                board.getScore().updateHighScore();
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        }
        return new DownData(clearRow, board.getViewData());
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
    public void createNewGame() {
        board.newGame(); //simpleBoard.newGame() also creates the first brick + resets score
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        //reset level + speed to initial
        viewGuiController.updateLevelLabel(1);
        viewGuiController.updateSpeed(INITIAL_TICK_MILLIS);
    }

    private void updateLevelIfNeeded() {
        int total = board.getScore().getTotalLines();
        int level = Math.min(MAX_LEVEL, 1 + total / LINES_PER_LEVEL);
        viewGuiController.updateLevelLabel(level);
        viewGuiController.updateSpeed(LEVEL_SPEED[level - 1]);
    }
}
