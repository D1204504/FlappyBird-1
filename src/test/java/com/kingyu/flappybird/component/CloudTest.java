package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CloudTest {

    private Cloud cloud;
    private Graphics graphics;

    @BeforeEach
    void setUp() {
        // Create a mock Graphics object
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedImage.getGraphics();

        // Initialize a Cloud object
        cloud = new Cloud(mock(BufferedImage.class), 100, 50);
    }

    @Test
    void testCloudDraw() {
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);

        int initialX = cloud.getX();

        // Simulate drawing the cloud
        cloud.draw(graphics, bird);

        // Verify that the x-coordinate decreases by the cloud's speed
        assertTrue(cloud.getX() < initialX, "Cloud X-coordinate should decrease after drawing");
    }


    @Test
    void testCloudMovement() {
        Bird bird = mock(Bird.class);

        // Simulate movement
        cloud.draw(graphics, bird);
        assertTrue(cloud.getX() < 100, "Cloud X-coordinate should decrease after movement");
    }

    @Test
    void testCloudOutOfFrame() {
        cloud.setAttribute(-50, 50); // Set cloud position outside the frame
        assertTrue(cloud.isOutFrame(), "Cloud should be marked as out of frame when off-screen");
    }
}
