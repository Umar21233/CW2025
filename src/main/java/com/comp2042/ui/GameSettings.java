package com.comp2042.ui;

import java.util.prefs.Preferences;

/**
 * Singleton class managing game settings with persistence.
 * Uses Java Preferences API to save settings between sessions.
 */
public class GameSettings {

    private static GameSettings instance;
    private final Preferences prefs;

    // Setting keys
    private static final String GHOST_MODE_KEY = "ghostMode";
    private static final String THEME_KEY = "theme";

    // Default values
    private static final boolean DEFAULT_GHOST_MODE = true;
    private static final String DEFAULT_THEME = "DARK";

    // Current values
    private boolean ghostModeEnabled;
    private String currentTheme;

    private GameSettings() {
        prefs = Preferences.userNodeForPackage(GameSettings.class);
        loadSettings();
    }

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    private void loadSettings() {
        ghostModeEnabled = prefs.getBoolean(GHOST_MODE_KEY, DEFAULT_GHOST_MODE);
        currentTheme = prefs.get(THEME_KEY, DEFAULT_THEME);
    }

    // Ghost Mode
    public boolean isGhostModeEnabled() {
        return ghostModeEnabled;
    }

    public void setGhostModeEnabled(boolean enabled) {
        this.ghostModeEnabled = enabled;
        prefs.putBoolean(GHOST_MODE_KEY, enabled);
    }

    // Theme
    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(String theme) {
        this.currentTheme = theme;
        prefs.put(THEME_KEY, theme);
    }
}
