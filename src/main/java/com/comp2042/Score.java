package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import static com.comp2042.Constants.*;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

    public int get() {
        return score.get();
    }

    //OLD METHOD (needed because GameController still calls it)
    public void add(int points) {
        score.set(score.get() + points);
    }

    //NEW METHODS
    public void addSoftDrop() {
        score.set(score.get() + SCORE_SOFT_DROP);
    }

    public void addLineClear(int lines) {
        if (lines >= 0 && lines < SCORE_LINE_CLEAR.length) {
            score.set(score.get() + SCORE_LINE_CLEAR[lines]);
        }
    }

    private int totalLinesCleared = 0;

    public int getTotalLines() {
        return totalLinesCleared;
    }

    public void addLines(int lines) {
        totalLinesCleared += lines;
    }

    public void reset() {
        score.set(0);
        totalLinesCleared = 0;
    }
}

