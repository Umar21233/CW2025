package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.model.NextShapeInfo;
import com.comp2042.model.ViewData;

import java.awt.Point;

public class PieceManager {

    private final GameBoard gameBoard;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private Point currentOffset;

    public PieceManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
    }

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

    public boolean rotateLeft() {
        NextShapeInfo nextShape = brickRotator.getNextShape();
        if (gameBoard.isCollision(currentOffset, nextShape.getShape())) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    public boolean spawnNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = getSpawnPoint();

        return gameBoard.isCollision(currentOffset, brickRotator.getCurrentShape());
    }

    private Point getSpawnPoint() {
        int spawnX = gameBoard.getWidth() / 2 - 2;
        if (spawnX < 0) spawnX = 0;
        return new Point(spawnX, 2);
    }

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

    public void mergeBrickToBackground() {
        gameBoard.mergeBrick(currentOffset, brickRotator.getCurrentShape());
    }
}
