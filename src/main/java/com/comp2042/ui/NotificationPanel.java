package com.comp2042.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A custom JavaFX BorderPane extension used to display transient notifications,
 * such as scores, with animation effects.
 */
public class NotificationPanel extends BorderPane {

    /**
     * Constructs a new NotificationPanel with the specified text.
     * Initializes the panel's size, creates a styled Label for the text,
     * applies a glow effect, sets text color to white, and places the label
     * in the center of the panel.
     *
     * @param text The text to be displayed in the notification.
     */
    public NotificationPanel(String text) {
        setMinHeight(200);
        setMinWidth(220);
        final Label score = new Label(text);
        score.getStyleClass().add("notificationText");
        final Effect glow = new Glow(0.6);
        score.setEffect(glow);
        score.setTextFill(Color.WHITE);
        setCenter(score);

    }

    /**
     * Displays the notification panel with fade and translate animations.
     * The panel will fade out and move upwards, and upon completion of the animation,
     * it will be removed from the provided ObservableList of Nodes.
     *
     * @param list The ObservableList of Nodes from which this panel will be removed
     *             after the animation completes.
     */
    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
        tt.setToY(this.getLayoutY() - 40);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.remove(NotificationPanel.this);
            }
        });
        transition.play();
    }
}
