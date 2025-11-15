package com.comp2042;

public class Constants {

        private Constants() {} // prevent instantiation

        //board size
        public static final int BOARD_WIDTH  = 10;
        public static final int BOARD_HEIGHT = 25;

        //tick rate (ms)
        public static final int INITIAL_TICK_MILLIS = 400;

        //scoring
        public static final int SCORE_SOFT_DROP = 1;

        //scoring for line clears: index = number of lines cleared
        public static final int[] SCORE_LINE_CLEAR = {0, 50, 150, 300, 500};
    }


