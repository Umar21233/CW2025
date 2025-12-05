package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OBrickTest {

    private OBrick oBrick;
    private final int value = 4;

    @BeforeEach
    void setUp() {
        oBrick = new OBrick();
    }

    @Test
    void oBrick_has_one_rotation_state() {
        List<int[][]> shapes = oBrick.getShapeMatrix();
        assertEquals(1, shapes.size());
    }

    @Test
    void rotation_0_is_correct_square_shape() {
        int[][] expectedShape = {
                {0, 0, 0, 0},
                {0, value, value, 0},
                {0, value, value, 0},
                {0, 0, 0, 0}
        };
        int[][] actualShape = oBrick.getShapeMatrix().get(0);
        assertArrayEquals(expectedShape, actualShape);
    }

    @Test
    void getShapeMatrix_returns_a_deep_copy() {
        List<int[][]> initialShapes = oBrick.getShapeMatrix();
        int[][] rotationToModify = initialShapes.get(0);
        int originalValue = rotationToModify[1][1];
        rotationToModify[1][1] = 99;

        List<int[][]> subsequentShapes = oBrick.getShapeMatrix();
        int[][] subsequentRotation = subsequentShapes.get(0);

        assertEquals(originalValue, subsequentRotation[1][1]);
        assertNotSame(initialShapes, subsequentShapes);
        assertNotSame(rotationToModify, subsequentRotation);
    }
}