package com.comp2042.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 - Manages the background grid, the display matrix of landed blocks, and size constants.
 */
public class GameBoardView {

    public static final int BRICK_SIZE = 20;

    private final GridPane gamePanel;
    private final Canvas gridCanvas;
    // DECLARE the field as final, but do not initialize it here.
    private final Rectangle[][] displayMatrix;
    private final PieceRenderer renderer;

    private final double cellWidth;
    private final double cellHeight;
    private final int boardRows;
    private final int boardCols;

    public GameBoardView(GridPane gamePanel, Canvas gridCanvas, PieceRenderer renderer, int[][] boardMatrix) {
        this.gamePanel = gamePanel;
        this.gridCanvas = gridCanvas;
        // REMOVED: this.displayMatrix = displayMatrix; // This was wrong
        this.renderer = renderer;
        this.boardRows = boardMatrix.length;
        this.boardCols = boardMatrix[0].length;

        // INITIALIZE the final array field here, based on boardMatrix dimensions.
        this.displayMatrix = new Rectangle[boardRows][boardCols];

        this.cellWidth  = BRICK_SIZE + gamePanel.getHgap();
        this.cellHeight = BRICK_SIZE + gamePanel.getVgap();

        // Initialize the canvas and grid structure
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

    private void initDisplayMatrix(int[][] boardMatrix) {
        // Initialize the landed blocks (displayMatrix)
        for (int i = 2; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                // Note: The boardMatrix[i][j] value should be 0 (transparent)
                // when initializing, but using the provided value is fine if the
                // matrix already contains landed pieces on init.
                renderer.styleBrickRectangle(boardMatrix[i][j], rectangle);

                // ASSIGNMENT is now valid as the array object itself was initialized
                // in the constructor, and we are now filling its contents.
                this.displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2); // board row i -> visible row i-2
            }
        }
    }


    // --- Drawing/Update Methods ---

    private void drawGrid() {
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        gc.setStroke(Color.color(1, 1, 1, 0.18)); // 18% opacity
        gc.setLineWidth(1);

        double width  = boardCols * cellWidth  - gamePanel.getHgap();
        double height = getVisibleRows() * cellHeight;

        // vertical lines
        for (int c = 0; c <= boardCols; c++) {
            gc.strokeLine(c * cellWidth, 0, c * cellWidth, height);
        }

        // horizontal lines
        for (int r = 0; r <= getVisibleRows(); r++) {
            gc.strokeLine(0, r * cellHeight, width, r * cellHeight);
        }
    }

    /**
     - Updates the color of landed blocks based on the game board state.
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                renderer.styleBrickRectangle(board[i][j], displayMatrix[i][j]);
            }
        }
    }
}
