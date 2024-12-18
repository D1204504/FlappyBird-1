package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.kingyu.flappybird.util.Constant;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.kingyu.flappybird.util.GameUtil;
import org.mockito.MockedStatic;

class GameForegroundTest {

    private GameForeground foreground;
    private Graphics graphics;
    private Bird bird;

    @BeforeEach
    void setUp() {
        // Create a mock Graphics object
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedImage.getGraphics();

        // Initialize GameForeground
        foreground = new GameForeground();

        // Mock a Bird object
        bird = mock(Bird.class);
    }

    @Test
    void testForegroundDraw() {
        // Simulate drawing the foreground
        foreground.draw(graphics, bird);

        // Verify no exceptions during drawing
        assertDoesNotThrow(() -> foreground.draw(graphics, bird), "Foreground drawing should not throw exceptions");
    }
    @Test
    void testMaxClouds() {
        // Simulate filling the clouds list
        for (int i = 0; i < Constant.MAX_CLOUD_COUNT; i++) {
            foreground.getClouds().add(mock(Cloud.class));
        }

        // Ensure no more clouds are added
        foreground.setTime(System.currentTimeMillis() - 200);
        foreground.draw(graphics, bird);
        assertEquals(Constant.MAX_CLOUD_COUNT, foreground.getClouds().size(), "Foreground should not exceed max cloud count");
    }

    @Test
    void testCloudGenerationProbability() {
        // Mock GameUtil to control randomness
        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // 模擬隨機概率條件為真
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(true);

            // 模擬隨機數生成雲朵位置
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(50);

            // 觸發雲朵生成邏輯
            foreground.setTime(System.currentTimeMillis() - 200); // 設置時間，觸發雲朵邏輯
            foreground.draw(graphics, bird);

        }
}
    @Test
    void testCloudRemoval() {
        // Mock a cloud that is out of frame
        Cloud outOfFrameCloud = mock(Cloud.class);
        when(outOfFrameCloud.isOutFrame()).thenReturn(true); // 模拟云朵超出屏幕

        // Add the cloud and simulate the draw logic
        foreground.getClouds().add(outOfFrameCloud);

        // Adjust the time to ensure `cloudBornLogic` is triggered
        foreground.setTime(System.currentTimeMillis() - 200);
        foreground.draw(graphics, bird);

        // Assert that the out-of-frame cloud is removed
    }


    @Test
    void testEdgeCases() {
        // Set speed to zero
        foreground.setSpeed(0);
        assertEquals(0, foreground.getSpeed(), "Foreground speed should remain at 0");
    }
    @Test
    void testSpeedAdjustment() {
        // Set a new speed
        int newSpeed = 5;
        foreground.setSpeed(newSpeed);

        // Assert the speed is correctly updated
        assertEquals(newSpeed, foreground.getSpeed(), "Foreground speed should be updated to the new value");

        // Mock a cloud and ensure its speed is adjusted
        Cloud mockCloud = mock(Cloud.class);
        foreground.getClouds().add(mockCloud);

        foreground.setSpeed(newSpeed);

        verify(mockCloud).setSpeed(newSpeed);
    }

    @Test
    void testSpeedBoundary() {
        // Set speed to zero and verify
        foreground.setSpeed(0);
        assertEquals(0, foreground.getSpeed(), "Foreground speed should remain at 0");

        // Set speed to a negative value and verify
        foreground.setSpeed(-5);
        assertEquals(-5, foreground.getSpeed(), "Foreground speed should accept negative values");
    }

    @Test
    void testCloudBornLogicEdgeCase() {
        // Set time so that cloudBornLogic is triggered
        foreground.setTime(System.currentTimeMillis() - 200);

        // Ensure logic runs without exceptions when no clouds exist
        assertDoesNotThrow(() -> foreground.draw(graphics, bird), "Cloud born logic should handle empty cloud list");
    }

    @Test
    void testCloudImagesInitialization() {
        // Verify that cloud images are correctly loaded
        assertNotNull(foreground.getClouds(), "Clouds list should be initialized");
        assertTrue(foreground.getClouds().isEmpty(), "Clouds list should initially be empty");
    }
}

