package com.comp2042.logic;

import com.comp2042.model.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 - Utility class containing static operations for matrix copying,
   collision detection, brick merging, and row clearing.
 - Stateless and purely functional to support the game logic layer.
 */

public class MatrixOperations {

    private MatrixOperations() { }

    /**
     - Checks whether placing a brick at (x, y) would collide
       with either the board boundary or existing blocks.
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {

        //check each cell of the brick
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {

                //skip empty cells
                if (brick[row][col] == 0)
                    continue;

                int targetX = x + col;  // col = X
                int targetY = y + row;  // row = Y

                //Out of bounds OR collision with occupied matrix cell
                if (checkOutOfBound(matrix, targetX, targetY)
                        || matrix[targetY][targetX] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean checkOutOfBound(int[][] matrix, int x, int y) {
        return (x < 0 || x >= matrix[0].length || y < 0 || y >= matrix.length);
    }

    /**
     - Returns a deep copy of a 2D integer matrix.
     - Used for defensive copying throughout the board logic.
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            myInt[i] = original[i].clone();
        }
        return myInt;
    }

    /**
     - Produces a new matrix representing the board after merging
       the falling brick into the background grid.
     */
    public static int[][] merge(int[][] field, int[][] brick, int x, int y) {
        int[][] copy = copy(field);

        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {

                if (brick[row][col] == 0)
                    continue;

                int targetX = x + col;
                int targetY = y + row;

                copy[targetY][targetX] = brick[row][col];
            }
        }

        return copy;
    }

    /**
     - Scans the matrix for full rows, removes them, and returns
       the updated matrix along with score bonus and removed count.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            boolean full = true;
            int[] row = matrix[i].clone();

            for (int v : row) {
                if (v == 0) full = false;
            }

            if (full) clearedRows.add(i);
            else newRows.add(row);
        }

        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] r = newRows.pollLast();
            if (r != null) tmp[i] = r;
            else tmp[i] = new int[matrix[0].length];
        }

        int count = clearedRows.size();
        int scoreBonus = 50 * count * count;

        return new ClearRow(count, tmp, scoreBonus);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}
