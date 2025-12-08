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

    /**
     * Constructs a new GameController, initializing the game board and connecting it to the GUI.
     * Sets up event listeners, binds score and level properties, and sets the initial game speed.
     *
     * @param c The GuiController responsible for rendering the game view and handling UI interactions.
     */
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

    /**
     * Handles the event when the active brick attempts to move down.
     * Manages brick merging, row clearing, score updates, level changes,
     * and game over conditions.
     *
     * @param event The MoveEvent triggering the down action.
     * @return A {@code DownData} object containing information about cleared rows,
     *         current view data, and whether the brick was locked in place.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if(canMove && event.getEventSource() == com.comp2042.model.EventSource.USER) {
            board.getScore().add(10);
        }

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

    /**
     * Handles the event when the active brick attempts to move left.
     *
     * @param event The MoveEvent triggering the left action.
     * @return A {@code ViewData} object representing the updated game state for display.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handles the event when the active brick attempts to move right.
     *
     * @param event The MoveEvent triggering the right action.
     * @return A {@code ViewData} object representing the updated game state for display.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handles the event when the active brick attempts to rotate.
     *
     * @param event The MoveEvent triggering the rotate action.
     * @return A {@code ViewData} object representing the updated game state for display.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Handles the hard drop event, instantly moving the current brick down
     * until it collides, then locks it in place.
     * Manages row clearing, score updates, level changes, and game over conditions.
     *
     * @param event The MoveEvent triggering the hard drop action.
     * @return A {@code DownData} object containing information about cleared rows,
     *         current view data, the board matrix, and a flag indicating a hard drop.
     */
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

    /**
     * Resets the game state to begin a new game.
     * Initializes the board, resets the level, updates the GUI, and sets the initial game speed.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        this.previousLevel = 1;
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        viewGuiController.updateGameSpeed(Constants.LEVEL_SPEED[0]);
    }

    /**
     * Handles actions to be performed when the game exits.
     * Currently, adds a score of 0, which might be a placeholder or intended for saving state.
     */
    @Override
    public void onGameExit() {
        board.getScore().add(0);
    }

    /**
     * Checks if the game level has changed and, if so, updates the game speed
     * accordingly based on the new level.
     */
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
