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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.comp2042.audio.AudioManager;
import com.comp2042.audio.SoundEffect;

/**
 * Controller class managing high-level UI interaction, state, and delegating
 * rendering responsibilities to dedicated view components (GameBoardView, NextPieceView).
 */
public class GuiController implements Initializable {

    @FXML private GridPane ghostPanel;
    @FXML private GridPane brickPanel;
    @FXML
    GridPane gamePanel;
    @FXML private Canvas gridCanvas;
    @FXML private Group groupNotification;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Label scoreLabel, highLabel, levelLabel;
    @FXML private Pane nextPane;
    @FXML private Button btnPlay, btnPause, btnMainMenu;
    @FXML private Rectangle dangerLine;


    private Timeline dangerLineFlashTimeline;
    private Timeline timeLine;
    private InputEventListener eventListener;
    private int currentGameSpeed = 400;
    private AudioManager audioManager;


    private PieceRenderer renderer;
    private GameBoardView gameBoardView;
    private NextPieceView nextPieceView;


    private Rectangle[][] activeRects;
    private Rectangle[][] ghostRects;

    private Stage primaryStage;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        audioManager = AudioManager.getInstance();
        loadCustomFont();
        setupInputHandling();
        setupFlashingDangerLine();

        gameOverPanel.setVisible(false);

        //Bind pause button text to the state property
        btnPause.textProperty().bind(
                Bindings.when(isPause)
                        .then("Resume")
                        .otherwise("Pause")
        );

        //Apply a visual effect
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

        //Use lambda and fix for immutable MoveEvent (creating new event objects)
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
                    audioManager.playSound(SoundEffect.PIECE_ROTATE);
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


    /**
     * Called by the game logic to fully initialize the view for a new game.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {

        //Initialize Renderers and Views, ensuring old components are cleared
        this.renderer = new PieceRenderer();
        this.gameBoardView = new GameBoardView(gamePanel, gridCanvas, renderer, boardMatrix);
        this.nextPieceView = new NextPieceView(nextPane, renderer);

        //Set up initial active piece and ghost piece containers
        initPieceContainers(brick);

        //Update the next piece preview
        nextPieceView.update(brick.getNextBrickData());

        //Reset state and danger line
        isPause.set(false);
        isGameOver.set(false);
        btnPause.setDisable(false);
        dangerLine.setWidth(gridCanvas.getWidth());


        if (dangerLineFlashTimeline != null) {
            dangerLineFlashTimeline.play();
            dangerLine.setOpacity(1.0); // Ensure it starts visible
        }

        startGameTimeline();

    }

    private void initPieceContainers(ViewData brick) {
        //Clear previous active brick and ghost rectangles
        brickPanel.getChildren().clear();
        ghostPanel.getChildren().clear();

        //Falling brick (active piece)
        activeRects = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameBoardView.BRICK_SIZE, GameBoardView.BRICK_SIZE);
                renderer.styleBrickRectangle(brick.getBrickData()[i][j], rectangle);
                activeRects[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        //Ghost brick
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
                Duration.millis(currentGameSpeed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void updateGameSpeed(int newSpeed) {
        this.currentGameSpeed = newSpeed;
        if (timeLine != null && !isPause.get() && !isGameOver.get()) {
            //Restart timeline with new speed
            startGameTimeline();
        }
    }

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
        ghostPanel.setTranslateY((visibleGhostY * cellHeight)-1.5); //hardcoded to fix ghostpiece overlapping with border
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
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            audioManager.playSound(SoundEffect.LINE_CLEAR);
            showScoreNotification(downData.getClearRow().getScoreBonus());
        } else if (downData.isPieceLanded()) { // Play drop sound only if piece landed and no lines cleared
            audioManager.playSound(SoundEffect.PIECE_DROP);
        }
        refreshBrick(downData.getViewData());
        gamePanel.requestFocus();
    }

    private void hardDrop(MoveEvent event) {
        if (isGameOver.get()) return;

        DownData downData = eventListener.onHardDrop(event);
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            audioManager.playSound(SoundEffect.LINE_CLEAR);
            showScoreNotification(downData.getClearRow().getScoreBonus());
        } else {
            audioManager.playSound(SoundEffect.PIECE_DROP);
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

    public void bindLevel(IntegerProperty levelProp) {
        levelLabel.textProperty().bind(Bindings.format("LEVEL %d", levelProp));
    }

    public void gameOver() {
        if (dangerLineFlashTimeline != null) {
            dangerLineFlashTimeline.stop();
        }
        if (timeLine != null) {
            timeLine.stop();
        }

        audioManager.playSound(SoundEffect.GAME_OVER);

        gameOverPanel.setVisible(true);
        isGameOver.set(true);
        isPause.set(false);
        btnPause.setDisable(true);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void requestGameFocus() {
        gamePanel.requestFocus();
    }


    public void newGame(ActionEvent actionEvent) {
        audioManager.playSound(SoundEffect.BUTTON_CLICK);

        // Stop any currently running timeline
        if (timeLine != null) {
            timeLine.stop();
        }

        gameOverPanel.setVisible(false);

        // This triggers the model reset and callback to initGameView
        eventListener.createNewGame();

        isPause.set(false);
        isGameOver.set(false);
        btnPause.setDisable(false);

        if (dangerLineFlashTimeline != null) {
            dangerLineFlashTimeline.play();
            dangerLine.setOpacity(1.0);
        }

        startGameTimeline();

        gamePanel.requestFocus();
    }


    public void pauseGame(ActionEvent actionEvent) {
        if (timeLine == null || isGameOver.get()) {
            return;
        }

        audioManager.playSound(SoundEffect.BUTTON_CLICK);

        if (isPause.get()) {
            timeLine.play();
            audioManager.resumeMusic();
            isPause.set(false);
        } else {
            timeLine.pause();
            audioManager.pauseMusic();
            isPause.set(true);
        }
        gamePanel.requestFocus();
    }

    @FXML
    private void goToMainMenu() {
        audioManager.playSound(SoundEffect.BUTTON_CLICK);

        eventListener.onGameExit();

        // 1. Stop the game loop
        if (timeLine != null) {
            timeLine.stop();
        }

        // 2. Stop the danger line flashing
        if (dangerLineFlashTimeline != null) {
            dangerLineFlashTimeline.stop();
        }

        // 3. Ensure Stage exists before switching
        if (primaryStage == null) {
            System.err.println("Error: Primary Stage not set in GuiController.");
            return;
        }

        try {
            // 4. Load the Main Menu FXML (using the resource root path)
            URL menuUrl = getClass().getResource("/main_menu.fxml");
            if (menuUrl == null) {
                System.err.println("FATAL ERROR: Could not find main_menu.fxml resource.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(menuUrl);
            Parent root = loader.load();

            // 5. Get the MainMenuController to hand control (and the Stage) back
            MainMenuController mainMenuController = loader.getController();
            mainMenuController.setPrimaryStage(primaryStage);

            audioManager.playMenuMusic();

            // 6. Switch the Scene
            Scene menuScene = new Scene(root, 600, 790); //use original menu dimensions
            primaryStage.setScene(menuScene);
            primaryStage.setTitle("Tetris Main Menu");


        } catch (IOException e) {
            System.err.println("Error returning to main menu: Cannot load main_menu.fxml");
            e.printStackTrace();
        }
    }
}