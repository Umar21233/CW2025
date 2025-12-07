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

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MatrixOperations() { }

    /**
     * Checks if a given brick would intersect with the game matrix boundaries or
     * existing blocks when placed at a specific (x, y) coordinate.
     *
     * @param matrix The game board matrix.
     * @param brick The brick's shape matrix.
     * @param x The X-coordinate (column) on the board where the brick's top-left corner would be.
     * @param y The Y-coordinate (row) on the board where the brick's top-left corner would be.
     * @return True if an intersection (collision) occurs, false otherwise.
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

    /**
     * Checks if a specific cell coordinate (x, y) is outside the bounds of the given matrix.
     *
     * @param matrix The matrix to check against.
     * @param x The X-coordinate (column) to check.
     * @param y The Y-coordinate (row) to check.
     * @return True if the coordinate is out of bounds, false otherwise.
     */
    private static boolean checkOutOfBound(int[][] matrix, int x, int y) {
        return (x < 0 || x >= matrix[0].length || y < 0 || y >= matrix.length);
    }

    /**
     * Creates and returns a deep copy of a 2D integer matrix.
     * This is used to prevent unintended modifications to original matrix data.
     *
     * @param original The 2D integer matrix to copy.
     * @return A new 2D integer array that is a deep copy of the original.
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            myInt[i] = original[i].clone();
        }
        return myInt;
    }

    /**
     * Merges a brick's shape into a given game field matrix at specified coordinates,
     * returning a new matrix with the merged result. The original field matrix remains unchanged.
     *
     * @param field The background game field matrix.
     * @param brick The brick's shape matrix to merge.
     * @param x The X-coordinate (column) on the field where the brick's top-left corner will be.
     * @param y The Y-coordinate (row) on the field where the brick's top-left corner will be.
     * @return A new 2D integer array representing the field after the brick has been merged.
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
     * Scans the given game matrix for complete rows, removes them, and shifts
     * the remaining rows down. It then calculates the score bonus for the cleared rows.
     *
     * @param matrix The 2D integer array representing the current game board.
     * @return A {@code ClearRow} object containing the number of cleared rows, the new game matrix,
     *         and the score bonus obtained.
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

    /**
     * Creates a deep copy of a list of 2D integer matrices.
     *
     * @param list The list of 2D integer matrices to copy.
     * @return A new list containing deep copies of the original matrices.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }
}
