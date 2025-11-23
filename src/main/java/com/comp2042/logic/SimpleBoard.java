package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.model.ClearRow;
import com.comp2042.model.NextShapeInfo;
import com.comp2042.model.ViewData;

import java.awt.*;

/**
 - Core game logic and authoritative game state.
 - Handles movement, collision detection, line clearing, ghost piece,
 brick spawning, and score updates. Does not perform any UI work.
 */

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    public SimpleBoard(int width, int height) {
        //width  = number of columns (X)
        //height = number of rows    (Y)
        this.width = width;
        this.height = height;

        //matrix[rows][cols] = [height][width]
        currentGameMatrix = new int[height][width];

        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                brickRotator.getCurrentShape(),
                (int) p.getX(),
                (int) p.getY()
        );
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                brickRotator.getCurrentShape(),
                (int) p.getX(),
                (int) p.getY()
        );
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                brickRotator.getCurrentShape(),
                (int) p.getX(),
                (int) p.getY()
        );
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(
                currentMatrix,
                nextShape.getShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    //SRP
    private Point getSpawnPoint() {
        int spawnX = width / 2 - 2;
        if (spawnX < 0) spawnX = 0;
        return new Point(spawnX, 2);
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);

        currentOffset = getSpawnPoint();

        // If new brick immediately collides, that means GAME OVER
        return MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                currentOffset.x,
                currentOffset.y
        );
    }




    @Override
    public int[][] getBoardMatrix() {
        return MatrixOperations.copy(currentGameMatrix);  //protects game state from ui modifying it
    }


    /**
     - Computes how far the active brick can drop before collision.
     - Used only for ghost piece rendering.
     */
    private int computeGhostY() {
        return GhostCalculator.computeGhostY(
                currentGameMatrix,
                brickRotator.getBrick(),
                currentOffset.x,
                currentOffset.y,
                height,
                brickRotator.getCurrentShapeIndex()
        );
    }


    @Override
    public ViewData getViewData() {
        int ghostY = computeGhostY();  // <- call helper

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                ghostY
        );
    }


    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}
