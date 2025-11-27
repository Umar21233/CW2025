package com.comp2042.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;

public class HelpDialog {

    public static void showHelpDialog() {
        //Create an Alert of type INFORMATION
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Controls");
        alert.setHeaderText("Control Help");
        alert.setContentText("CONTROLS ASSIST:\n" +
                "- ↓: Move piece down 1 row\n" +
                "- ↑ or W: Rotate piece\n" +
                "- Press Space: Hard Drop\n" +
                "- ←/→ or A/D: Move piece left/right");

        //APPLY CUSTOM CSS TO DIALOG
        DialogPane dialogPane = alert.getDialogPane();

        //load CSS from resources (main_menu_style.css)
        dialogPane.getStylesheets().add(
                HelpDialog.class.getResource("/main_menu_style.css").toExternalForm()
        );

        //also apply style class so .dialog-pane matches CSS
        dialogPane.getStyleClass().add("dialog-pane");

        //to prevent background bleed-through
        dialogPane.setStyle("-fx-background-color: #1a1a1a;");


        //show and wait for the user to close the dialog
        alert.showAndWait();
    }
}
