package com.comp2042.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Utility class to manage Tetris brick colors and styling.
 * Separates styling logic from the UI controller and view components.
 */
public class PieceRenderer {

    // --- Styling Constants ---
    private static final double ARC_HEIGHT = 9;
    private static final double ARC_WIDTH  = 9;
    private static final double GHOST_OPACITY = 0.40;

    /**
     * Maps the brick ID (1-7) to its JavaFX Color.
     */
    public Paint getFillColor(int i) {
        switch (i) {
            case 0: return Color.TRANSPARENT;
            case 1: return Color.AQUA;
            case 2: return Color.BLUEVIOLET;
            case 3: return Color.DARKGREEN;
            case 4: return Color.YELLOW;
            case 5: return Color.RED;
            case 6: return Color.BEIGE;
            case 7: return Color.BURLYWOOD;
            default: return Color.WHITE;
        }
    }

    /**
     * Sets the fill color and styling for an active (or landed) piece rectangle.
     */
    public void styleBrickRectangle(int colorId, Rectangle rectangle) {
        rectangle.setFill(getFillColor(colorId));
        rectangle.setArcHeight(ARC_HEIGHT);
        rectangle.setArcWidth(ARC_WIDTH);
    }

    /**
     * Sets the translucent fill color and styling for a ghost piece rectangle.
     */
    public void styleGhostRectangle(int colorId, Rectangle rectangle) {
        if (colorId == 0) {
            rectangle.setFill(Color.TRANSPARENT);
        } else {
            Paint base = getFillColor(colorId);
            if (base instanceof Color c) {
                // Set the translucent color
                rectangle.setFill(
                        Color.color(c.getRed(), c.getGreen(), c.getBlue(), GHOST_OPACITY)
                );
            } else {
                rectangle.setFill(base); // Fallback for non-Color Paints
            }
        }
        rectangle.setArcHeight(ARC_HEIGHT);
        rectangle.setArcWidth(ARC_WIDTH);
    }
}
