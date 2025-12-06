package com.comp2042.ui;

import com.comp2042.audio.AudioManager;
import com.comp2042.ui.GameSettings;
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
 */
public class SettingsController {

    @FXML private Button btnAudio;
    @FXML private Button btnGhostMode;
    @FXML private Button btnBack;

    private Stage primaryStage;
    private AudioManager audioManager;
    private GameSettings gameSettings;

    public void initialize() {
        audioManager = AudioManager.getInstance();
        gameSettings = GameSettings.getInstance();

        updateGhostModeButtonText();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
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
            primaryStage.setScene(menuScene);
            primaryStage.setTitle("Tetris Main Menu");

        } catch (IOException e) {
            System.err.println("Error loading main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}