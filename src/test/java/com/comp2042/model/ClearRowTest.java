package com.comp2042.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {

    @Test
    void constructorStoresValuesAndGettersReturnThem() {
        int[][] matrix = {
                {1, 0},
                {0, 2}
        };

        ClearRow clearRow = new ClearRow(2, matrix, 400);

        assertEquals(2, clearRow.getLinesRemoved());
        assertEquals(400, clearRow.getScoreBonus());

        int[][] returned = clearRow.getNewMatrix();

        assertArrayEquals(matrix[0], returned[0]);
        assertArrayEquals(matrix[1], returned[1]);
    }

    @Test
    void getNewMatrixReturnsDefensiveCopy() {
        int[][] original = {
                {1, 2},
                {3, 4}
        };

        ClearRow clearRow = new ClearRow(1, original, 100);

        int[][] first = clearRow.getNewMatrix();
        int[][] second = clearRow.getNewMatrix();


        assertNotSame(original, first);
        assertNotSame(first, second);


        assertArrayEquals(original[0], first[0]);
        assertArrayEquals(original[1], first[1]);


        first[0][0] = 99;


        assertEquals(1, second[0][0],
                "Modifying the returned matrix should not affect subsequent copies");
    }
}
