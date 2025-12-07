package com.comp2042.ui;

import javafx.scene.Scene;
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
 *
 * Design Patterns Used:
 * - Observer Pattern: Uses GameSettings singleton to react to setting changes
 * - MVC Pattern: Acts as controller between model (game logic) and view (UI)
 */

public class GuiController implements Initializable {

    @FXML private GridPane ghostPanel; // GridPane for displaying the ghost piece.
    @FXML private GridPane brickPanel; // GridPane for displaying the active falling brick.
    @FXML GridPane gamePanel; // The main GridPane representing the game board.
    @FXML private Canvas gridCanvas; // Canvas for drawing the background grid lines.
    @FXML private Group groupNotification; // Group for displaying temporary notifications like score bonuses.
    @FXML private GameOverPanel gameOverPanel; // Panel displayed when the game is over.
    @FXML private Label scoreLabel, highLabel, levelLabel; // Labels for displaying score, high score, and current level.
    @FXML private Pane nextPane; // Pane for displaying the next falling brick preview.
    @FXML private Button btnPlay, btnPause, btnMainMenu; // Buttons for playing, pausing, and returning to main menu.
    @FXML private Rectangle dangerLine; // Visual indicator for the "danger zone" at the top of the board.


    /** Timeline for animating the flashing danger line at the top of the game board. */
    private Timeline dangerLineFlashTimeline;
    /** The main game timeline, controlling the automatic downward movement of bricks. */
    private Timeline timeLine;
    /** Listener for handling input events, typically the GameController. */
    private InputEventListener eventListener;
    /** The current speed of the game (interval in milliseconds for brick to move down). */
    private int currentGameSpeed = 400;
    /** The audio manager for playing sound effects and music. */
    private AudioManager audioManager;


    /** Renderer responsible for styling game pieces. */
    private PieceRenderer renderer;
    /** View component responsible for rendering the main game board background. */
    private GameBoardView gameBoardView;
    /** View component responsible for rendering the next piece preview. */
    private NextPieceView nextPieceView;


    /** 2D array of Rectangles representing the active falling brick. */
    private Rectangle[][] activeRects;
    /** 2D array of Rectangles representing the ghost piece. */
    private Rectangle[][] ghostRects;

    /** The primary stage of the application. */
    private Stage primaryStage;

    /** Singleton instance for managing game settings. */
    private GameSettings gameSettings;

    /** Singleton instance for managing player statistics. */
    private PlayerStats playerStats;
    /** Stores the final score of the last completed game. */
    private int currentFinalScore = 0;
    /** Stores the final level reached in the last completed game. */
    private int currentFinalLevel = 1;

    /** Property indicating whether the game is currently paused. */
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    /** Property indicating whether the game is currently over. */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    /**
     * Initializes the GuiController after FXML loading.
     * Sets up game settings, audio manager, font, input handling, and danger line.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameSettings = GameSettings.getInstance();
        playerStats = PlayerStats.getInstance();
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

    /**
     * Loads a custom font from the resources.
     */
    private void loadCustomFont() {
        URL fontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (fontUrl != null) {
            Font.loadFont(fontUrl.toExternalForm(), 38);
        }
    }

    /**
     * Sets up keyboard event handling for game controls.
     * Delegates input to the event listener based on key presses.
     */
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

    /**
     * Configures and starts the timeline for the flashing danger line animation.
     */
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

    /**
     * Toggles the opacity of the danger line to create a flashing effect.
     */
    private void toggleDangerLine() {
        dangerLine.setOpacity(dangerLine.getOpacity() == 1.0 ? 0 : 1.0);
    }


    /**
     * Called by the game logic to fully initialize the view for a new game.
     * Sets up view components, piece containers, and starts the game timeline.
     *
     * @param boardMatrix The initial state of the game board.
     * @param brick The initial ViewData for the first falling brick.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {

        this.renderer = new PieceRenderer();
        this.gameBoardView = new GameBoardView(gamePanel, gridCanvas, renderer, boardMatrix);
        this.nextPieceView = new NextPieceView(nextPane, renderer);

        initPieceContainers(brick);
        nextPieceView.update(brick.getNextBrickData());

        isPause.set(false);
        isGameOver.set(false);
        btnPause.setDisable(false);
        dangerLine.setWidth(gridCanvas.getWidth());

        if (dangerLineFlashTimeline != null) {
            dangerLineFlashTimeline.play();
            dangerLine.setOpacity(1.0);
        }

        // Apply theme when game view initializes
        if (gamePanel != null && gamePanel.getScene() != null) {
            ThemeManager.applyTheme(gamePanel.getScene());
        }

        startGameTimeline();
    }

    /**
     * Initializes the visual containers for the active falling brick and the ghost piece.
     * Creates and styles the rectangles for both.
     *
     * @param brick The ViewData for the current brick.
     */
    private void initPieceContainers(ViewData brick) {
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

        ghostPanel.setVisible(gameSettings.isGhostModeEnabled());

        updatePiecePositions(brick);

    }

    /**
     * Starts or restarts the main game timeline, controlling the automatic brick descent.
     * The speed is determined by {@code currentGameSpeed}.
     */
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

    /**
     * Updates the game speed and restarts the game timeline if necessary.
     *
     * @param newSpeed The new interval in milliseconds for brick movement.
     */
    public void updateGameSpeed(int newSpeed) {
        this.currentGameSpeed = newSpeed;
        if (timeLine != null && !isPause.get() && !isGameOver.get()) {
            //Restart timeline with new speed
            startGameTimeline();
        }
    }

    /**
     * Refreshes the visual background of the game board with the latest state of landed blocks.
     *
     * @param board The 2D integer array representing the game board's current state.
     */
    public void refreshGameBackground(int[][] board) {
        if (gameBoardView != null) {
            gameBoardView.refreshGameBackground(board);
        }
    }

