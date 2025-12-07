package com.comp2042.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * A simple JavaFX panel to display a "GAME OVER" message.
 * It extends {@code BorderPane} and centers a styled Label.
 */
public class GameOverPanel extends BorderPane {

    /**
     * Constructs a new GameOverPanel.
     * Initializes and styles a "GAME OVER" label and sets it in the center of the pane.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }
}

