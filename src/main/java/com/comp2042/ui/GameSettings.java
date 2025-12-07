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

    /** The singleton instance of the GameSettings class. */
    private static GameSettings instance;
    /** The Java Preferences node used for persisting settings. */
    private final Preferences prefs;

    // Setting keys
    /** Key for storing the ghost mode setting in preferences. */
    private static final String GHOST_MODE_KEY = "ghostMode";
    /** Key for storing the theme setting in preferences. */
    private static final String THEME_KEY = "theme";

    // Default values
    /** Default value for ghost mode. */
    private static final boolean DEFAULT_GHOST_MODE = true;
    /** Default value for the application theme. */
    private static final Theme DEFAULT_THEME = Theme.DARK;

    // Current values
    /** The current state of ghost mode (enabled/disabled). */
    private boolean ghostModeEnabled;
    /** The currently selected theme. */
    private Theme currentTheme;

    /**
     * Private constructor to enforce the Singleton pattern.
     * Initializes the preferences API and loads existing settings.
     */
    private GameSettings() {
        prefs = Preferences.userNodeForPackage(GameSettings.class);
        loadSettings();
    }

    /**
     * Returns the singleton instance of the GameSettings.
     *
     * @return The single instance of GameSettings.
     */
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    /**
     * Loads game settings from the Java Preferences API.
     * Sets default values if settings are not found.
     */
    private void loadSettings() {
        ghostModeEnabled = prefs.getBoolean(GHOST_MODE_KEY, DEFAULT_GHOST_MODE);
        String themeName = prefs.get(THEME_KEY, DEFAULT_THEME.name());
        currentTheme = Theme.fromString(themeName);
    }

    // Ghost Mode
    /**
     * Checks if ghost mode is currently enabled.
     *
     * @return True if ghost mode is enabled, false otherwise.
     */
    public boolean isGhostModeEnabled() {
        return ghostModeEnabled;
    }

    /**
     * Sets the ghost mode state and persists the setting.
     *
     * @param enabled True to enable ghost mode, false to disable.
     */
    public void setGhostModeEnabled(boolean enabled) {
        this.ghostModeEnabled = enabled;
        prefs.putBoolean(GHOST_MODE_KEY, enabled);
    }

    // Theme
    /**
     * Returns the currently selected theme.
     *
     * @return The current {@code Theme} enum value.
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Sets the current theme and persists the setting.
     *
     * @param theme The {@code Theme} enum value to set.
     */
    public void setCurrentTheme(Theme theme) {
        this.currentTheme = theme;
        prefs.put(THEME_KEY, theme.name());
    }
}