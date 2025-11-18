package com.comp2042;

public final class Constants {

    private Constants() {}

    // Board size
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 25;

    // Speed (ms per tick)
    public static final int INITIAL_TICK = 400;

    // Leveling
    public static final int LINES_PER_LEVEL = 10;
    public static final int MAX_LEVEL = 10;

    public static final int[] LEVEL_SPEED = {
            400, 350, 300, 260, 230,
            200, 170, 150, 130, 120
    };

    // Score for clearing lines
    public static final int[] SCORE_LINE_CLEAR = {
            0,    // 0 lines
            50,   // 1 line
            150,  // 2 lines
            300,  // 3 lines
            500   // 4 lines
    };
}
