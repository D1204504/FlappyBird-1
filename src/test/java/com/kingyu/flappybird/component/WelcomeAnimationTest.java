package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WelcomeAnimationTest {

    private WelcomeAnimation welcomeAnimation;
    private static final int CYCLE = 30; // Flash cycle duration

    @BeforeEach
    void setUp() {
        welcomeAnimation = new WelcomeAnimation();
    }

    @Test
    void testFlashCountReset() throws Exception {
        Graphics mockGraphics = mock(Graphics.class);

        // Use reflection to access the private flashCount field
        java.lang.reflect.Field flashCountField = WelcomeAnimation.class.getDeclaredField("flashCount");
        flashCountField.setAccessible(true);

        // Simulate CYCLE * 2 calls to draw
        for (int i = 0; i < CYCLE * 2; i++) {
            welcomeAnimation.draw(mockGraphics);
        }

        // Verify that flashCount is reset to 0
        int flashCount = (int) flashCountField.get(welcomeAnimation);
        assertEquals(0, flashCount, "flashCount should reset to 0 after reaching CYCLE * 2.");
    }
}
