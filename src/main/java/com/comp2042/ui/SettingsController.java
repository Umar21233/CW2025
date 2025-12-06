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

    private void updateThemeButtonText() {
        btnTheme.setText("Button Theme: " + gameSettings.getCurrentTheme().getDisplayName());
    }

    @FXML private Button btnAudio;
    @FXML private Button btnTheme;
    @FXML private Button btnGhostMode;
    @FXML private Button btnBack;

    private Stage primaryStage;
    private AudioManager audioManager;
    private GameSettings gameSettings;

    public void initialize() {
        audioManager = AudioManager.getInstance();
        gameSettings = GameSettings.getInstance();

        updateGhostModeButtonText();
        updateThemeButtonText();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        // Apply theme when the stage is set
        if (primaryStage != null && primaryStage.getScene() != null) {
            ThemeManager.applyTheme(primaryStage.getScene());
        }
    }

    @FXML
    private void toggleGhostMode() {
        audioManager.playSound(com.comp2042.audio.SoundEffect.BUTTON_CLICK);

        boolean currentState = gameSettings.isGhostModeEnabled();
        gameSettings.setGhostModeEnabled(!currentState);

        updateGhostModeButtonText();
    }

    private void updateGhostModeButtonText() {
        if (gameSettings.isGhostModeEnabled()) {
            btnGhostMode.setText("Ghost Mode: ON");
        } else {
            btnGhostMode.setText("Ghost Mode: OFF");
        }
    }

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