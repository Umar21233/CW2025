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
    /** The arc height for rounded corners of brick rectangles. */
    private static final double ARC_HEIGHT = 9;
    /** The arc width for rounded corners of brick rectangles. */
    private static final double ARC_WIDTH  = 9;
    /** The opacity level for ghost pieces. */
    private static final double GHOST_OPACITY = 0.40;

    /**
     * Maps the brick ID (1-7) to its JavaFX Color.
     */
    /**
     * Returns the JavaFX Paint object corresponding to a given brick ID.
     * Brick ID 0 typically represents a transparent or empty cell.
     *
     * @param i The integer ID of the brick (0 for transparent, 1-7 for different colors).
     * @return A JavaFX Paint object representing the color of the brick.
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
     * Applies the appropriate fill color and rounded corner styling to a given Rectangle
     * based on the provided color ID for an active or landed brick.
     *
     * @param colorId The integer ID of the brick's color.
     * @param rectangle The JavaFX Rectangle to style.
     */
    public void styleBrickRectangle(int colorId, Rectangle rectangle) {
        rectangle.setFill(getFillColor(colorId));
        rectangle.setArcHeight(ARC_HEIGHT);
        rectangle.setArcWidth(ARC_WIDTH);
    }

    /**
     * Applies a translucent fill color and rounded corner styling to a given Rectangle
     * to represent a ghost piece. The opacity is determined by GHOST_OPACITY.
     * If colorId is 0, the rectangle will be transparent.
     *
     * @param colorId The integer ID of the brick's color.
     * @param rectangle The JavaFX Rectangle to style as a ghost piece.
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
