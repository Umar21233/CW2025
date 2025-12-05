package com.comp2042.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 - Test class for the Score class.
 - Tests all scoring logic including score tracking, high score updates,
  line counting, and reset functionality.
 */
class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score(false);
    }

    @Test
    @DisplayName("Initial score should be zero")
    void testInitialScore() {
        assertEquals(0, score.getScore());
    }

    @Test
    @DisplayName("Initial high score should be zero")
    void testInitialHighScore() {
        assertEquals(0, score.getHighScore());
    }

    @Test
    @DisplayName("Initial total lines should be zero")
    void testInitialTotalLines() {
        assertEquals(0, score.getTotalLines());
    }

    @Test
    @DisplayName("Adding positive score should increase current score")
    void testAddPositiveScore() {
        score.add(100);
        assertEquals(100, score.getScore());

        score.add(50);
        assertEquals(150, score.getScore());
    }

    @Test
    @DisplayName("Adding zero or negative score should not change score")
    void testAddZeroOrNegativeScore() {
        score.add(100);
        int currentScore = score.getScore();

        score.add(0);
        assertEquals(currentScore, score.getScore());

        score.add(-50);
        assertEquals(currentScore, score.getScore());
    }

    @Test
    @DisplayName("High score should update when current score exceeds it")
    void testHighScoreUpdate() {
        score.add(100);
        assertEquals(100, score.getHighScore());

        score.add(50);
        assertEquals(150, score.getHighScore());
    }

    @Test
    @DisplayName("High score should not decrease when score is reset")
    void testHighScorePreservedAfterReset() {
        score.add(500);
        assertEquals(500, score.getHighScore());

        score.reset();
        assertEquals(0, score.getScore());
        assertEquals(500, score.getHighScore());
    }

    @Test
    @DisplayName("High score should update across multiple games")
    void testHighScoreAcrossMultipleGames() {
        //First game
        score.add(300);
        assertEquals(300, score.getHighScore());

        score.reset();

        //Second game with lower score
        score.add(200);
        assertEquals(300, score.getHighScore());

        score.reset();

        //Third game with higher score
        score.add(400);
        assertEquals(400, score.getHighScore());
    }

    @Test
    @DisplayName("Adding positive lines should increase total lines")
    void testAddPositiveLines() {
        score.addLines(1);
        assertEquals(1, score.getTotalLines());

        score.addLines(3);
        assertEquals(4, score.getTotalLines());
    }

    @Test
    @DisplayName("Adding zero or negative lines should not change total lines")
    void testAddZeroOrNegativeLines() {
        score.addLines(5);
        int currentLines = score.getTotalLines();

        score.addLines(0);
        assertEquals(currentLines, score.getTotalLines());

        score.addLines(-2);
        assertEquals(currentLines, score.getTotalLines());
    }

    @Test
    @DisplayName("Reset should clear score and lines but preserve high score")
    void testReset() {
        score.add(1000);
        score.addLines(10);

        assertEquals(1000, score.getScore());
        assertEquals(10, score.getTotalLines());
        assertEquals(1000, score.getHighScore());

        score.reset();

        assertEquals(0, score.getScore());
        assertEquals(0, score.getTotalLines());
        assertEquals(1000, score.getHighScore());
    }

    @Test
    @DisplayName("Score property should be bindable")
    void testScoreProperty() {
        assertNotNull(score.scoreProperty());
        score.add(250);
        assertEquals(250, score.scoreProperty().get());
    }

    @Test
    @DisplayName("High score property should be bindable")
    void testHighScoreProperty() {
        assertNotNull(score.highScoreProperty());
        score.add(350);
        assertEquals(350, score.highScoreProperty().get());
    }

    @Test
    @DisplayName("Total lines property should be bindable")
    void testTotalLinesProperty() {
        assertNotNull(score.totalLinesProperty());
        score.addLines(7);
        assertEquals(7, score.totalLinesProperty().get());
    }

    @Test
    @DisplayName("Large score values should be handled correctly")
    void testLargeScoreValues() {
        score.add(999999);
        assertEquals(999999, score.getScore());
        assertEquals(999999, score.getHighScore());
    }

    @Test
    @DisplayName("Multiple sequential operations should work correctly")
    void testSequentialOperations() {
        score.add(100);
        score.addLines(2);
        score.add(200);
        score.addLines(3);

        assertEquals(300, score.getScore());
        assertEquals(5, score.getTotalLines());
        assertEquals(300, score.getHighScore());

        score.reset();

        assertEquals(0, score.getScore());
        assertEquals(0, score.getTotalLines());
        assertEquals(300, score.getHighScore());

        score.add(150);
        score.addLines(1);

        assertEquals(150, score.getScore());
        assertEquals(1, score.getTotalLines());
        assertEquals(300, score.getHighScore());
    }

    @Test
    @DisplayName("High score should remain unchanged when adding lower scores")
    void testHighScoreNotDecreasing() {
        score.add(1000);
        assertEquals(1000, score.getHighScore());

        score.reset();
        score.add(500);

        assertEquals(500, score.getScore());
        assertEquals(1000, score.getHighScore());
    }
}