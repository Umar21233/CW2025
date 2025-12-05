package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LBrickTest {

    private LBrick lBrick;
    private final int value = 3;

    @BeforeEach
    void setUp() {
        lBrick = new LBrick();
    }

    @Test
    void lBrick_has_four_rotation_states() {
        List<int[][]> shapes = lBrick.getShapeMatrix();
        assertEquals(4, shapes.size());
    }

    @Test
    void rotation_0_is_correct() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {0, value, value, value},
                {0, value, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = lBrick.getShapeMatrix().get(0);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_1_is_correct() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {0, value, value, 0},
                {0, 0, value, 0},
                {0, 0, value, 0}
        };
        int[][] actualShape = lBrick.getShapeMatrix().get(1);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_2_is_correct() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {0, 0, value, 0},
                {value, value, value, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = lBrick.getShapeMatrix().get(2);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_3_is_correct() {
        int[][] expectedShape = {
                {0, value, 0, 0},
                {0, value, 0, 0},
                {0, value, value, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = lBrick.getShapeMatrix().get(3);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void getShapeMatrix_returns_a_deep_copy() {
        List<int[][]> initialShapes = lBrick.getShapeMatrix();
        int[][] rotationToModify = initialShapes.get(0);
        int originalValue = rotationToModify[1][1];
        rotationToModify[1][1] = 99;

        List<int[][]> subsequentShapes = lBrick.getShapeMatrix();
        int[][] subsequentRotation = subsequentShapes.get(0);

        assertEquals(originalValue, subsequentRotation[1][1]);
        assertNotSame(initialShapes, subsequentShapes);
        assertNotSame(rotationToModify, subsequentRotation);
    }
}
