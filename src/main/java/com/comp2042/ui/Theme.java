package com.comp2042.ui;

/**
 * Strategy interface for different visual themes.
 * Each theme defines its own color scheme and styling.
 */
public enum Theme {
    DARK("Dark", "window_style.css"),
    LIGHT("Light", "window_style_light.css"),
    NEON("Neon", "window_style_neon.css"),
    RETRO("Retro", "window_style_retro.css");

    private final String displayName;
    private final String cssFile;

    Theme(String displayName, String cssFile) {
        this.displayName = displayName;
        this.cssFile = cssFile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCssFile() {
        return cssFile;
    }

    public static Theme fromString(String name) {
        for (Theme theme : Theme.values()) {
            if (theme.name().equalsIgnoreCase(name)) {
                return theme;
            }
        }
        return DARK;
    }
}
