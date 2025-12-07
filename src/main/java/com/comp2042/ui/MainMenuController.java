package com.comp2042.ui;

import com.comp2042.audio.AudioManager;
import com.comp2042.audio.SoundEffect;
import com.comp2042.logic.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for the main menu of the TetrisJFX application.
 * Manages navigation between different application screens (game, help, stats, settings).
 */
public class MainMenuController {

    @FXML private Button btnPlay; // Button to start a new game.
    @FXML private Button btnHelp; // Button to display game help and controls.
    @FXML private Button btnStats; // Button to view player statistics.
    @FXML private Button btnExit; // Button to exit the application.

    /** The primary stage of the application, used for setting and displaying scenes. */
    private Stage primaryStage;
    /** The audio manager instance for playing menu music and button click sound effects. */
    private AudioManager audioManager;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Starts playing menu background music.
     */
    @FXML
    public void initialize() {
        audioManager = AudioManager.getInstance();
        audioManager.playMenuMusic();
    }

    /**
     * Sets the primary stage for this controller, allowing it to change scenes.
     *
     * @param stage The primary Stage of the application.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Handles the action to start a new game.
     * Loads the game layout, initializes the game controller, and switches to the game scene.
     */
    @FXML
    private void startGame() {
        try {
            audioManager.playSound(SoundEffect.BUTTON_CLICK);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
            Pane gameRoot = loader.load();

            GuiController guiController = loader.getController();

            guiController.setPrimaryStage(primaryStage);

            GameController gameController = new GameController(guiController);

            audioManager.playGameMusic();

            Scene gameScene = new Scene(gameRoot, 690, 640);
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("Tetris");
            primaryStage.show();

            guiController.requestGameFocus();

        } catch (IOException e) {
            System.err.println("Error loading game view FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the help dialog with game controls and information.
     */
    @FXML
    private void showHelp() {
        HelpDialog.showHelpDialog();
    }

    /**
     * Handles the action to show player statistics.
     * Plays a button click sound, loads the stats FXML, and switches to the stats scene.
     */
    @FXML
    private void showStats() {
        audioManager.playSound(SoundEffect.BUTTON_CLICK);

        try {
            URL statsUrl = getClass().getResource("/stats.fxml");
            if (statsUrl == null) {
                System.err.println("Cannot find stats.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(statsUrl);
            Parent root = loader.load();

            StatsController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            Scene statsScene = new Scene(root, 600, 790);
            primaryStage.setScene(statsScene);
            primaryStage.setTitle("Player Statistics");

        } catch (IOException e) {
            System.err.println("Error loading stats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action to open the game settings screen.
     * Plays a button click sound, loads the settings FXML, and switches to the settings scene.
     */
    @FXML
    private void openSettings() {
        audioManager.playSound(SoundEffect.BUTTON_CLICK);

        try {
            URL settingsUrl = getClass().getResource("/settings.fxml");
            if (settingsUrl == null) {
                System.err.println("Cannot find settings.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(settingsUrl);
            Parent root = loader.load();

            com.comp2042.ui.SettingsController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            Scene settingsScene = new Scene(root, 600, 790);
            primaryStage.setScene(settingsScene);
            primaryStage.setTitle("Settings");
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action to exit the application.
     * Closes the primary stage.
     */
    @FXML
    private void exitGame() {
        primaryStage.close();
    }
}