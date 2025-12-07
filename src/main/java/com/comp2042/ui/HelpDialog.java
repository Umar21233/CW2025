package com.comp2042.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;

/**
 * A utility class for displaying a help dialog with game controls and scoring information.
 * Uses JavaFX Alert to present informative content to the user.
 */
public class HelpDialog {

    /**
     * Displays a modal information dialog detailing game controls, scoring rules,
     * leveling mechanics, and available settings.
     * The dialog is styled using external CSS.
     */
    public static void showHelpDialog() {
        //Create an Alert of type INFORMATION
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Controls");
        alert.setHeaderText("Control Help");

        String helpText =
                "CONTROLS:\n" +
                        "  - ↓ : Move piece down 1 row\n" +
                        "  - ↑ or W : Rotate piece\n" +
                        "  - Space : Hard drop\n" +
                        "  - ← / → or A / D : Move piece left/right\n" +
                        "\n" +
                        "SCORING (LINES CLEARED AT ONCE):\n" +
                        "  - 0 lines  →  0 points\n" +
                        "  - 1 line   →  50 points\n" +
                        "  - 2 lines  →  150 points\n" +
                        "  - 3 lines  →  300 points\n" +
                        "  - 4 lines  →  500 points\n" +
                        "\n" +
                        "LEVELS:\n" +
                        "  - Level increases every 5 lines cleared\n" +
                        "  - Total of 10 levels available\n" +
                        "\n" +
                        "SETTINGS:\n" +
                        "  - Toggle music and sound effects\n" +
                        "  - Enable / disable ghost piece\n" +
                        "  - Switch between different button themes\n";

        alert.setContentText(helpText);

        //apply custom CSS to helpdialogbox
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
