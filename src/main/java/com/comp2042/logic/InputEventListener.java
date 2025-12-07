package com.comp2042.logic;

import com.comp2042.model.DownData;
import com.comp2042.model.MoveEvent;
import com.comp2042.model.ViewData;

/**
 * Defines the contract for an input event listener in the game.
 * Implementations of this interface will handle various user input actions
 * related to game control, such as moving, rotating, or dropping bricks.
 */
public interface InputEventListener {

    /**
     * Handles an event to move the current brick down.
     *
     * @param event The move event containing details of the action.
     * @return Data related to the outcome of the down movement, including row clearing information.
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles an event to move the current brick left.
     *
     * @param event The move event containing details of the action.
     * @return Data related to the updated view after the left movement.
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles an event to move the current brick right.
     *
     * @param event The move event containing details of the action.
     * @return Data related to the updated view after the right movement.
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles an event to rotate the current brick.
     *
     * @param event The move event containing details of the action.
     * @return Data related to the updated view after the rotation.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles an event to hard drop the current brick (move instantly to the bottom).
     *
     * @param event The move event containing details of the action.
     * @return Data related to the outcome of the hard drop, including row clearing information.
     */
    DownData onHardDrop(MoveEvent event);

    /**
     * Handles an event to create and start a new game.
     */
    void createNewGame();

    /**
     * Handles an event indicating the game is exiting or needs cleanup.
     */
    void onGameExit();
}
