package com.comp2042.ui;

import com.comp2042.audio.AudioManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for the Settings menu screen.
 *
 * Design Patterns Used:
 * - Singleton Pattern: Accesses GameSettings and AudioManager singletons
 */

public class SettingsController {

    /**
     * Cycles through the available themes and updates the game settings and UI.
     * Plays a button click sound effect.
     */
    @FXML
    private void cycleTheme() {
        audioManager.playSound(com.comp2042.audio.SoundEffect.BUTTON_CLICK);

        Theme currentTheme = gameSettings.getCurrentTheme();
        Theme[] themes = Theme.values();

        int currentIndex = currentTheme.ordinal();
        int nextIndex = (currentIndex + 1) % themes.length;
        Theme nextTheme = themes[nextIndex];

        gameSettings.setCurrentTheme(nextTheme);
        updateThemeButtonText();

        // Apply theme instantly
        if (btnTheme != null && btnTheme.getScene() != null) {
            ThemeManager.applyTheme(btnTheme.getScene());
        }
    }

    /**
     * Updates the text of the theme button to reflect the currently selected theme.
     */
    private void updateThemeButtonText() {
        btnTheme.setText("Button Theme: " + gameSettings.getCurrentTheme().getDisplayName());
    }

    @FXML private Button btnAudio;
    @FXML private Button btnTheme;
    @FXML private Button btnGhostMode;
    @FXML private Button btnBack;

    /** The primary stage of the application, used to set new scenes. */
    private Stage primaryStage;
    /** Singleton instance of AudioManager for playing sound effects. */
    private AudioManager audioManager;
    /** Singleton instance of GameSettings for managing game preferences. */
    private GameSettings gameSettings;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     * It initializes the AudioManager and GameSettings instances and updates the button texts.
     */
    public void initialize() {
        audioManager = AudioManager.getInstance();
        gameSettings = GameSettings.getInstance();

        updateGhostModeButtonText();
        updateThemeButtonText();
    }

    /**
     * Sets the primary stage for this controller. This is typically called by the main application.
     * The theme is applied to the scene once the stage is set.
     *
     * @param stage The primary Stage of the application.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        // Apply theme when the stage is set
        if (primaryStage != null && primaryStage.getScene() != null) {
            ThemeManager.applyTheme(primaryStage.getScene());
        }
    }

    /**
     * Toggles the ghost mode setting (on/off) and updates the UI accordingly.
     * Plays a button click sound effect.
     */
    @FXML
    private void toggleGhostMode() {
        audioManager.playSound(com.comp2042.audio.SoundEffect.BUTTON_CLICK);

        boolean currentState = gameSettings.isGhostModeEnabled();
        gameSettings.setGhostModeEnabled(!currentState);

        updateGhostModeButtonText();
    }

    /**
     * Updates the text of the ghost mode button to reflect its current state (ON/OFF).
     */
    private void updateGhostModeButtonText() {
        if (gameSettings.isGhostModeEnabled()) {
            btnGhostMode.setText("Ghost Mode: ON");
        } else {
            btnGhostMode.setText("Ghost Mode: OFF");
        }
    }

    /**
     * Opens the audio settings screen.
     * Plays a button click sound effect.
     * Loads the audio_settings.fxml, sets its controller, and displays it on the primary stage.
     */
    @FXML
    private void openAudioSettings() {
        audioManager.playSound(com.comp2042.audio.SoundEffect.BUTTON_CLICK);

        try {
            URL audioUrl = getClass().getResource("/audio_settings.fxml");
            if (audioUrl == null) {
                System.err.println("Cannot find audio_settings.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(audioUrl);
            Parent root = loader.load();

            AudioSettingsController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            Scene audioScene = new Scene(root, 600, 790);
            ThemeManager.applyTheme(audioScene); // Apply theme to the new scene
            primaryStage.setScene(audioScene);
            primaryStage.setTitle("Audio Settings");

        } catch (IOException e) {
            System.err.println("Error loading audio settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the main menu screen.
     * Plays a button click sound effect.
     * Loads the main_menu.fxml, sets its controller, and displays it on the primary stage.
     */
    @FXML
    private void backToMainMenu() {
        audioManager.playSound(com.comp2042.audio.SoundEffect.BUTTON_CLICK);

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
            ThemeManager.applyTheme(menuScene); // Apply theme to the new scene
            primaryStage.setScene(menuScene);
            primaryStage.setTitle("Tetris Main Menu");

        } catch (IOException e) {
            System.err.println("Error loading main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}