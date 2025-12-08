package com.comp2042.ui;

import com.comp2042.audio.AudioManager;
import com.comp2042.logic.HighScorePersistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The main entry point for the TetrisJFX application.
 * This class extends {@code javafx.application.Application} and is responsible for
 * initializing the primary stage and loading the main menu.
 */
public class Main extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be passed to the application
     *                     constructor and will be the first stage shown.
     * @throws Exception if something goes wrong.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        synchronizeHighScores();

        URL location = getClass().getResource("/main_menu.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();

        //Get the controller of the Main Menu
        MainMenuController controller = fxmlLoader.getController();

        //Set primary stage to show the main menu
        primaryStage.setTitle("TetrisJFX - Main Menu");
        Scene scene = new Scene(root, 600, 790); // Adjusted size to match your FXML/Game size
        primaryStage.setScene(scene);
        primaryStage.show();

        //Set primaryStage in the controller to allow switching scenes later
        controller.setPrimaryStage(primaryStage);

        //Handle application close - cleanup audio
        primaryStage.setOnCloseRequest(event -> {
            AudioManager.getInstance().dispose();
        });
    }

    private void synchronizeHighScores() {
        PlayerStats playerStats = PlayerStats.getInstance();
        int playerStatsHighScore = playerStats.getHighestScore();

        int fileHighScore = HighScorePersistence.loadHighScore();

        if (playerStatsHighScore > fileHighScore) {
            HighScorePersistence.saveHighScore(playerStatsHighScore);
        } else if (fileHighScore > playerStatsHighScore) {
            playerStats.setHighestScore(fileHighScore);
        }
    }


    /**
     * This method is called when the application should stop.
     * Performs cleanup tasks, such as disposing audio resources.
     *
     * @throws Exception if something goes wrong during cleanup.
     */
    @Override
    public void stop() throws Exception {
        //Called when application is closing
        //High score is already saved in Score.updateHighScore()
        //Just cleanup audio
        AudioManager.getInstance().dispose();
        super.stop();
    }

    /**
     * The main method is the entry point of the Java application.
     * This method is not required for JavaFX applications that run on an embedded system
     * or as applets. The launcher can be configured to call the {@code start} method directly.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
