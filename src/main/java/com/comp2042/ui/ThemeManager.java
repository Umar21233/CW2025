package com.comp2042.ui;

import javafx.scene.Scene;
import java.net.URL;

/**
 * Manages the application of different visual themes to JavaFX scenes.
 * This utility class is responsible for loading and setting CSS stylesheets
 * based on the currently selected theme from GameSettings.
 */
public class ThemeManager {

    /**
     * Applies the currently selected theme to the given JavaFX Scene.
     * It clears existing stylesheets and adds the CSS file associated with
     * the current theme from GameSettings. If the specified CSS file is not found,
     * it attempts to load a default theme.
     *
     * @param scene The JavaFX Scene to which the theme should be applied.
     */
    public static void applyTheme(Scene scene) {
        if (scene == null) {
            return;
        }

        GameSettings settings = GameSettings.getInstance();
        Theme currentTheme = settings.getCurrentTheme();

        scene.getStylesheets().clear();
        String cssFile = currentTheme.getCssFile();

        try {
            URL cssUrl = ThemeManager.class.getResource("/" + cssFile);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("CSS file not found: " + cssFile);
                // As a fallback, you might want to load a default theme
                URL defaultCssUrl = ThemeManager.class.getResource("/window_style.css");
                 if (defaultCssUrl != null) {
                    scene.getStylesheets().add(defaultCssUrl.toExternalForm());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
