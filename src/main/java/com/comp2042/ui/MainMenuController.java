package com.comp2042.ui;

import com.comp2042.logic.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML private Button btnPlay;
    @FXML private Button btnHelp;
    @FXML private Button btnExit;

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void startGame() {
        try {
            //Load the Game's FXML (game layout)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
            Pane gameRoot = loader.load();

            //Retrieve the automatically created GuiController instance
            GuiController guiController = loader.getController();

            guiController.setPrimaryStage(primaryStage);

            //Initialize the GameController, passing the required GuiController
            GameController gameController = new GameController(guiController);

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
    private void exitGame() {
        // Close the application
        primaryStage.close();
    }
}
