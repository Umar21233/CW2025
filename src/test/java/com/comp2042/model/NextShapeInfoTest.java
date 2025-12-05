package com.comp2042.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NextShapeInfoTest {

    @Test
    void constructorStoresPositionAndShapeContents() {
        int[][] shape = {
                {1, 0},
                {0, 1}
        };

        NextShapeInfo info = new NextShapeInfo(shape, 2);

        assertEquals(2, info.getPosition());

        int[][] returned = info.getShape();

        assertArrayEquals(shape[0], returned[0]);
        assertArrayEquals(shape[1], returned[1]);
    }

    @Test
    void getShapeReturnsDefensiveCopy() {
        int[][] shape = {
                {1, 2},
                {3, 4}
        };

        NextShapeInfo info = new NextShapeInfo(shape, 1);

        int[][] first = info.getShape();
        int[][] second = info.getShape();

        assertNotSame(first, second, "Each call to getShape should return a new array instance");

        assertArrayEquals(first[0], second[0]);
        assertArrayEquals(first[1], second[1]);

        first[0][0] = 99;

        assertEquals(1, second[0][0],
                "Modifying one returned shape array must not affect subsequent copies");
    }
}
