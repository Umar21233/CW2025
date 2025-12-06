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

public class MainMenuController {

    @FXML private Button btnPlay;
    @FXML private Button btnHelp;
    @FXML private Button btnExit;

    private Stage primaryStage;
    private AudioManager audioManager;
    private GameSettings gameSettings;

    @FXML
    public void initialize() {
        audioManager = AudioManager.getInstance();
        gameSettings = GameSettings.getInstance();
        audioManager.playMenuMusic();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        if (primaryStage != null && primaryStage.getScene() != null) {
            ThemeManager.applyTheme(primaryStage.getScene());
        }
    }

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
            ThemeManager.applyTheme(gameScene);
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("Tetris");

            guiController.requestGameFocus();

        } catch (IOException e) {
            System.err.println("Error loading game view FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showHelp() {
        HelpDialog.showHelpDialog();
    }

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
            ThemeManager.applyTheme(settingsScene);
            primaryStage.setScene(settingsScene);
            primaryStage.setTitle("Settings");

        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void exitGame() {
        primaryStage.close();
    }
}