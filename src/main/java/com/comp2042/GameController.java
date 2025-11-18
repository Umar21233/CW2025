package com.comp2042;

public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(10, 25);
    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());

        //Bind current score + high score to sidebar labels
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindHighScore(board.getScore().highScoreProperty());
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
                }
            }

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }

        //normal soft drop doesn’t need the board in DownData
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
        }

        //Spawn new piece, check game over
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        //Update background for locked piece
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        //For hard drop we also pass the board matrix (if GUI ever wants it)
        return new DownData(clearRow, board.getViewData(), board.getBoardMatrix());
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}
