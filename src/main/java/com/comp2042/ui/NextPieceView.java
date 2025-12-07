package com.comp2042.ui;

import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Manages the display of the next falling piece preview.
 * Adheres to SRP by handling the visualization of the upcoming piece.
 */
public class NextPieceView {

    /** The JavaFX Pane where the next piece preview is displayed. */
    private final Pane nextPane;
    /** The renderer responsible for styling the individual brick rectangles. */
    private final PieceRenderer renderer;
    /** A 2D array of Rectangles representing the 4x4 grid for the next piece preview. */
    private final Rectangle[][] nextRects = new Rectangle[4][4];
    /** The calculated size of the 4x4 grid used for the next piece preview. */
    private final double gridSize;

    /**
     * Constructs a new NextPieceView.
     * Initializes the preview pane, renderer, and calculates the grid size.
     *
     * @param nextPane The Pane where the next piece will be displayed.
     * @param renderer The PieceRenderer instance to style the brick rectangles.
     */
    public NextPieceView(Pane nextPane, PieceRenderer renderer) {
        this.nextPane = nextPane;
        this.renderer = renderer;
        this.gridSize = GameBoardView.BRICK_SIZE * 4;
        initPaneStructure();
    }

    /**
     * Initializes the structure of the next piece preview pane.
     * This method clears existing children, sets up a 4x4 grid of Rectangle objects,
     * centers them within the pane, and adds a listener to re-center the grid
     * if the pane is resized.
     */
    private void initPaneStructure() {
        // Clear the pane before re-adding elements on new game.
        nextPane.getChildren().clear();

        // Initial setup for centering the 4x4 grid
        Bounds bounds = nextPane.getLayoutBounds();
        double paneWInit = bounds.getWidth();
        double paneHInit = bounds.getHeight();
        double offsetXInit = (paneWInit - gridSize) / 2.0;
        double offsetYInit = (paneHInit - gridSize) / 2.0;

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Rectangle rect = new Rectangle(GameBoardView.BRICK_SIZE, GameBoardView.BRICK_SIZE);
                renderer.styleBrickRectangle(0, rect); // Start transparent
                nextRects[r][c] = rect;

                rect.setLayoutX(offsetXInit + c * GameBoardView.BRICK_SIZE);
                rect.setLayoutY(offsetYInit + r * GameBoardView.BRICK_SIZE);

                nextPane.getChildren().add(rect);
            }
        }

        // Listener to keep the preview centered when the pane is resized
        nextPane.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            double paneW = newB.getWidth();
            double paneH = newB.getHeight();
            double offsetX = (paneW - gridSize) / 2.0;
            double offsetY = (paneH - gridSize) / 2.0;

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    Rectangle rect = nextRects[r][c];
                    rect.setLayoutX(offsetX + c * GameBoardView.BRICK_SIZE);
                    rect.setLayoutY(offsetY + r * GameBoardView.BRICK_SIZE);
                }
            }
        });
    }

    /**
     * Updates the colors of the next piece preview based on the piece data.
     * @param nextData A 2D array representing the shape and color of the next piece.
     */
    public void update(int[][] nextData) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                renderer.styleBrickRectangle(nextData[r][c], nextRects[r][c]);
            }
        }
    }
}