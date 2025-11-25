package com.comp2042.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Manages the background grid, the display matrix of landed blocks, and size constants.
 * Adheres to SRP by handling all aspects of the static game background.
 */
public class GameBoardView {

    // Constant moved from GuiController
    public static final int BRICK_SIZE = 20;

    private final GridPane gamePanel;
    private final Canvas gridCanvas;
    // Declared final and initialized in the constructor
    private final Rectangle[][] displayMatrix;
    private final PieceRenderer renderer;

    private final double cellWidth;
    private final double cellHeight;
    private final int boardRows;
    private final int boardCols;

    public GameBoardView(GridPane gamePanel, Canvas gridCanvas, PieceRenderer renderer, int[][] boardMatrix) {
        this.gamePanel = gamePanel;
        this.gridCanvas = gridCanvas;
        this.renderer = renderer;
        this.boardRows = boardMatrix.length;
        this.boardCols = boardMatrix[0].length;

        this.displayMatrix = new Rectangle[boardRows][boardCols];

        // Calculations moved from GuiController
        this.cellWidth  = BRICK_SIZE + gamePanel.getHgap();
        this.cellHeight = BRICK_SIZE + gamePanel.getVgap();

        initGridCanvas();
        initDisplayMatrix(boardMatrix);
    }

    //Public Getters
    public double getCellWidth() { return cellWidth; }
    public double getCellHeight() { return cellHeight; }
    public int getVisibleRows() { return boardRows - 2; }


    // Setup Methods

    private void initGridCanvas() {
        int visibleRows = getVisibleRows();

        double canvasWidth  = boardCols * cellWidth  - gamePanel.getHgap();
        double canvasHeight = visibleRows * cellHeight - gamePanel.getVgap() - 3.5; // Fix gap

        gridCanvas.setWidth(canvasWidth);
        gridCanvas.setHeight(canvasHeight);
        gridCanvas.setLayoutX(0);
        gridCanvas.setLayoutY(0);

        drawGrid();
    }

    /**
     * Initializes the static grid of rectangles for landed blocks.
     * Includes the critical fix to clear old rectangles on new game start.
     */
    private void initDisplayMatrix(int[][] boardMatrix) {

        // FIX for 'Play Again': Remove all previous Rectangle objects (landed pieces) from the gamePanel.
        // This ensures a clean slate when the GameBoardView is recreated.
        gamePanel.getChildren().removeIf(node -> node instanceof Rectangle);

        // Initialize the landed blocks (displayMatrix)
        for (int i = 2; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                renderer.styleBrickRectangle(boardMatrix[i][j], rectangle);

                this.displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2); // board row i -> visible row i-2
            }
        }
    }


    // --- Drawing/Update Methods ---

    // drawGrid method moved from GuiController
    private void drawGrid() {
        if (gridCanvas == null) return;
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        gc.setStroke(Color.color(1, 1, 1, 0.18)); // 18% opacity
        gc.setLineWidth(1);

        double width  = boardCols * cellWidth  - gamePanel.getHgap();
        double height = getVisibleRows() * cellHeight;

        // vertical lines
        for (int c = 0; c <= boardCols; c++) {
            double x = c * cellWidth;
            gc.strokeLine(x, 0, x, height);
        }

        // horizontal lines
        for (int r = 0; r <= getVisibleRows(); r++) {
            double y = r * cellHeight;
            gc.strokeLine(0, y, width, y);
        }
    }

    /**
     * Updates the color of landed blocks based on the game board state.
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                renderer.styleBrickRectangle(board[i][j], displayMatrix[i][j]);
            }
        }
    }
}