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
        //Start playing menu music when menu loads
        audioManager.playMenuMusic();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void startGame() {
        try {
            audioManager.playSound(SoundEffect.BUTTON_CLICK);

            //Load the Game's FXML (game layout)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
            Pane gameRoot = loader.load();

            //Retrieve the automatically created GuiController instance
            GuiController guiController = loader.getController();

            guiController.setPrimaryStage(primaryStage);

            //Initialize the GameController, passing the required GuiController
            GameController gameController = new GameController(guiController);

            //Switch to game music
            audioManager.playGameMusic();

            //Create the new Scene and switch the Stage
            Scene gameScene = new Scene(gameRoot, 690, 640); // Adjust size as needed
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("Tetris");
            primaryStage.show();

            //Ensure the game board takes focus for input
            guiController.requestGameFocus();

        } catch (IOException e) {
            System.err.println("Error loading game view FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showHelp() {
        // Show control help information in an alert dialog
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
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void exitGame() {
        // Close the application
        primaryStage.close();
    }
}
