package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZBrickTest {

    private ZBrick zBrick;
    private final int value = 7;

    @BeforeEach
    void setUp() {
        zBrick = new ZBrick();
    }

    @Test
    void zBrick_has_two_rotation_states() {
        List<int[][]> shapes = zBrick.getShapeMatrix();
        assertEquals(2, shapes.size());
    }

    @Test
    void rotation_0_is_correct() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {value, value, 0, 0},
                {0, value, value, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = zBrick.getShapeMatrix().get(0);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_1_is_correct() {
        int[][] expectedShape = {
                {0, value, 0, 0},
                {value, value, 0, 0},
                {value, 0, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = zBrick.getShapeMatrix().get(1);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void getShapeMatrix_returns_a_deep_copy() {
        List<int[][]> initialShapes = zBrick.getShapeMatrix();
        int[][] rotationToModify = initialShapes.get(0);
        int originalValue = rotationToModify[1][0];
        rotationToModify[1][0] = 99;

        List<int[][]> subsequentShapes = zBrick.getShapeMatrix();
        int[][] subsequentRotation = subsequentShapes.get(0);

        assertEquals(originalValue, subsequentRotation[1][0]);
        assertNotSame(initialShapes, subsequentShapes);
        assertNotSame(rotationToModify, subsequentRotation);
    }
}
