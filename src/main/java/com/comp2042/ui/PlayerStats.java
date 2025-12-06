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
    private static final String TOTAL_GAMES_KEY = "totalGames";
    private static final String TOTAL_POINTS_KEY = "totalPoints";
    private static final String HIGHEST_SCORE_KEY = "highestScore";
    private static final String HIGHEST_COMBO_KEY = "highestCombo";
    private static final String HIGHEST_LEVEL_KEY = "highestLevel";

    // Current values
    private int totalGamesPlayed;
    private long totalPointsScored;
    private int highestScore;
    private int highestCombo;
    private int highestLevel;

    private PlayerStats() {
        prefs = Preferences.userNodeForPackage(PlayerStats.class);
        loadStats();
    }

    public static PlayerStats getInstance() {
        if (instance == null) {
            instance = new PlayerStats();
        }
        return instance;
    }

    private void loadStats() {
        totalGamesPlayed = prefs.getInt(TOTAL_GAMES_KEY, 0);
        totalPointsScored = prefs.getLong(TOTAL_POINTS_KEY, 0L);
        highestScore = prefs.getInt(HIGHEST_SCORE_KEY, 0);
        highestCombo = prefs.getInt(HIGHEST_COMBO_KEY, 0);
        highestLevel = prefs.getInt(HIGHEST_LEVEL_KEY, 1);
    }

    private void saveStats() {
        prefs.putInt(TOTAL_GAMES_KEY, totalGamesPlayed);
        prefs.putLong(TOTAL_POINTS_KEY, totalPointsScored);
        prefs.putInt(HIGHEST_SCORE_KEY, highestScore);
        prefs.putInt(HIGHEST_COMBO_KEY, highestCombo);
        prefs.putInt(HIGHEST_LEVEL_KEY, highestLevel);
    }

    // Called when a game ends
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

    // Called when lines are cleared
    public void recordCombo(int linesCleared) {
        if (linesCleared > highestCombo) {
            highestCombo = linesCleared;
            saveStats();
        }
    }

    // Getters
    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public long getTotalPointsScored() {
        return totalPointsScored;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getHighestCombo() {
        return highestCombo;
    }

    public int getHighestLevel() {
        return highestLevel;
    }

    // Reset all stats
    public void resetStats() {
        totalGamesPlayed = 0;
        totalPointsScored = 0L;
        highestScore = 0;
        highestCombo = 0;
        highestLevel = 1;
        saveStats();
    }
}