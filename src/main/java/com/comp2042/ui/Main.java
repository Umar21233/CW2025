package com.comp2042.ui;

import com.comp2042.audio.AudioManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

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

    @Override
    public void stop() throws Exception {
        //Called when application is closing
        //High score is already saved in Score.updateHighScore()
        //Just cleanup audio
        AudioManager.getInstance().dispose();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
