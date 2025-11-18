package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);
    private final IntegerProperty totalLines = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty highScoreProperty() { return highScore; }
    public IntegerProperty totalLinesProperty() { return totalLines; }

    public int getScore() { return score.get(); }
    public int getHighScore() { return highScore.get(); }
    public int getTotalLines() { return totalLines.get(); }

    private void updateHighScore() {
        if (score.get() > highScore.get()) {
            highScore.set(score.get());
        }
    }

    public void add(int amount) {
        score.set(score.get() + amount);
        updateHighScore();
    }

    public void addLineClear(int lines) {
        if (lines >= 0 && lines < Constants.SCORE_LINE_CLEAR.length) {
            score.set(score.get() + Constants.SCORE_LINE_CLEAR[lines]);
            updateHighScore();
        }
    }

    public void addLines(int lines) {
        totalLines.set(totalLines.get() + lines);
    }

    public void reset() {
        score.set(0);
        totalLines.set(0);
    }
}
