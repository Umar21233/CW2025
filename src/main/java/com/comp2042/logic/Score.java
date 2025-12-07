package com.comp2042.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 - Manages all scoring logic for the Tetris game.
 - This class encapsulates the current score, high score, and total lines cleared.
 - UI layers access score values via read-only JavaFX properties,
 while game logic updates the score strictly through controlled methods
 (add, addLines, reset), ensuring proper encapsulation and preventing misuse.
 */



public final class Score {

    /** The current score of the player. */
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    /** The highest score achieved by the player. */
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    /** The total number of lines cleared by the player in the current game. */
    private final IntegerProperty totalLines = new SimpleIntegerProperty(0);
    /** The current game level. */
    private final IntegerProperty level = new SimpleIntegerProperty(1);

    /** Indicates whether high score persistence to disk is enabled. */
    private final boolean persistenceEnabled;


    /**
     * Constructs a new Score manager with high score persistence enabled by default.
     * Loads the high score from disk upon initialization.
     */
    public Score() {
        this(true);
    }


    /**
     * Constructs a new Score manager, allowing specification of high score persistence.
     * If persistence is enabled, the high score is loaded from disk.
     *
     * @param persistenceEnabled True to enable high score persistence, false otherwise.
     */
    public Score(boolean persistenceEnabled) {
        this.persistenceEnabled = persistenceEnabled;
        if (this.persistenceEnabled) {
            //Load high score from disk when Score is created
            int savedHighScore = HighScorePersistence.loadHighScore();
            highScore.set(savedHighScore);
        }
    }

    /**
     * Returns the IntegerProperty for the current score, allowing UI elements to observe changes.
     *
     * @return The IntegerProperty for {@code score}.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Returns the IntegerProperty for the high score, allowing UI elements to observe changes.
     *
     * @return The IntegerProperty for {@code highScore}.
     */
    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    /**
     * Returns the IntegerProperty for the total lines cleared, allowing UI elements to observe changes.
     *
     * @return The IntegerProperty for {@code totalLines}.
     */
    public IntegerProperty totalLinesProperty() {
        return totalLines;
    }

    /**
     * Returns the IntegerProperty for the current level, allowing UI elements to observe changes.
     *
     * @return The IntegerProperty for {@code level}.
     */
    public IntegerProperty levelProperty() {
        return level;
    }

    /**
     * Retrieves the current score as a primitive int.
     *
     * @return The current score.
     */
    public int getScore() {
        return score.get();
    }

    /**
     * Retrieves the high score as a primitive int.
     *
     * @return The high score.
     */
    public int getHighScore() {
        return highScore.get();
    }

    /**
     * Retrieves the total lines cleared as a primitive int.
     *
     * @return The total number of lines cleared.
     */
    public int getTotalLines() {
        return totalLines.get();
    }

    /**
     * Retrieves the current game level as a primitive int.
     *
     * @return The current level.
     */
    public int getLevel() {
        return level.get();
    }

    /**
     * Checks if the current score is greater than the high score.
     * If it is, updates the high score and saves it to disk if persistence is enabled.
     */
    private void updateHighScore() {
        if (score.get() > highScore.get()) {
            highScore.set(score.get());
            //Save to disk immediately when high score is beaten
            if(persistenceEnabled) {
                HighScorePersistence.saveHighScore(highScore.get());
            }
        }
    }

    /**
     * Recalculates and updates the current game level based on the total lines cleared.
     * The level increases every {@code Constants.LINES_PER_LEVEL} lines, up to {@code Constants.MAX_LEVEL}.
     */
    private void updateLevel() {
        //Calculate level based on total lines cleared
        //Every 10 lines = next level, max level 10
        int newLevel = Math.min((totalLines.get() / Constants.LINES_PER_LEVEL) + 1, Constants.MAX_LEVEL);
        if (newLevel != level.get()) {
            level.set(newLevel);
        }
    }

    /**
     * Adds the specified amount to the current score.
     * Automatically checks and updates the high score.
     *
     * @param amount The amount to add to the score. Must be a positive value.
     */
    public void add(int amount) {
        if (amount <= 0) {
            return;
        }
        score.set(score.get() + amount);
        updateHighScore();
    }

    /**
     * Adds the specified number of cleared lines to the total lines count.
     * Automatically checks and updates the game level.
     *
     * @param lines The number of lines to add. Must be a positive value.
     */
    public void addLines(int lines) {
        if (lines <= 0) {
            return;
        }
        totalLines.set(totalLines.get() + lines);

        updateLevel();
    }


    /**
     * Resets the current game's score, lines, and level.
     * The high score is intentionally not reset to maintain persistence across games.
     */
    public void reset() {
        score.set(0);
        totalLines.set(0);
        level.set(1);
    }

}
