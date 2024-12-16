package com.kingyu.flappybird.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Music Utility Class
 *
 * Handles audio playback for WAV files.
 * Note: For MP3 support, you would need a third-party library like JLayer.
 */
public class MusicUtil {

    private static Clip flyClip;
    private static Clip crashClip;
    private static Clip scoreClip;

    // Play fly sound
    public static void playFly() {
        flyClip = playSound("resources/wav/fly.wav");
    }

    // Play crash sound
    public static void playCrash() {
        crashClip = playSound("resources/wav/crash.wav");
    }

    // Play score sound
    public static void playScore() {
        scoreClip = playSound("resources/wav/score.wav");
    }

    // Generic method to play a sound
    public static Clip playSound(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }
}
