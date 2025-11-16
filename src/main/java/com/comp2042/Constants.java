package com.comp2042;

public class Constants {

        private Constants() {} //prevent instantiation

        //board size
        public static final int BOARD_WIDTH  = 10;
        public static final int BOARD_HEIGHT = 25;

        //tick rate (ms)
        public static final int INITIAL_TICK_MILLIS = 400;

        //scoring
        public static final int SCORE_SOFT_DROP = 1;

        //scoring for line clears: index = number of lines cleared
        public static final int[] SCORE_LINE_CLEAR = {0, 50, 150, 300, 500};

        //levels system
        public static final int LINES_PER_LEVEL = 10;
        public static final int MAX_LEVEL = 10;

        //speed per level (ms)
        public static final int[] LEVEL_SPEED = {
                400, //level 1
                350, //level 2
                300, //level 3
                260, //level 4
                230, //level 5
                200, //level 6
                170, //level 7
                150, //level 8
                130, //level 9
                120  //level 10 (fastest)
        };
    }


