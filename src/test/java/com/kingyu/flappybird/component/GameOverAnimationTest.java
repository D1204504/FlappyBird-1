package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameOverAnimationTest {

    private GameOverAnimation animation;
    private Graphics graphics;
    private Bird bird;

    @BeforeEach
    void setUp() {
        animation = new GameOverAnimation();

        // Create a mock Graphics object
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedImage.getGraphics();

        // Create a mock Bird object
        bird = mock(Bird.class);
        when(bird.getCurrentScore()).thenReturn(100L);
        when(bird.getBestScore()).thenReturn(200L);
    }
    @Test
    void testAnimationDrawing() {
        // Simulate drawing multiple times
        for (int i = 0; i < 30; i++) {
            animation.draw(graphics, bird);
        }

        // Animation should not yet be finished in the middle of flashing
        assertFalse(animation.isAnimationFinished(), "動畫應該在中間未完成");

        // Simulate enough calls to complete the animation
        for (int i = 0; i < 30; i++) {
            animation.draw(graphics, bird);
        }

        // After completing one full cycle, the animation should reset and be finished
        assertTrue(animation.isAnimationFinished(), "動畫應該完成");
    }

    @Test
    void testScoresDisplayed() {
        // Simulate drawing
        animation.draw(graphics, bird);

        // Verify the scores are fetched from the Bird object
        verify(bird, atLeastOnce()).getCurrentScore();
        verify(bird, atLeastOnce()).getBestScore();

    }
}