    /**
     * Updates the on-screen positions of the active brick and ghost piece based on the provided {@code ViewData}.
     *
     * @param brick The {@code ViewData} containing the updated positions.
     */
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

    /**
     * Refreshes the visual representation of the active brick and ghost piece.
     * Updates their positions and styling based on the provided {@code ViewData}.
     *
     * @param brick The {@code ViewData} containing the brick's current state.
     */
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

        ghostPanel.setVisible(gameSettings.isGhostModeEnabled());

        if (nextPieceView != null) {
            nextPieceView.update(brick.getNextBrickData());
        }
    }

    /**
     * Displays a temporary notification on the screen for a score bonus.
     *
     * @param scoreBonus The amount of score bonus to display.
     */
    private void showScoreNotification(int scoreBonus) {
        if (scoreBonus > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    /**
     * Handles the downward movement of the current brick, whether initiated by user input or game timer.
     * Processes game logic, updates UI, plays sounds, and handles piece landing/line clearing.
     *
     * @param event The {@code MoveEvent} triggering the downward movement.
     */
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

    /**
     * Handles the instant "hard drop" of the current brick.
     * Processes game logic, updates UI, plays sounds, and handles line clearing.
     *
     * @param event The {@code MoveEvent} triggering the hard drop.
     */
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

    /**
     * Sets the input event listener for this controller.
     *
     * @param eventListener The {@code InputEventListener} to be set.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Binds a score property from the game logic to the UI's score label.
     *
     * @param scoreProp The {@code IntegerProperty} representing the current score.
     */
    public void bindScore(IntegerProperty scoreProp) {
        scoreLabel.textProperty().bind(Bindings.format("Score: %d", scoreProp));

        scoreProp.addListener((obs, oldVal, newVal) -> {
            currentFinalScore = newVal.intValue();
        });
    }

    /**
     * Binds a high score property from the game logic to the UI's high score label.
     *
     * @param highProp The {@code IntegerProperty} representing the high score.
     */
    public void bindHighScore(IntegerProperty highProp) {
        highLabel.textProperty().bind(Bindings.format("High Score: %d", highProp));
    }

    /**
     * Binds a level property from the game logic to the UI's level label.
     *
     * @param levelProp The {@code IntegerProperty} representing the current level.
     */
    public void bindLevel(IntegerProperty levelProp) {
        levelLabel.textProperty().bind(Bindings.format("LEVEL %d", levelProp));

        levelProp.addListener((obs, oldVal, newVal) -> {
            currentFinalLevel = newVal.intValue();
        });
    }

    /**
     * Handles the game over state.
     * Stops game timelines, records player statistics, displays the game over panel,
     * and updates game state flags.
     */
    public void gameOver() {
        if (dangerLineFlashTimeline != null) {
            dangerLineFlashTimeline.stop();
        }
        if (timeLine != null) {
            timeLine.stop();
        }

        audioManager.playSound(SoundEffect.GAME_OVER);

        playerStats.recordGameEnd(currentFinalScore, currentFinalLevel);

        gameOverPanel.setVisible(true);
        isGameOver.set(true);
        isPause.set(false);
        btnPause.setDisable(true);
    }

    /**
     * Records a combo event when multiple lines are cleared simultaneously.
     *
     * @param linesCleared The number of lines cleared in the combo.
     */
    public void recordCombo(int linesCleared) {
        if (linesCleared > 0) {
            playerStats.recordCombo(linesCleared);
        }
    }

    /**
     * Sets the primary stage of the application.
     *
     * @param stage The primary Stage object.
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Requests keyboard focus for the game panel to ensure input events are captured.
     */
    public void requestGameFocus() {
        gamePanel.requestFocus();
    }


    /**
     * Initiates a new game.
     * Stops existing timelines, hides game over panel, triggers model reset,
     * and restarts game timelines.
     *
     * @param actionEvent The ActionEvent that triggered this method (can be null if called programmatically).
     */
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


    /**
     * Toggles the pause state of the game.
     * Pauses/resumes game timelines and music, and updates UI elements accordingly.
     *
     * @param actionEvent The ActionEvent that triggered this method.
     */
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

        /**
         * Navigates back to the main menu.
         * Stops all game activities, saves game state, and loads the main menu FXML.
         */
        @FXML
        private void goToMainMenu() {
            audioManager.playSound(SoundEffect.BUTTON_CLICK);
    
            eventListener.onGameExit();
    
            if (timeLine != null) {
                timeLine.stop();
            }
    
            if (dangerLineFlashTimeline != null) {
                dangerLineFlashTimeline.stop();
            }
    
            if (primaryStage == null) {
                System.err.println("Error: Primary Stage not set in GuiController.");
                return;
            }
    
            try {
                URL menuUrl = getClass().getResource("/main_menu.fxml");
                if (menuUrl == null) {
                    System.err.println("FATAL ERROR: Could not find main_menu.fxml resource.");
                    return;
                }
    
                FXMLLoader loader = new FXMLLoader(menuUrl);
                Parent root = loader.load();
    
                MainMenuController mainMenuController = loader.getController();
                mainMenuController.setPrimaryStage(primaryStage);
    
                audioManager.playMenuMusic();
    
                Scene menuScene = new Scene(root, 600, 790);
                ThemeManager.applyTheme(menuScene);
                primaryStage.setScene(menuScene);
                primaryStage.setTitle("Tetris Main Menu");
    
    
            } catch (IOException e) {
                System.err.println("Error returning to main menu: Cannot load main_menu.fxml");
                e.printStackTrace();
            }
        }

}