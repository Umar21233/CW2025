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

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    public IntegerProperty totalLinesProperty() {
        return totalLines;
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

    private void updateHighScore() {
        if (score.get() > highScore.get()) {
            highScore.set(score.get());
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
    }


    public void reset() {
        score.set(0);
        totalLines.set(0);

    }

}
