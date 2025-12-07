package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.model.NextShapeInfo;
import com.comp2042.model.ViewData;

import java.awt.Point;

/**
 * Manages the active falling brick, including its movement, rotation,
 * spawning of new bricks, and interaction with the game board.
 * It acts as an intermediary between the game logic and individual brick operations.
 */
public class PieceManager {

    private final GameBoard gameBoard;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private Point currentOffset;

    /**
     * Constructs a new PieceManager, associating it with a specific GameBoard.
     * Initializes the brick generator and rotator.
     *
     * @param gameBoard The GameBoard instance this PieceManager will operate on.
     */
    public PieceManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
    }

    /**
     * Attempts to move the active brick down by one unit.
     * Checks for collisions with the game board or other blocks.
     *
     * @return True if the brick successfully moved down, false if a collision prevented the move.
     */
    public boolean moveDown() {
        Point p = new Point(currentOffset);
        p.translate(0, 1);

        if (gameBoard.isCollision(p, brickRotator.getCurrentShape())) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the active brick left by one unit.
     * Checks for collisions with the game board or other blocks.
     *
     * @return True if the brick successfully moved left, false if a collision prevented the move.
     */
    public boolean moveLeft() {
        Point p = new Point(currentOffset);
        p.translate(-1, 0);

        if (gameBoard.isCollision(p, brickRotator.getCurrentShape())) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the active brick right by one unit.
     * Checks for collisions with the game board or other blocks.
     *
     * @return True if the brick successfully moved right, false if a collision prevented the move.
     */
    public boolean moveRight() {
        Point p = new Point(currentOffset);
        p.translate(1, 0);

        if (gameBoard.isCollision(p, brickRotator.getCurrentShape())) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to rotate the active brick to the left.
     * Checks for collisions after rotation.
     *
     * @return True if the brick successfully rotated, false if a collision prevented the rotation.
     */
    public boolean rotateLeft() {
        NextShapeInfo nextShape = brickRotator.getNextShape();
        if (gameBoard.isCollision(currentOffset, nextShape.getShape())) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /**
     * Spawns a new brick at the designated spawn point.
     * Sets the new brick as the active brick and checks for immediate collision,
     * which would indicate a game over state.
     *
     * @return True if the newly spawned brick immediately collides, indicating game over.
     */
    public boolean spawnNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = getSpawnPoint();

        return gameBoard.isCollision(currentOffset, brickRotator.getCurrentShape());
    }

    /**
     * Calculates the initial spawn point for a new brick.
     *
     * @return A {@code Point} representing the top-left corner of the brick's spawn location.
     */
    private Point getSpawnPoint() {
        int spawnX = gameBoard.getWidth() / 2 - 2;
        if (spawnX < 0) spawnX = 0;
        return new Point(spawnX, 2);
    }

    /**
     * Gathers and returns all necessary data for rendering the game view.
     * This includes the current brick's shape and position, the next brick's shape,
     * and the ghost piece's position.
     *
     * @return A {@code ViewData} object containing comprehensive information for display.
     */
    public ViewData getViewData() {
        int ghostY = computeGhostY();
        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                ghostY
        );
    }

    /**
     * Computes the Y-coordinate of the ghost piece (projected landing position of the current brick).
     *
     * @return The Y-coordinate for the ghost piece.
     */
    private int computeGhostY() {
        return GhostCalculator.computeGhostY(
                gameBoard.getGameMatrix(),
                brickRotator.getBrick(),
                currentOffset.x,
                currentOffset.y,
                gameBoard.getHeight(),
                brickRotator.getCurrentShapeIndex()
        );
    }

    /**
     * Merges the current active brick into the game board's background.
     * This is typically called when the brick has landed and can no longer move.
     */
    public void mergeBrickToBackground() {
        gameBoard.mergeBrick(currentOffset, brickRotator.getCurrentShape());
    }
}
