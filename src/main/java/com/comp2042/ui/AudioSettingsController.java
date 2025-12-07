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

    @FXML private Slider musicVolumeSlider; // Slider for controlling music volume.
    @FXML private Slider sfxVolumeSlider; // Slider for controlling sound effects volume.
    @FXML private Label musicVolumeLabel; // Label displaying the current music volume percentage.
    @FXML private Label sfxVolumeLabel; // Label displaying the current sound effects volume percentage.
    @FXML private CheckBox musicToggle; // Checkbox to enable or disable music.
    @FXML private CheckBox sfxToggle; // Checkbox to enable or disable sound effects.
    @FXML private Button btnBack; // Button to navigate back to the previous screen.
    @FXML private Button btnTestSound; // Button to play a test sound effect.

    /** The primary stage of the application. */
    private Stage primaryStage;
    /** The audio manager instance for controlling sounds. */
    private AudioManager audioManager;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up initial volume and toggle states from AudioManager and adds listeners to UI controls.
     */
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

    /**
     * Updates the text of the music volume label to reflect the current slider value.
     */
    private void updateMusicVolumeLabel() {
        musicVolumeLabel.setText(String.format("%d%%", (int) musicVolumeSlider.getValue()));
    }

    /**
     * Updates the text of the sound effects volume label to reflect the current slider value.
     */
    private void updateSfxVolumeLabel() {
        sfxVolumeLabel.setText(String.format("%d%%", (int) sfxVolumeSlider.getValue()));
    }

    /**
     * Sets the primary stage for this controller and applies the current theme to its scene.
     *
     * @param stage The primary Stage of the application.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        if (primaryStage != null && primaryStage.getScene() != null) {
            ThemeManager.applyTheme(primaryStage.getScene());
        }
    }

    /**
     * Plays a test sound effect (PIECE_ROTATE) when the test sound button is clicked.
     */
    @FXML
    private void testSound() {
        audioManager.playSound(SoundEffect.PIECE_ROTATE);
    }

    /**
     * Handles the action to navigate back to the main settings screen.
     * Plays a button click sound and loads the settings FXML.
     */
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
