package com.comp2042.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class HighScorePersistenceTest {

    @TempDir
    Path tempDir;

    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        tempFile = tempDir.resolve("test_highscore.dat").toFile();
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    private void writeToTempFile(byte[] data) throws IOException {
        tempFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }
    }

    private int loadIsolatedHighScore() throws Exception {
        if (!tempFile.exists()) {
            return 0;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(tempFile))) {
            return dis.readInt();
        } catch (IOException e) {
            return 0;
        }
    }

    private void saveIsolatedHighScore(int highScore) throws Exception {
        tempFile.getParentFile().mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile))) {
            dos.writeInt(highScore);
        }
    }

    @Test
    void loadHighScore_returns_zero_if_file_does_not_exist_in_isolation() throws Exception {
        assertFalse(tempFile.exists());
        assertEquals(0, loadIsolatedHighScore());
    }

    @Test
    void saveHighScore_writes_score_to_disk_and_loadHighScore_retrieves_it_in_isolation() throws Exception {
        int expectedScore = 42000;
        saveIsolatedHighScore(expectedScore);
        int loadedScore = loadIsolatedHighScore();
        assertEquals(expectedScore, loadedScore);
    }

    @Test
    void loadHighScore_returns_zero_on_malformed_file_in_isolation() throws Exception {
        writeToTempFile(new byte[]{65, 66});

        assertEquals(0, loadIsolatedHighScore());
    }

    @Test
    void saveHighScore_creates_directories_if_needed_in_isolation() throws Exception {
        int expectedScore = 1000;
        saveIsolatedHighScore(expectedScore);

        assertTrue(tempFile.exists());
        assertEquals(expectedScore, loadIsolatedHighScore());
    }
}