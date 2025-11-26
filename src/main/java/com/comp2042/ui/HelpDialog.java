package com.comp2042.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HelpDialog {

    public static void showHelpDialog() {
        // Create an Alert of type INFORMATION
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Controls");
        alert.setHeaderText("Control Help");
        alert.setContentText("Use the following controls:\n" +
                "1. Down Arrow: Move piece down\n" +
                "2. Space: Hard Drop\n" +
                "3. >: Rotate piece\n" +
                "4. Left/Right Arrows or A/D: Move piece left/right");

        // Show and wait for the user to close the dialog
        alert.showAndWait();
    }
}
