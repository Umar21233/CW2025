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

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final IntegerProperty totalLines = new SimpleIntegerProperty(0);
    private final IntegerProperty level = new SimpleIntegerProperty(1);

    private final boolean persistenceEnabled;


    public Score() {
        this(true);
    }


    public Score(boolean persistenceEnabled) {
        this.persistenceEnabled = persistenceEnabled;
        if (this.persistenceEnabled) {
            //Load high score from disk when Score is created
            int savedHighScore = HighScorePersistence.loadHighScore();
            highScore.set(savedHighScore);
        }
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    public IntegerProperty totalLinesProperty() {
        return totalLines;
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public int getScore() {
        return score.get();
    }

    public int getHighScore() {
        return highScore.get();
    }

    public int getTotalLines() {
        return totalLines.get();
    }

    public int getLevel() {
        return level.get();
    }

    private void updateHighScore() {
        if (score.get() > highScore.get()) {
            highScore.set(score.get());
            //Save to disk immediately when high score is beaten
            if(persistenceEnabled) {
                HighScorePersistence.saveHighScore(highScore.get());
            }
        }
    }

    private void updateLevel() {
        //Calculate level based on total lines cleared
        //Every 10 lines = next level, max level 10
        int newLevel = Math.min((totalLines.get() / Constants.LINES_PER_LEVEL) + 1, Constants.MAX_LEVEL);
        if (newLevel != level.get()) {
            level.set(newLevel);
        }
    }

    public void add(int amount) {
        if (amount <= 0) {
            return;
        }
        score.set(score.get() + amount);
        updateHighScore();
    }

    public void addLines(int lines) {
        if (lines <= 0) {
            return;
        }
        totalLines.set(totalLines.get() + lines);

        updateLevel();
    }


    public void reset() {
        score.set(0);
        totalLines.set(0);
        level.set(1);
    }

}
