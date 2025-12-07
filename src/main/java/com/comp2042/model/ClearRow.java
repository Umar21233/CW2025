package com.comp2042.model;

import com.comp2042.logic.MatrixOperations;

/**
 * A data class representing the result of a row clearing operation on the game board.
 * It encapsulates information about the number of lines removed, the updated game matrix,
 * and any score bonus awarded.
 */
public final class ClearRow {

    /** The number of lines that were removed in the operation. */
    private final int linesRemoved;
    /** The state of the game matrix after the rows have been removed and shifted. */
    private final int[][] newMatrix;
    /** The score awarded for clearing these lines. */
    private final int scoreBonus;

    /**
     * Constructs a new ClearRow object with details about the row clearing operation.
     *
     * @param linesRemoved The count of lines removed.
     * @param newMatrix The updated game matrix after clearing.
     * @param scoreBonus The score awarded for the clear.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Returns the number of lines that were removed.
     *
     * @return The count of lines removed.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a deep copy of the updated game matrix after the rows have been removed and shifted.
     *
     * @return A deep copy of the new game matrix.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the score bonus awarded for clearing the rows.
     *
     * @return The score bonus.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
