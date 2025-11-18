package com.comp2042;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {

    private MatrixOperations() { }

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {

        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {

                if (brick[row][col] == 0)
                    continue;

                int targetX = x + col;  // col = X
                int targetY = y + row;  // row = Y

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

    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            myInt[i] = original[i].clone();
        }
        return myInt;
    }

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
