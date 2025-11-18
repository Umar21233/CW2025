package com.comp2042;

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
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Label scoreLabel, highLabel, levelLabel;

    @FXML
    private Pane nextPane;

    @FXML
    private Button btnPlay, btnPause, btnSettings;

    private Rectangle[][] displayMatrix;
    private InputEventListener eventListener;
    private Rectangle[][] rectangles;
    private Rectangle[][] nextRects;
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
        //background cells
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        //falling brick
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(
                gamePanel.getLayoutX()
                        + brick.getxPosition() * brickPanel.getVgap()
                        + brick.getxPosition() * BRICK_SIZE
        );
        brickPanel.setLayoutY(
                -42 + gamePanel.getLayoutY()
                        + brick.getyPosition() * brickPanel.getHgap()
                        + brick.getyPosition() * BRICK_SIZE
        );

        // --- Next block preview: 4x4 grid, centred inside nextPane ---

        nextPane.getChildren().clear();
        nextRects = new Rectangle[4][4];

        double grid = BRICK_SIZE * 4;

        // initial centering using current / preferred size
        double paneWInit = nextPane.getWidth()  > 0 ? nextPane.getWidth()  : nextPane.getPrefWidth();
        double paneHInit = nextPane.getHeight() > 0 ? nextPane.getHeight() : nextPane.getPrefHeight();
        double offsetXInit = (paneWInit - grid) / 2.0;
        double offsetYInit = (paneHInit - grid) / 2.0;

        // create rectangles, positioned centred from the very first frame
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

        // keep them centred if the pane is resized later
        nextPane.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            double paneW = newB.getWidth();
            double paneH = newB.getHeight();
            double offsetX = (paneW - grid) / 2.0;
            double offsetY = (paneH - grid) / 2.0;

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    Rectangle rect = nextRects[r][c];
                    rect.setLayoutX(offsetX + c * BRICK_SIZE);
                    rect.setLayoutY(offsetY + r * BRICK_SIZE);
                }
            }
        });

        // fill with the initial "next" brick data
        int[][] nextData = brick.getNextBrickData();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                setRectangleData(nextData[r][c], nextRects[r][c]);
            }
        }



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
            brickPanel.setLayoutX(
                    gamePanel.getLayoutX()
                            + brick.getxPosition() * brickPanel.getVgap()
                            + brick.getxPosition() * BRICK_SIZE
            );
            brickPanel.setLayoutY(
                    -42 + gamePanel.getLayoutY()
                            + brick.getyPosition() * brickPanel.getHgap()
                            + brick.getyPosition() * BRICK_SIZE
            );

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
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

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));
    }

    public void bindHighScore(IntegerProperty highProp) {
        highLabel.textProperty().bind(Bindings.format("High Score: %d", highProp));
    }

    public void updateLevelLabel(int level) {
        levelLabel.setText("Level: " + level);
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
