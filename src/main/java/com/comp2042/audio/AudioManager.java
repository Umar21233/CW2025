package com.comp2042.audio;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


/**
 * Works through a Singleton Design Pattern
 * Centralized audio manager for the Tetris game.
 * Handles background music and sound effects with volume control.
 */
public class AudioManager {

    private static AudioManager instance;

    // Background music players
    private MediaPlayer menuMusicPlayer;
    private MediaPlayer gameMusicPlayer;

    // Sound effect players (reusable)
    private Map<SoundEffect, MediaPlayer> soundEffects;

    // Volume settings (0.0 to 1.0)
    private double musicVolume = 0.5;
    private double sfxVolume = 0.7;
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;

    // Current music state
    private MusicType currentMusic = MusicType.NONE;

    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the sound effect map and loads all audio resources.
     */
    private AudioManager() {
        soundEffects = new HashMap<>();
        loadAudio();
    }

    /**
     * Returns the singleton instance of the AudioManager.
     * If the instance does not exist, it will be created.
     *
     * @return The singleton instance of AudioManager.
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Loads all audio files (background music and sound effects) into the manager.
     * Initializes media players and sets their initial volumes.
     */
    private void loadAudio() {
        try {
            // Load background music
            menuMusicPlayer = loadMusic("menu_music.wav");
            gameMusicPlayer = loadMusic("game_music.wav");

            // Load sound effects
            soundEffects.put(SoundEffect.BUTTON_CLICK, loadSound("button_click.wav"));
            soundEffects.put(SoundEffect.PIECE_ROTATE, loadSound("piece_rotate.wav"));
            soundEffects.put(SoundEffect.PIECE_DROP, loadSound("piece_drop.wav"));
            soundEffects.put(SoundEffect.LINE_CLEAR, loadSound("line_clear.wav"));
            soundEffects.put(SoundEffect.GAME_OVER, loadSound("game_over.wav"));

            // Set initial volumes
            updateMusicVolumes();
            updateSfxVolumes();

        } catch (Exception e) {
            System.err.println("Error loading audio files: " + e.getMessage());
        }
    }

