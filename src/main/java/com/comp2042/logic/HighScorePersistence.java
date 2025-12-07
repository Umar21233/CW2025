package com.comp2042.logic;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 - Handles persistence of high score to disk.
 - Saves high score in resources/HighScore/highscore.dat
 */
public class HighScorePersistence {

    /** The path to the high score data file within the resources folder. */
    private static final String HIGHSCORE_FILE = "HighScore/highscore.dat";

    /**
     * Determines the correct file path for the high score persistence file.
     * It attempts to locate the file within the application's resources
     * and creates the necessary directory structure if it doesn't exist.
     * As a fallback, it uses the user's home directory.
     *
     * @return A File object representing the high score persistence file.
     */
    private static File getHighScoreFile() {
        try {
            // Try to get the resource URL
            URL resourceUrl = HighScorePersistence.class.getClassLoader().getResource(HIGHSCORE_FILE);

            if (resourceUrl != null) {
                // File exists in resources, use it
                return new File(resourceUrl.toURI());
            } else {
                // File doesn't exist yet, create it in resources folder
                // This works when running from IDE
                String projectPath = System.getProperty("user.dir");
                File resourcesDir = new File(projectPath, "src/main/resources/HighScore");

                // Create directory if it doesn't exist
                if (!resourcesDir.exists()) {
                    resourcesDir.mkdirs();
                }

                return new File(resourcesDir, "highscore.dat");
            }
        } catch (URISyntaxException e) {
            System.err.println("Error finding high score file: " + e.getMessage());
            // Fallback to user home directory
            return new File(System.getProperty("user.home"), ".tetris_highscore.dat");
        }
    }

    /**
     * Loads the high score from the persistence file.
     *
     * @return The loaded high score, or 0 if the file does not exist or an error occurs during reading.
     */
    public static int loadHighScore() {
        File file = getHighScoreFile();

        if (!file.exists()) {
            System.out.println("No high score file found, starting with 0");
            return 0;
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            int highScore = dis.readInt();
            System.out.println("Loaded high score: " + highScore);
            return highScore;
        } catch (IOException e) {
            System.err.println("Could not load high score: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Saves the provided high score to the persistence file.
     * Creates parent directories if they do not exist.
     *
     * @param highScore The integer value of the high score to save.
     */
    public static void saveHighScore(int highScore) {
        File file = getHighScoreFile();

        // Ensure parent directory exists
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            dos.writeInt(highScore);
            System.out.println("Saved high score: " + highScore + " to " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }
}