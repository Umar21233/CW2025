package com.comp2042.ui;

import com.comp2042.logic.*;
import com.comp2042.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane ghostPanel;     //translucent ghost piece

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Canvas gridCanvas;       //background grid

    @FXML
    private Group groupNotification;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Label scoreLabel, highLabel;

    @FXML
    private Pane nextPane;

    @FXML
    private Button btnPlay, btnPause, btnSettings;

    private Rectangle[][] displayMatrix;
    private InputEventListener eventListener;
    private Rectangle[][] rectangles;
    private Rectangle[][] nextRects;
    private Rectangle[][] ghostRects;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(
                getClass().getClassLoader().getResource("digital.ttf").toExternalForm(),
                38
        );
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!isPause.get() && !isGameOver.get()) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(
                                new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(
                                new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(
                                new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                        keyEvent.consume();
                    }
                }

                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });

        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        //Configure and draw the background grid

        int cols        = boardMatrix[0].length;
        int visibleRows = boardMatrix.length - 2; // top 2 = hidden spawn rows

        double cellWidth  = BRICK_SIZE + gamePanel.getHgap();
        double cellHeight = BRICK_SIZE + gamePanel.getVgap();

        double canvasWidth  = cols * cellWidth  - gamePanel.getHgap();
        double canvasHeight = visibleRows * cellHeight - gamePanel.getVgap() - 3.5;  //hardcoded to fix a gap

        gridCanvas.setWidth(canvasWidth);
        gridCanvas.setHeight(canvasHeight);

        // StackPane origin is (0,0), same for gamePanel / ghostPanel / brickPanel
        gridCanvas.setLayoutX(0);
        gridCanvas.setLayoutY(0);

        drawGrid(cols, visibleRows);

        //Background cells (landed blocks)

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2); // board row i -> visible row i-2
            }
        }

        //Falling brick (active piece)

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        //Ghost brick (same shape, at ghostY)

        ghostRects = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle g = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setGhostRectangleData(brick.getBrickData()[i][j], g);
                ghostRects[i][j] = g;
                ghostPanel.add(g, j, i);
            }
        }

        //board row 2 = visible row 0
        int visibleY      = brick.getyPosition()      - 2;
        int visibleGhostY = brick.getGhostYPosition() - 2;

        //Align to the SAME grid as background using translations
        brickPanel.setTranslateX(brick.getxPosition() * cellWidth);
        brickPanel.setTranslateY(visibleY * cellHeight);

        ghostPanel.setTranslateX(brick.getxPosition() * cellWidth);
        ghostPanel.setTranslateY(visibleGhostY * cellHeight);

        //Next piece preview

        nextPane.getChildren().clear();
        nextRects = new Rectangle[4][4];

        double gridSize = BRICK_SIZE * 4;

        double paneWInit = nextPane.getWidth()  > 0 ? nextPane.getWidth()  : nextPane.getPrefWidth();
        double paneHInit = nextPane.getHeight() > 0 ? nextPane.getHeight() : nextPane.getPrefHeight();
        double offsetXInit = (paneWInit - gridSize) / 2.0;
        double offsetYInit = (paneHInit - gridSize) / 2.0;

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setFill(Color.TRANSPARENT);
                nextRects[r][c] = rect;

                rect.setLayoutX(offsetXInit + c * BRICK_SIZE);
                rect.setLayoutY(offsetYInit + r * BRICK_SIZE);

                nextPane.getChildren().add(rect);
            }
        }

        nextPane.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            double paneW = newB.getWidth();
            double paneH = newB.getHeight();
            double offsetX = (paneW - gridSize) / 2.0;
            double offsetY = (paneH - gridSize) / 2.0;

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    Rectangle rect = nextRects[r][c];
                    rect.setLayoutX(offsetX + c * BRICK_SIZE);
                    rect.setLayoutY(offsetY + r * BRICK_SIZE);
                }
            }
        });

        int[][] nextData = brick.getNextBrickData();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                setRectangleData(nextData[r][c], nextRects[r][c]);
            }
        }

        //Timeline
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        isPause.set(false);
        isGameOver.set(false);
        btnPause.setDisable(false);
        btnPause.setText("Pause");
    }




    //Draws the faint background grid (medium contrast)
    private void drawGrid(int cols, int rows) {
        if (gridCanvas == null) return;

        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        gc.setStroke(Color.color(1, 1, 1, 0.18)); // 18% opacity
        gc.setLineWidth(1);

        double cellWidth  = BRICK_SIZE + gamePanel.getHgap();
        double cellHeight = BRICK_SIZE + gamePanel.getVgap();

        double width  = cols * cellWidth  - gamePanel.getHgap();
        double height = rows * cellHeight;

        // vertical lines
        for (int c = 0; c <= cols; c++) {
            double x = c * cellWidth;
            gc.strokeLine(x, 0, x, height);
        }

        // horizontal lines
        for (int r = 0; r <= rows; r++) {
            double y = r * cellHeight;
            gc.strokeLine(0, y, width, y);
        }
    }



    private Paint getFillColor(int i) {
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

    private void refreshBrick(ViewData brick) {
        if (!isPause.get()) {
            double cellWidth  = BRICK_SIZE + gamePanel.getHgap();
            double cellHeight = BRICK_SIZE + gamePanel.getVgap();

            int visibleY      = brick.getyPosition()      - 2;
            int visibleGhostY = brick.getGhostYPosition() - 2;

            // active piece
            brickPanel.setTranslateX(brick.getxPosition() * cellWidth);
            brickPanel.setTranslateY(visibleY * cellHeight);

            // ghost piece
            if (ghostPanel != null && ghostRects != null) {
                ghostPanel.setTranslateX(brick.getxPosition() * cellWidth);
                ghostPanel.setTranslateY(visibleGhostY * cellHeight);
            }

            // update cell colours
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    int value = brick.getBrickData()[i][j];
                    setRectangleData(value, rectangles[i][j]);
                    if (ghostRects != null) {
                        setGhostRectangleData(value, ghostRects[i][j]);
                    }
                }
            }

            int[][] nextData = brick.getNextBrickData();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    setRectangleData(nextData[i][j], nextRects[i][j]);
                }
            }
        }
    }





    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void setGhostRectangleData(int color, Rectangle rectangle) {
        if (color == 0) {
            rectangle.setFill(Color.TRANSPARENT);
        } else {
            Paint base = getFillColor(color);
            if (base instanceof Color) {
                Color c = (Color) base;
                rectangle.setFill(
                        Color.color(c.getRed(), c.getGreen(), c.getBlue(), 0.40) // 40% opacity
                );
            } else {
                rectangle.setFill(base);
            }
        }
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (!isPause.get()) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null &&
                    downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel =
                        new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    private void hardDrop(MoveEvent event) {
        DownData downData = eventListener.onHardDrop(event);

        if (downData.getClearRow() != null &&
                downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel =
                    new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }

        refreshBrick(downData.getViewData());
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));
    }

    public void bindHighScore(IntegerProperty highProp) {
        highLabel.textProperty().bind(Bindings.format("High Score: %d", highProp));
    }

    public void gameOver() {
        if (timeLine != null) {
            timeLine.stop();
        }
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
        isPause.set(false);

        btnPause.setDisable(true);
        btnPause.setText("Pause");
    }

    public void newGame(ActionEvent actionEvent) {
        if (timeLine != null) {
            timeLine.stop();
        }

        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();

        if (timeLine != null) {
            timeLine.play();
        }

        isPause.set(false);
        isGameOver.set(false);
        btnPause.setDisable(false);
        btnPause.setText("Pause");
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (timeLine == null || isGameOver.get()) {
            return;
        }

        if (isPause.get()) {
            timeLine.play();
            isPause.set(false);
            btnPause.setText("Pause");
        } else {
            timeLine.pause();
            isPause.set(true);
            btnPause.setText("Resume");
        }

        gamePanel.requestFocus();
    }
}
