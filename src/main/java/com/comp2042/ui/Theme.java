package com.comp2042.ui;

/**
 * Enum representing available visual themes for the game.

 * Design Patterns Used:
 * - Strategy Pattern: Each theme represents a different visual strategy
 * - Enum Pattern: Type-safe theme selection with predefined constants
 */

public enum Theme {
    /** Dark theme with a default styling. */
    DARK("Dark", "window_style.css"),
    /** Light theme with a bright styling. */
    LIGHT("Light", "window_style_light.css"),
    /** Neon theme with vibrant, glowing elements. */
    NEON("Neon", "window_style_neon.css"),
    /** Retro theme with an old-school aesthetic. */
    RETRO("Retro", "window_style_retro.css");

    /** The human-readable name of the theme. */
    private final String displayName;
    /** The CSS file associated with this theme. */
    private final String cssFile;

    /**
     * Constructs a new Theme enum constant.
     * @param displayName The display name of the theme.
     * @param cssFile The CSS file path for the theme.
     */
    Theme(String displayName, String cssFile) {
        this.displayName = displayName;
        this.cssFile = cssFile;
    }

    /**
     * Returns the human-readable display name of the theme.
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the CSS file name associated with this theme.
     * @return The CSS file name.
     */
    public String getCssFile() {
        return cssFile;
    }

    /**
     * Converts a string representation to a Theme enum constant (case-insensitive).
     * Defaults to the DARK theme if no matching theme is found.
     * @param name The string name of the theme.
     * @return The corresponding Theme enum constant.
     */
    public static Theme fromString(String name) {
        for (Theme theme : Theme.values()) {
            if (theme.name().equalsIgnoreCase(name)) {
                return theme;
            }
        }
        return DARK;
    }
}
