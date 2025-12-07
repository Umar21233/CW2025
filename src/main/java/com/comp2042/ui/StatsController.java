package com.comp2042.ui;

import com.comp2042.audio.AudioManager;
import com.comp2042.audio.SoundEffect;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * Controller for the Player Statistics screen.
 * Displays cumulative gameplay stats with animated count-up effects.
 */
public class StatsController {

    @FXML private Label lblTotalGames;
    @FXML private Label lblTotalPoints;
    @FXML private Label lblHighestScore;
    @FXML private Label lblHighestCombo;
    @FXML private Label lblHighestLevel;
    @FXML private Button btnReset;
    @FXML private Button btnBack;

    /** The primary stage of the application, used to set new scenes. */
    private Stage primaryStage;
    /** Singleton instance of AudioManager for playing sound effects. */
    private AudioManager audioManager;
    /** Singleton instance of PlayerStats for accessing and managing player statistics. */
    private PlayerStats playerStats;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     * It initializes the AudioManager and PlayerStats instances and starts the animation of statistics.
     */
    @FXML
    public void initialize() {
        audioManager = AudioManager.getInstance();
        playerStats = PlayerStats.getInstance();

        animateStats();
    }

    /**
     * Sets the primary stage for this controller. This is typically called by the main application.
     *
     * @param stage The primary Stage of the application.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Animates the display of various player statistics (total games, total points,
     * highest score, highest combo, highest level) using a count-up effect.
     */
    private void animateStats() {
        // Animate Total Games
        animateNumber(lblTotalGames, 0, playerStats.getTotalGamesPlayed(), 1000, "");

        // Animate Total Points
        animateNumber(lblTotalPoints, 0, (int) playerStats.getTotalPointsScored(), 1200, "");

        // Animate Highest Score
        animateNumber(lblHighestScore, 0, playerStats.getHighestScore(), 1000, "");

        // Animate Highest Combo
        animateNumber(lblHighestCombo, 0, playerStats.getHighestCombo(), 800, " Lines");

        // Animate Highest Level
        animateNumber(lblHighestLevel, 0, playerStats.getHighestLevel(), 800, "");
    }

    /**
     * Animates a number displayed in a JavaFX Label from a starting value to an ending value
     * over a specified duration.
     *
     * @param label The JavaFX Label to update with the animated number.
     * @param start The starting integer value for the animation.
     * @param end The final integer value for the animation.
     * @param durationMs The duration of the animation in milliseconds.
     * @param suffix An optional string suffix to append to the displayed number (e.g., " Lines").
     */
    private void animateNumber(Label label, int start, int end, int durationMs, String suffix) {
        if (end == 0) {
            label.setText("0" + suffix);
            return;
        }

        final int steps = 30;
        final int increment = Math.max(1, (end - start) / steps);
        final int[] currentValue = {start};

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(durationMs / steps), event -> {
            currentValue[0] += increment;
            if (currentValue[0] >= end) {
                currentValue[0] = end;
                label.setText(String.format("%,d", currentValue[0]) + suffix);
            } else {
                label.setText(String.format("%,d", currentValue[0]) + suffix);
            }
        }));

        timeline.setCycleCount(steps);
        timeline.play();
    }

    /**
     * Handles the action of the reset statistics button.
     * Displays a confirmation dialog before resetting all player statistics.
     * Plays a button click sound effect.
     */
    @FXML
    private void resetStats() {
        audioManager.playSound(SoundEffect.BUTTON_CLICK);

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Reset Statistics");
        confirmation.setHeaderText("Are you sure?");
        confirmation.setContentText("This will permanently reset all your statistics!");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            playerStats.resetStats();

            // Reset labels immediately
            lblTotalGames.setText("0");
            lblTotalPoints.setText("0");
            lblHighestScore.setText("0");
            lblHighestCombo.setText("0 Lines");
            lblHighestLevel.setText("1");

            audioManager.playSound(SoundEffect.BUTTON_CLICK);
        }
    }

    /**
     * Handles the action of the back button, navigating the user back to the main menu.
     * Plays a button click sound effect.
     */
    @FXML
    private void goBack() {
        audioManager.playSound(SoundEffect.BUTTON_CLICK);

        try {
            URL menuUrl = getClass().getResource("/main_menu.fxml");
            if (menuUrl == null) {
                System.err.println("Cannot find main_menu.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(menuUrl);
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            Scene menuScene = new Scene(root, 600, 790);
            primaryStage.setScene(menuScene);
            primaryStage.setTitle("Tetris Main Menu");

        } catch (IOException e) {
            System.err.println("Error loading main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}