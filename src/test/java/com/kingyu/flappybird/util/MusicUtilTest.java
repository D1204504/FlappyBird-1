package com.kingyu.flappybird.util;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.Clip;
class MusicUtilTest {

    @Mock
    private Clip mockClip;

    @Mock
    private AudioInputStream mockAudioStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 初始化 mock 物件
    }

    @Test
    void testPlayCrash() throws Exception {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class)) {
            mockedAudioSystem.when(() -> AudioSystem.getClip()).thenReturn(mockClip);
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(File.class))).thenReturn(mockAudioStream);

            MusicUtil.playCrash();
            verify(mockClip, times(1)).start();
        }
    }

    @Test
    void testPlayScore() throws Exception {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class)) {
            mockedAudioSystem.when(() -> AudioSystem.getClip()).thenReturn(mockClip);
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(any(File.class))).thenReturn(mockAudioStream);
            MusicUtil.playScore();
            verify(mockClip, times(1)).start();
        }
    }
    @Test
    void testPlaySoundLineUnavailableException() throws Exception {
        try (MockedStatic<AudioSystem> mockedAudioSystem = mockStatic(AudioSystem.class)) {
            mockedAudioSystem.when(() -> AudioSystem.getClip()).thenThrow(new LineUnavailableException("Line unavailable"));

            assertNull(MusicUtil.playSound("resources/wav/fly.wav"), "playSound should return null when the line is unavailable");
        }
    }
}

