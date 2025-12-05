package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IBrickTest {

    private IBrick iBrick;

    @BeforeEach
    void setUp() {
        iBrick = new IBrick();
    }

    @Test
    void iBrick_has_two_rotation_states() {
        List<int[][]> shapes = iBrick.getShapeMatrix();
        assertEquals(2, shapes.size());
    }

    @Test
    void rotation_0_is_horizontal_i_shape() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = iBrick.getShapeMatrix().get(0);

        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void rotation_1_is_vertical_i_shape() {
        int[][] expectedShape = {
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        };
        int[][] actualShape = iBrick.getShapeMatrix().get(1);

        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void getShapeMatrix_returns_a_deep_copy() {
        List<int[][]> initialShapes = iBrick.getShapeMatrix();
        int[][] rotationToModify = initialShapes.get(0);

        int originalValue = rotationToModify[1][0];

        rotationToModify[1][0] = 9;

        List<int[][]> subsequentShapes = iBrick.getShapeMatrix();
        int[][] subsequentRotation = subsequentShapes.get(0);

        assertEquals(originalValue, subsequentRotation[1][0]);

        assertNotSame(initialShapes, subsequentShapes);

        assertNotSame(rotationToModify, subsequentRotation);
    }
}