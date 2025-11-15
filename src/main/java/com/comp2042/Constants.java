package com.comp2042;

//added Constants class to remove 'magic numbers' so later changes are easy
public class Constants {
    //board dimensions (matrix)
    public static final int BOARD_HEIGHT = 25;
    public static final int BOARD_WIDTH  = 10;

    //size of each visible brick square (in pixels)
    public static final int BRICK_SIZE = 20;

    //falling speed (milliseconds per tick)
    public static final int INITIAL_TICK_MILLIS = 400;

    //base score per cleared line unit
    public static final int SCORE_PER_LINE_UNIT = 50;

    //utility class – prevent instantiation
    private Constants() { }
}
