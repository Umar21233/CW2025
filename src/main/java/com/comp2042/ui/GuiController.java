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

/**
 * Controller class managing high-level UI interaction, state, and delegating
 * rendering responsibilities to dedicated view components (GameBoardView, NextPieceView).
 */
public class GuiController implements Initializable {

    // FXML Injections (View Components)
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

    // Timelines and Logic Links
    private Timeline dangerLineFlashTimeline;
    private Timeline timeLine;
    private InputEventListener eventListener;

    // View Component Instances
    private PieceRenderer renderer;
    private GameBoardView gameBoardView;
    private NextPieceView nextPieceView;

    // Dynamic Piece Management
    private Rectangle[][] activeRects;
    private Rectangle[][] ghostRects;

    // State Properties
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCustomFont();
        setupInputHandling();
        setupFlashingDangerLine();

        gameOverPanel.setVisible(false);

        // Bind pause button text to the state property
        btnPause.textProperty().bind(
                Bindings.when(isPause)
                        .then("Resume")
                        .otherwise("Pause")
        );

        // Apply a visual effect
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    private void loadCustomFont() {
        URL fontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (fontUrl != null) {
            Font.loadFont(fontUrl.toExternalForm(), 38);
        }
    }

    private void setupInputHandling() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // Use lambda and fix for immutable MoveEvent (creating new event objects)
        gamePanel.setOnKeyPressed(keyEvent -> {
            if (!isPause.get() && !isGameOver.get()) {
                KeyCode code = keyEvent.getCode();

                if (code == KeyCode.LEFT || code == KeyCode.A) {
                    refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                    keyEvent.consume();
                } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    keyEvent.consume();
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
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
        dangerLine.setTranslateY(-198);

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

    // --- Game Initialization (Core of the "Play Again" logic) ---

    /**
     * Called by the game logic to fully initialize the view for a new game.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {

        // 1. Initialize Renderers and Views, ensuring old components are cleared
        this.renderer = new PieceRenderer();
        this.gameBoardView = new GameBoardView(gamePanel, gridCanvas, renderer, boardMatrix);
        this.nextPieceView = new NextPieceView(nextPane, renderer);

        // 2. Set up initial active piece and ghost piece containers
        initPieceContainers(brick);

        // 3. Update the next piece preview
        nextPieceView.update(brick.getNextBrickData());

        // 4. Reset state and danger line
        isPause.set(false);
        isGameOver.set(false);
        btnPause.setDisable(false);
        dangerLine.setWidth(gridCanvas.getWidth());

        // FIX: Restart danger line flashing (it was stopped in gameOver())
        if (dangerLineFlashTimeline != null) {
            dangerLineFlashTimeline.play();
            dangerLine.setOpacity(1.0); // Ensure it starts visible
        }

        startGameTimeline();

    }

    private void initPieceContainers(ViewData brick) {
        // Clear previous active brick and ghost rectangles
        brickPanel.getChildren().clear();
        ghostPanel.getChildren().clear();

        // Falling brick (active piece)
        activeRects = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
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
        if (gameBoardView != null) {
            gameBoardView.refreshGameBackground(board);
        }
    }

    private void updatePiecePositions(ViewData brick) {
        if (gameBoardView == null) return;

        double cellWidth = gameBoardView.getCellWidth();
        double cellHeight = gameBoardView.getCellHeight();

        // board row 2 = visible row 0
        int visibleY = brick.getyPosition() - 2;
        int visibleGhostY = brick.getGhostYPosition() - 2;

        brickPanel.setTranslateX(brick.getxPosition() * cellWidth);
        brickPanel.setTranslateY(visibleY * cellHeight);

        ghostPanel.setTranslateX(brick.getxPosition() * cellWidth);
        ghostPanel.setTranslateY(visibleGhostY * cellHeight);
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.get() || gameBoardView == null) return;

        updatePiecePositions(brick);

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                int value = brick.getBrickData()[i][j];
                renderer.styleBrickRectangle(value, activeRects[i][j]);
                renderer.styleGhostRectangle(value, ghostRects[i][j]);
            }
        }

        if (nextPieceView != null) {
            nextPieceView.update(brick.getNextBrickData());
        }
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
        gamePanel.requestFocus();
    }

    private void hardDrop(MoveEvent event) {
        if (isGameOver.get()) return;

        DownData downData = eventListener.onHardDrop(event);
        if (downData.getClearRow() != null) {
            showScoreNotification(downData.getClearRow().getScoreBonus());
        }
        refreshBrick(downData.getViewData());
        gamePanel.requestFocus();
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
        isPause.set(false);
        btnPause.setDisable(true);
    }

    public void newGame(ActionEvent actionEvent) {
        // Stop any currently running timeline
        if (timeLine != null) {
            timeLine.stop();
        }

        gameOverPanel.setVisible(false);

        // This triggers the model reset and callback to initGameView
        eventListener.createNewGame();

        // FIX: Explicitly recreate and start the game loop here,
        // ensuring the timeline is playing regardless of the callback timing.
        startGameTimeline();

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