    /**
     * Loads a music file from the resources and prepares a MediaPlayer for it.
     * The music will loop indefinitely.
     *
     * @param filename The name of the music file to load (e.g., "menu_music.wav").
     * @return A configured MediaPlayer instance for the music, or null if loading fails.
     */
    private MediaPlayer loadMusic(String filename) {
        try {
            URL resource = getClass().getClassLoader().getResource("audio/" + filename);
            if (resource == null) {
                System.err.println("Audio file not found: " + filename);
                return null;
            }
            Media media = new Media(resource.toString());
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE); // Loop background music
            return player;
        } catch (Exception e) {
            System.err.println("Failed to load music: " + filename);
            return null;
        }
    }

    /**
     * Loads a sound effect file from the resources and prepares a MediaPlayer for it.
     * Sound effects are typically played once.
     *
     * @param filename The name of the sound effect file to load (e.g., "button_click.wav").
     * @return A configured MediaPlayer instance for the sound effect, or null if loading fails.
     */
    private MediaPlayer loadSound(String filename) {
        try {
            URL resource = getClass().getClassLoader().getResource("audio/" + filename);
            if (resource == null) {
                System.err.println("Audio file not found: " + filename);
                return null;
            }
            Media media = new Media(resource.toString());
            MediaPlayer player = new MediaPlayer(media);
            return player;
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + filename);
            return null;
        }
    }

    // Music Control
    /**
     * Stops any currently playing music and starts playing the menu background music.
     * Music will only play if music is enabled in the settings.
     */
    public void playMenuMusic() {
        stopAllMusic();
        if (musicEnabled && menuMusicPlayer != null) {
            currentMusic = MusicType.MENU;
            menuMusicPlayer.play();
        }
    }

    /**
     * Stops any currently playing music and starts playing the game background music.
     * Music will only play if music is enabled in the settings.
     */
    public void playGameMusic() {
        stopAllMusic();
        if (musicEnabled && gameMusicPlayer != null) {
            currentMusic = MusicType.GAME;
            gameMusicPlayer.play();
        }
    }

    /**
     * Stops all currently playing background music.
     * Resets the current music state to NONE.
     */
    public void stopAllMusic() {
        if (menuMusicPlayer != null) {
            menuMusicPlayer.stop();
        }
        if (gameMusicPlayer != null) {
            gameMusicPlayer.stop();
        }
        currentMusic = MusicType.NONE;
    }

    /**
     * Pauses the currently playing background music.
     * The music can be resumed later from where it left off.
     */
    public void pauseMusic() {
        if (currentMusic == MusicType.MENU && menuMusicPlayer != null) {
            menuMusicPlayer.pause();
        } else if (currentMusic == MusicType.GAME && gameMusicPlayer != null) {
            gameMusicPlayer.pause();
        }
    }

    /**
     * Resumes the previously paused background music, if music is enabled.
     */
    public void resumeMusic() {
        if (musicEnabled) {
            if (currentMusic == MusicType.MENU && menuMusicPlayer != null) {
                menuMusicPlayer.play();
            } else if (currentMusic == MusicType.GAME && gameMusicPlayer != null) {
                gameMusicPlayer.play();
            }
        }
    }

    // Sound Effects
    /**
     * Plays a specified sound effect.
     * The sound effect will only play if sound effects are enabled in the settings.
     * Each sound effect is reset to the beginning before playing.
     *
     * @param effect The SoundEffect enum representing the sound to play.
     */
    public void playSound(SoundEffect effect) {
        if (!sfxEnabled) return;

        MediaPlayer player = soundEffects.get(effect);
        if (player != null) {
            // Reset to beginning and play
            player.seek(Duration.ZERO);
            player.play();
        }
    }

    // Volume Control
    /**
     * Sets the global volume for all background music.
     * The volume is clamped between 0.0 and 1.0.
     *
     * @param volume The desired music volume (0.0 to 1.0).
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        updateMusicVolumes();
    }

    /**
     * Sets the global volume for all sound effects.
     * The volume is clamped between 0.0 and 1.0.
     *
     * @param volume The desired sound effects volume (0.0 to 1.0).
     */
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
        updateSfxVolumes();
    }

    /**
     * Updates the volume of all active music players based on the current musicVolume setting.
     */
    private void updateMusicVolumes() {
        if (menuMusicPlayer != null) {
            menuMusicPlayer.setVolume(musicVolume);
        }
        if (gameMusicPlayer != null) {
            gameMusicPlayer.setVolume(musicVolume);
        }
    }

    /**
     * Updates the volume of all loaded sound effect players based on the current sfxVolume setting.
     */
    private void updateSfxVolumes() {
        for (MediaPlayer player : soundEffects.values()) {
            if (player != null) {
                player.setVolume(sfxVolume);
            }
        }
    }

    /**
     * Retrieves the current global volume setting for background music.
     *
     * @return The current music volume (0.0 to 1.0).
     */
    public double getMusicVolume() {
        return musicVolume;
    }

    /**
     * Retrieves the current global volume setting for sound effects.
     *
     * @return The current sound effects volume (0.0 to 1.0).
     */
    public double getSfxVolume() {
        return sfxVolume;
    }

    // Enable/Disable
    /**
     * Enables or disables all background music.
     * If music is disabled, any playing music will be stopped.
     * If re-enabled, previously playing music will resume.
     *
     * @param enabled True to enable music, false to disable.
     */
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopAllMusic();
        } else {
            // Resume current music if any
            resumeMusic();
        }
    }

    /**
     * Enables or disables all sound effects.
     *
     * @param enabled True to enable sound effects, false to disable.
     */
    public void setSfxEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
    }

    /**
     * Checks if background music is currently enabled.
     *
     * @return True if music is enabled, false otherwise.
     */
    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    /**
     * Checks if sound effects are currently enabled.
     *
     * @return True if sound effects are enabled, false otherwise.
     */
    public boolean isSfxEnabled() {
        return sfxEnabled;
    }

    // Cleanup
    /**
     * Releases all resources held by the AudioManager, stopping all music
     * and disposing of MediaPlayer instances to prevent resource leaks.
     */
    public void dispose() {
        stopAllMusic();
        if (menuMusicPlayer != null) menuMusicPlayer.dispose();
        if (gameMusicPlayer != null) gameMusicPlayer.dispose();
        for (MediaPlayer player : soundEffects.values()) {
            if (player != null) player.dispose();
        }
        soundEffects.clear();
    }
}