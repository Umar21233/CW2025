package com.comp2042.ui;

import java.util.prefs.Preferences;

/**
 * Singleton class managing player statistics with persistence.
 * Tracks cumulative gameplay data across sessions.

 * Design Patterns Used:
 * - Singleton Pattern: Ensures single instance manages all player stats
 * - Persistence Pattern: Uses Java Preferences API for data persistence
 */
public class PlayerStats {

    private static PlayerStats instance;
    private final Preferences prefs;

    // Stat keys
    /** Key for storing the total number of games played. */
    private static final String TOTAL_GAMES_KEY = "totalGames";
    /** Key for storing the total points scored across all games. */
    private static final String TOTAL_POINTS_KEY = "totalPoints";
    /** Key for storing the highest score achieved in a single game. */
    private static final String HIGHEST_SCORE_KEY = "highestScore";
    /** Key for storing the highest combo (lines cleared at once) achieved. */
    private static final String HIGHEST_COMBO_KEY = "highestCombo";
    /** Key for storing the highest level reached. */
    private static final String HIGHEST_LEVEL_KEY = "highestLevel";

    // Current values
    /** The total number of games the player has played. */
    private int totalGamesPlayed;
    /** The cumulative total of all points the player has scored. */
    private long totalPointsScored;
    /** The highest score recorded in a single game. */
    private int highestScore;
    /** The highest number of lines cleared in a single combo. */
    private int highestCombo;
    /** The highest level the player has reached in any game. */
    private int highestLevel;

    /**
     * Private constructor to enforce the Singleton pattern.
     * Initializes the Preferences API node and loads existing stats.
     */
    private PlayerStats() {
        prefs = Preferences.userNodeForPackage(PlayerStats.class);
        loadStats();
    }

    /**
     * Returns the singleton instance of PlayerStats.
     * If the instance does not exist, it creates one.
     *
     * @return The single instance of PlayerStats.
     */
    public static PlayerStats getInstance() {
        if (instance == null) {
            instance = new PlayerStats();
        }
        return instance;
    }

    /**
     * Loads player statistics from the user's preferences.
     * Default values are used if no saved preferences are found.
     */
    private void loadStats() {
        totalGamesPlayed = prefs.getInt(TOTAL_GAMES_KEY, 0);
        totalPointsScored = prefs.getLong(TOTAL_POINTS_KEY, 0L);
        highestScore = prefs.getInt(HIGHEST_SCORE_KEY, 0);
        highestCombo = prefs.getInt(HIGHEST_COMBO_KEY, 0);
        highestLevel = prefs.getInt(HIGHEST_LEVEL_KEY, 1);
    }

    /**
     * Saves the current player statistics to the user's preferences.
     */
    private void saveStats() {
        prefs.putInt(TOTAL_GAMES_KEY, totalGamesPlayed);
        prefs.putLong(TOTAL_POINTS_KEY, totalPointsScored);
        prefs.putInt(HIGHEST_SCORE_KEY, highestScore);
        prefs.putInt(HIGHEST_COMBO_KEY, highestCombo);
        prefs.putInt(HIGHEST_LEVEL_KEY, highestLevel);
    }

    /**
     * Records the end of a game, updating total games played, total points scored,
     * and potentially the highest score and highest level.
     *
     * @param finalScore The score achieved in the just-completed game.
     * @param finalLevel The level reached in the just-completed game.
     */
    public void recordGameEnd(int finalScore, int finalLevel) {
        totalGamesPlayed++;
        totalPointsScored += finalScore;

        if (finalScore > highestScore) {
            highestScore = finalScore;
        }

        if (finalLevel > highestLevel) {
            highestLevel = finalLevel;
        }

        saveStats();
    }

    /**
     * Records a combo event, updating the highest combo if the current combo
     * (lines cleared) is greater than the previously recorded highest.
     *
     * @param linesCleared The number of lines cleared in the current combo.
     */
    public void recordCombo(int linesCleared) {
        if (linesCleared > highestCombo) {
            highestCombo = linesCleared;
            saveStats();
        }
    }

    // Getters
    /**
     * Returns the total number of games the player has played.
     * @return The total number of games played.
     */
    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    /**
     * Returns the cumulative total of all points the player has scored.
     * @return The total points scored.
     */
    public long getTotalPointsScored() {
        return totalPointsScored;
    }

    /**
     * Returns the highest score recorded in a single game.
     * @return The highest score.
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     * Returns the highest number of lines cleared in a single combo.
     * @return The highest combo.
     */
    public int getHighestCombo() {
        return highestCombo;
    }

    /**
     * Returns the highest level the player has reached in any game.
     * @return The highest level.
     */
    public int getHighestLevel() {
        return highestLevel;
    }

    /**
     * Resets all player statistics to their initial values (0 or 1 for level)
     * and persists these changes.
     */
    public void resetStats() {
        totalGamesPlayed = 0;
        totalPointsScored = 0L;
        highestScore = 0;
        highestCombo = 0;
        highestLevel = 1;
        saveStats();
    }
}