package com.comp2042.ui;

import java.util.prefs.Preferences;

/**
 * Singleton class managing game settings with persistence.
 * Uses Java Preferences API to save settings between sessions.
 * Design Patterns Used:
 * - Singleton Pattern: Ensures single instance manages all game settings
 * - Persistence Pattern: Uses Java Preferences API for data persistence
 * - Strategy Pattern: Manages theme selection where each theme is a visual strategy
 */

public class GameSettings {

    private static GameSettings instance;
    private final Preferences prefs;

    // Setting keys
    private static final String GHOST_MODE_KEY = "ghostMode";
    private static final String THEME_KEY = "theme";

    // Default values
    private static final boolean DEFAULT_GHOST_MODE = true;
    private static final Theme DEFAULT_THEME = Theme.DARK;

    // Current values
    private boolean ghostModeEnabled;
    private Theme currentTheme;

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
        String themeName = prefs.get(THEME_KEY, DEFAULT_THEME.name());
        currentTheme = Theme.fromString(themeName);
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
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(Theme theme) {
        this.currentTheme = theme;
        prefs.put(THEME_KEY, theme.name());
    }
}