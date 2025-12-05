package com.comp2042.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void board_dimensions_are_correct() {
        assertEquals(10, Constants.BOARD_WIDTH);
        assertEquals(25, Constants.BOARD_HEIGHT);
    }

    @Test
    void initial_tick_is_correct() {
        assertEquals(400, Constants.INITIAL_TICK);
    }

    @Test
    void lines_per_level_is_correct() {
        assertEquals(5, Constants.LINES_PER_LEVEL);
    }

    @Test
    void max_level_is_correct() {
        assertEquals(10, Constants.MAX_LEVEL);
    }

    @Test
    void level_speed_array_has_correct_size() {
        assertEquals(Constants.MAX_LEVEL, Constants.LEVEL_SPEED.length);
    }

    @Test
    void score_line_clear_array_has_correct_size() {
        // Includes index 0 for 0 lines cleared
        assertEquals(5, Constants.SCORE_LINE_CLEAR.length);
    }

    @Test
    void score_line_clear_values_are_correct() {
        assertEquals(0, Constants.SCORE_LINE_CLEAR[0]);
        assertEquals(50, Constants.SCORE_LINE_CLEAR[1]);
        assertEquals(150, Constants.SCORE_LINE_CLEAR[2]);
        assertEquals(300, Constants.SCORE_LINE_CLEAR[3]);
        assertEquals(500, Constants.SCORE_LINE_CLEAR[4]);
    }
}