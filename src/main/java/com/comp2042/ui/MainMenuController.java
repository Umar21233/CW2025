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

    @FXML
    public void initialize() {
        audioManager = AudioManager.getInstance();
        audioManager.playMenuMusic();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
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