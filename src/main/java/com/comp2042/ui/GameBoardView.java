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
    /** The standard size of a single brick block in pixels. */
    public static final int BRICK_SIZE = 20;

    /** The JavaFX GridPane that holds the visual representation of the game board. */
    private final GridPane gamePanel;
    /** The JavaFX Canvas used to draw the background grid lines. */
    private final Canvas gridCanvas;
    /** A 2D array of JavaFX Rectangle objects representing the landed blocks on the board. */
    private final Rectangle[][] displayMatrix;
    /** The renderer responsible for styling individual brick rectangles. */
    private final PieceRenderer renderer;

    /** The calculated width of a single cell, including horizontal gap. */
    private final double cellWidth;
    /** The calculated height of a single cell, including vertical gap. */
    private final double cellHeight;
    /** The total number of rows in the game board matrix (including hidden rows). */
    private final int boardRows;
    /** The total number of columns in the game board matrix. */
    private final int boardCols;

    /**
     * Constructs a new GameBoardView, initializing the visual components for the game board.
     *
     * @param gamePanel The GridPane where landed bricks will be displayed.
     * @param gridCanvas The Canvas used for drawing the background grid.
     * @param renderer The PieceRenderer for styling brick elements.
     * @param boardMatrix The initial 2D array representing the game board state.
     */
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
    /**
     * Returns the calculated width of a single cell on the game board, including any horizontal gap.
     *
     * @return The cell width in pixels.
     */
    public double getCellWidth() { return cellWidth; }
    /**
     * Returns the calculated height of a single cell on the game board, including any vertical gap.
     *
     * @return The cell height in pixels.
     */
    public double getCellHeight() { return cellHeight; }
    /**
     * Returns the number of visible rows on the game board.
     *
     * @return The count of visible rows.
     */
    public int getVisibleRows() { return boardRows - 2; }


    // Setup Methods

    /**
     * Initializes the Canvas for drawing grid lines.
     * Sets canvas dimensions and draws the grid.
     */
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
     *
     * @param boardMatrix The initial state of the game board matrix.
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
    /**
     * Draws the grid lines on the {@code gridCanvas}.
     * Clears previous drawings and redraws horizontal and vertical lines.
     */
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
    /**
     * Updates the visual representation of the landed blocks on the game board
     * based on the current state of the game matrix.
     *
     * @param board The current 2D integer array representing the game board.
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                renderer.styleBrickRectangle(board[i][j], displayMatrix[i][j]);
            }
        }
    }
}