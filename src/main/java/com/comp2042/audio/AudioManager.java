package com.comp2042.audio;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


/**
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

    private AudioManager() {
        soundEffects = new HashMap<>();
        loadAudio();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

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
    public void playMenuMusic() {
        stopAllMusic();
        if (musicEnabled && menuMusicPlayer != null) {
            currentMusic = MusicType.MENU;
            menuMusicPlayer.play();
        }
    }

    public void playGameMusic() {
        stopAllMusic();
        if (musicEnabled && gameMusicPlayer != null) {
            currentMusic = MusicType.GAME;
            gameMusicPlayer.play();
        }
    }

    public void stopAllMusic() {
        if (menuMusicPlayer != null) {
            menuMusicPlayer.stop();
        }
        if (gameMusicPlayer != null) {
            gameMusicPlayer.stop();
        }
        currentMusic = MusicType.NONE;
    }

    public void pauseMusic() {
        if (currentMusic == MusicType.MENU && menuMusicPlayer != null) {
            menuMusicPlayer.pause();
        } else if (currentMusic == MusicType.GAME && gameMusicPlayer != null) {
            gameMusicPlayer.pause();
        }
    }

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
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        updateMusicVolumes();
    }

    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
        updateSfxVolumes();
    }

    private void updateMusicVolumes() {
        if (menuMusicPlayer != null) {
            menuMusicPlayer.setVolume(musicVolume);
        }
        if (gameMusicPlayer != null) {
            gameMusicPlayer.setVolume(musicVolume);
        }
    }

    private void updateSfxVolumes() {
        for (MediaPlayer player : soundEffects.values()) {
            if (player != null) {
                player.setVolume(sfxVolume);
            }
        }
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    // Enable/Disable
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopAllMusic();
        } else {
            // Resume current music if any
            resumeMusic();
        }
    }

    public void setSfxEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isSfxEnabled() {
        return sfxEnabled;
    }

    // Cleanup
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