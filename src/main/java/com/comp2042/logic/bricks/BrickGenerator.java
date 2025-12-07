package com.comp2042.logic.bricks;

/**
 * Defines the contract for generating bricks in the game.
 * Implementations will provide methods to get the current brick
 * and the next brick in sequence.
 */
public interface BrickGenerator {

    /**
     * Returns the currently active brick.
     *
     * @return The current {@code Brick} object.
     */
    Brick getBrick();

    /**
     * Returns the next brick that will be available, without changing the current brick.
     *
     * @return The next {@code Brick} object.
     */
    Brick getNextBrick();
}
