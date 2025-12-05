package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SBrickTest {

    private SBrick sBrick;
    private final int value = 5;

    @BeforeEach
    void setUp() {
        sBrick = new SBrick();
    }

    @Test
    void sBrick_has_two_rotation_states() {
        List<int[][]> shapes = sBrick.getShapeMatrix();
        assertEquals(2, shapes.size());
    }

    @Test
    void rotation_0_is_horizontal_s_shape() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {0, value, value, 0},
                {value, value, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = sBrick.getShapeMatrix().get(0);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_1_is_vertical_s_shape() {
        int[][] expectedShape = {
                {value, 0, 0, 0},
                {value, value, 0, 0},
                {0, value, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = sBrick.getShapeMatrix().get(1);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void getShapeMatrix_returns_a_deep_copy() {
        List<int[][]> initialShapes = sBrick.getShapeMatrix();
        int[][] rotationToModify = initialShapes.get(0);
        int originalValue = rotationToModify[1][1];
        rotationToModify[1][1] = 99;

        List<int[][]> subsequentShapes = sBrick.getShapeMatrix();
        int[][] subsequentRotation = subsequentShapes.get(0);

        assertEquals(originalValue, subsequentRotation[1][1]);
        assertNotSame(initialShapes, subsequentShapes);
        assertNotSame(rotationToModify, subsequentRotation);
    }
}