package com.comp2042.logic;

/**
 * A utility class containing constant values used throughout the Tetris game.
 * This includes board dimensions, game speed, leveling parameters, and scoring rules.
 */
public final class Constants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constants() {}

    // Board size
    /** The width of the game board in blocks. */
    public static final int BOARD_WIDTH = 10;
    /** The height of the game board in blocks. */
    public static final int BOARD_HEIGHT = 25;

    // Speed (ms per tick)
    /** The initial delay (in milliseconds) between game ticks. */
    public static final int INITIAL_TICK = 400;

    // Leveling
    /** The number of lines required to clear to advance to the next level. */
    public static final int LINES_PER_LEVEL = 5;
    /** The maximum achievable game level. */
    public static final int MAX_LEVEL = 10;

    /**
     * An array defining the game tick speed (in milliseconds) for each level.
     * Speed decreases (game gets faster) with increasing level.
     */
    public static final int[] LEVEL_SPEED = {
            400, 320, 256, 205, 164,
            131, 105, 84, 67, 54

            //a constant 20% decrease each level
    };

    //Score for clearing lines
    /**
     * An array defining the score awarded for clearing a specific number of lines at once.
     * Index 0 is for 0 lines, index 1 for 1 line, etc.
     */
    public static final int[] SCORE_LINE_CLEAR = {
            0,    // 0 lines
            50,   // 1 line
            150,  // 2 lines
            300,  // 3 lines
            500   // 4 lines
    };
}
