package com.comp2042.ui;

import com.comp2042.audio.AudioManager;
import com.comp2042.audio.SoundEffect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for the Audio Settings screen with volume sliders.
 */
public class AudioSettingsController {

    @FXML private Slider musicVolumeSlider;
    @FXML private Slider sfxVolumeSlider;
    @FXML private Label musicVolumeLabel;
    @FXML private Label sfxVolumeLabel;
    @FXML private CheckBox musicToggle;
    @FXML private CheckBox sfxToggle;
    @FXML private Button btnBack;
    @FXML private Button btnTestSound;

    private Stage primaryStage;
    private AudioManager audioManager;

    @FXML
    public void initialize() {
        audioManager = AudioManager.getInstance();

        // Set initial values from AudioManager
        musicVolumeSlider.setValue(audioManager.getMusicVolume() * 100);
        sfxVolumeSlider.setValue(audioManager.getSfxVolume() * 100);
        musicToggle.setSelected(audioManager.isMusicEnabled());
        sfxToggle.setSelected(audioManager.isSfxEnabled());

        // Update labels
        updateMusicVolumeLabel();
        updateSfxVolumeLabel();

        // Add listeners for sliders
        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setMusicVolume(newVal.doubleValue() / 100.0);
            updateMusicVolumeLabel();
        });

        sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setSfxVolume(newVal.doubleValue() / 100.0);
            updateSfxVolumeLabel();
        });

        // Add listeners for checkboxes
        musicToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setMusicEnabled(newVal);
            if (newVal) {
                audioManager.playMenuMusic();
            }
        });

        sfxToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setSfxEnabled(newVal);
        });
    }

    private void updateMusicVolumeLabel() {
        musicVolumeLabel.setText(String.format("%d%%", (int) musicVolumeSlider.getValue()));
    }

    private void updateSfxVolumeLabel() {
        sfxVolumeLabel.setText(String.format("%d%%", (int) sfxVolumeSlider.getValue()));
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        if (primaryStage != null && primaryStage.getScene() != null) {
            ThemeManager.applyTheme(primaryStage.getScene());
        }
    }

    @FXML
    private void testSound() {
        audioManager.playSound(SoundEffect.PIECE_ROTATE);
    }

    @FXML
    private void backToSettings() {
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
            ThemeManager.applyTheme(settingsScene);
            primaryStage.setScene(settingsScene);
            primaryStage.setTitle("Settings");
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
