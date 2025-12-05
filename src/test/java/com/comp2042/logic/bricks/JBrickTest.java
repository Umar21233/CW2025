package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JBrickTest {

    private JBrick jBrick;
    private final int value = 2;

    @BeforeEach
    void setUp() {
        jBrick = new JBrick();
    }

    @Test
    void jBrick_has_four_rotation_states() {
        List<int[][]> shapes = jBrick.getShapeMatrix();
        assertEquals(4, shapes.size());
    }

    @Test
    void rotation_0_is_correct() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {value, value, value, 0},
                {0, 0, value, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = jBrick.getShapeMatrix().get(0);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_1_is_correct() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {0, value, value, 0},
                {0, value, 0, 0},
                {0, value, 0, 0}
        };
        int[][] actualShape = jBrick.getShapeMatrix().get(1);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_2_is_correct() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {0, value, 0, 0},
                {0, value, value, value},
                {0, 0, 0, 0}
        };
        int[][] actualShape = jBrick.getShapeMatrix().get(2);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_3_is_correct() {
        int[][] expectedShape = {
                {0, 0, value, 0},
                {0, 0, value, 0},
                {0, value, value, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = jBrick.getShapeMatrix().get(3);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void getShapeMatrix_returns_a_deep_copy() {
        List<int[][]> initialShapes = jBrick.getShapeMatrix();
        int[][] rotationToModify = initialShapes.get(0);
        int originalValue = rotationToModify[1][0];
        rotationToModify[1][0] = 99;

        List<int[][]> subsequentShapes = jBrick.getShapeMatrix();
        int[][] subsequentRotation = subsequentShapes.get(0);

        assertEquals(originalValue, subsequentRotation[1][0]);
        assertNotSame(initialShapes, subsequentShapes);
        assertNotSame(rotationToModify, subsequentRotation);
    }
}