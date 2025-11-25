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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    @FXML private GridPane ghostPanel;
    @FXML private GridPane brickPanel;
    @FXML private GridPane gamePanel;
    @FXML private Canvas gridCanvas;
    @FXML private Group groupNotification;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Label scoreLabel, highLabel;
    @FXML private Pane nextPane;
    @FXML private Button btnPlay, btnPause, btnSettings;
    @FXML private Rectangle dangerLine;

    private Timeline dangerLineFlashTimeline;
    private Timeline timeLine;

    private InputEventListener eventListener;
    private PieceRenderer renderer;
    private GameBoardView gameBoardView;
    private NextPieceView nextPieceView;

    // Active Piece Rectangles (still managed by controller as they move)
    private Rectangle[][] activeRects;
    private Rectangle[][] ghostRects;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCustomFont();
        setupInputHandling();
        setupFlashingDangerLine();

        gameOverPanel.setVisible(false);

        // Bind pause button text
        btnPause.textProperty().bind(
                Bindings.when(isPause)
                        .then("Resume")
                        .otherwise("Pause")
        );

        // Apply a nice visual effect to something (kept from original)
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    private void loadCustomFont() {
        Font.loadFont(
                getClass().getClassLoader().getResource("digital.ttf").toExternalForm(),
                38
        );
    }

    private void setupInputHandling() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // Use lambda for clean input handling
        gamePanel.setOnKeyPressed(keyEvent -> {
            if (!isPause.get() && !isGameOver.get()) {
                KeyCode code = keyEvent.getCode();

                // Creating a new MoveEvent for each direction to fix setType errors
                if (code == KeyCode.LEFT || code == KeyCode.A) {
                    MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
                    refreshBrick(eventListener.onLeftEvent(event));
                    keyEvent.consume();
                } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
                    refreshBrick(eventListener.onRightEvent(event));
                    keyEvent.consume();
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
                    refreshBrick(eventListener.onRotateEvent(event));
                    keyEvent.consume();
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();
                } else if (code == KeyCode.SPACE) {
                    hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                    keyEvent.consume();
                }
            }

            if (keyEvent.getCode() == KeyCode.N) {
                newGame(null);
                keyEvent.consume();
            }
        });
    }

    private void setupFlashingDangerLine() {
        dangerLine.setTranslateY(-198); // Position adjusted as needed

        dangerLineFlashTimeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.5),
                        event -> toggleDangerLine()
                )
        );
        dangerLineFlashTimeline.setCycleCount(Timeline.INDEFINITE);
        dangerLineFlashTimeline.play();
    }

    private void toggleDangerLine() {
        dangerLine.setOpacity(dangerLine.getOpacity() == 1.0 ? 0 : 1.0);
    }

    // --- Game Initialization ---

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        // 1. Initialize Renderers and Views
        this.renderer = new PieceRenderer();
        this.gameBoardView = new GameBoardView(gamePanel, gridCanvas, renderer, boardMatrix);
        this.nextPieceView = new NextPieceView(nextPane, renderer);

        // Set up initial active piece and ghost piece containers
        initPieceContainers(brick);

        // Update the next piece preview
        nextPieceView.update(brick.getNextBrickData());

        // Start game timeline
        startGameTimeline();

        // Reset state
        isPause.set(false);
        isGameOver.set(false);
        btnPause.setDisable(false);
        dangerLine.setWidth(gridCanvas.getWidth());
    }

    private void initPieceContainers(ViewData brick) {
        // Falling brick (active piece)
        activeRects = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        brickPanel.getChildren().clear();
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameBoardView.BRICK_SIZE, GameBoardView.BRICK_SIZE);
                renderer.styleBrickRectangle(brick.getBrickData()[i][j], rectangle);
                activeRects[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        // Ghost brick
        ghostRects = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostPanel.getChildren().clear();
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle g = new Rectangle(GameBoardView.BRICK_SIZE, GameBoardView.BRICK_SIZE);
                renderer.styleGhostRectangle(brick.getBrickData()[i][j], g);
                ghostRects[i][j] = g;
                ghostPanel.add(g, j, i);
            }
        }

        updatePiecePositions(brick);
    }

    private void startGameTimeline() {
        if (timeLine != null) {
            timeLine.stop();
        }

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    // --- Refresh and Update ---

    public void refreshGameBackground(int[][] board) {
        gameBoardView.refreshGameBackground(board);
    }

    private void updatePiecePositions(ViewData brick) {
        double cellWidth = gameBoardView.getCellWidth();
        double cellHeight = gameBoardView.getCellHeight();

        // board row 2 = visible row 0
        int visibleY = brick.getyPosition() - 2;
        int visibleGhostY = brick.getGhostYPosition() - 2;

        // Active piece position
        brickPanel.setTranslateX(brick.getxPosition() * cellWidth);
        brickPanel.setTranslateY(visibleY * cellHeight);

        // Ghost piece position
        ghostPanel.setTranslateX(brick.getxPosition() * cellWidth);
        ghostPanel.setTranslateY(visibleGhostY * cellHeight);
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.get()) return;

        // 1. Update Position
        updatePiecePositions(brick);

        // 2. Update Cell Colors/Style
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                int value = brick.getBrickData()[i][j];
                renderer.styleBrickRectangle(value, activeRects[i][j]);
                renderer.styleGhostRectangle(value, ghostRects[i][j]);
            }
        }

        // 3. Update Next Piece Preview
        nextPieceView.update(brick.getNextBrickData());
    }

    // --- Game Action Methods ---

    private void showScoreNotification(int scoreBonus) {
        if (scoreBonus > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    private void moveDown(MoveEvent event) {
        if (isPause.get() || isGameOver.get()) return;

        DownData downData = eventListener.onDownEvent(event);
        if (downData.getClearRow() != null) {
            showScoreNotification(downData.getClearRow().getScoreBonus());
        }
        refreshBrick(downData.getViewData());
        gamePanel.requestFocus(); // Keep focus for user input
    }

    private void hardDrop(MoveEvent event) {
        if (isGameOver.get()) return;

        DownData downData = eventListener.onHardDrop(event);
        if (downData.getClearRow() != null) {
            showScoreNotification(downData.getClearRow().getScoreBonus());
        }
        refreshBrick(downData.getViewData());
        gamePanel.requestFocus(); // Keep focus for user input
    }

    // --- Bindings and State Changes ---

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
        if (dangerLineFlashTimeline != null) {
            dangerLineFlashTimeline.stop();
        }
        if (timeLine != null) {
            timeLine.stop();
        }
        gameOverPanel.setVisible(true);
        isGameOver.set(true);
        isPause.set(false); // Ensure game doesn't resume paused
        btnPause.setDisable(true);
    }

    public void newGame(ActionEvent actionEvent) {
        if (timeLine != null) {
            timeLine.stop();
        }
        gameOverPanel.setVisible(false);
        eventListener.createNewGame(); // This will trigger initGameView again
        gamePanel.requestFocus();
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (timeLine == null || isGameOver.get()) {
            return;
        }

        if (isPause.get()) {
            timeLine.play();
            isPause.set(false);
        } else {
            timeLine.pause();
            isPause.set(true);
        }
        gamePanel.requestFocus();
    }
}