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
    private BufferedImage bufferedImage;
    @BeforeEach
    void setUp() {
        // 创建一个 Mock 的 BufferedImage 对象
        bufferedImage = mock(BufferedImage.class);
        when(bufferedImage.getWidth()).thenReturn(100); // 模拟返回图片宽度

        // 创建 Graphics 对象
        Graphics mockGraphics = mock(Graphics.class);
        this.graphics = mockGraphics;

        // 初始化 Cloud 对象
        cloud = new Cloud(bufferedImage, 100, 50);
    }
    @Test
    void testCloudDrawWhenBirdIsAlive() {
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);

        int initialX = cloud.getX();

        // Simulate drawing the cloud
        cloud.draw(graphics, bird);

        // Verify that the x-coordinate decreases by the cloud's speed
        assertTrue(cloud.getX() < initialX, "Cloud X-coordinate should decrease when bird is alive");
    }
    @Test
    void testCloudDrawWhenBirdIsDead() {
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(true);

        int initialX = cloud.getX();

        // Simulate drawing the cloud
        cloud.draw(graphics, bird);

        // Verify that the x-coordinate decreases by 1 when the bird is dead
        assertEquals(initialX - 1, cloud.getX(), "Cloud X-coordinate should decrease by 1 when bird is dead");
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
        cloud.setAttribute(-150, 50); // Set cloud position outside the frame

        cloud.setAttribute(0, 50); // Set cloud position inside the frame
        assertFalse(cloud.isOutFrame(), "Cloud should not be marked as out of frame when on-screen");
    }
    @Test
    void testCloudSpeedAdjustment() {
        // 调用 setSpeed 方法
        cloud.setSpeed(5);

        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);

        int initialX = cloud.getX();
        cloud.draw(graphics, bird);

        // 验证云朵移动是否按照调整后的速度
        assertEquals(initialX - 5, cloud.getX(), "Cloud should move according to the adjusted speed");
    }

    @Test
    void testCloudScaling() {
        // 无法直接访问 `scaleImageWidth` 和 `scaleImageHeight`，但可以通过验证绘制逻辑间接验证
        cloud.draw(graphics, mock(Bird.class));

        // 验证 Graphics 的 `drawImage` 方法是否被调用
        verify(graphics).drawImage(eq(bufferedImage), anyInt(), anyInt(), anyInt(), anyInt(), eq(null));
    }

    @Test
    void testCloudSetAttribute() {
        cloud.setAttribute(200, 100);
        assertEquals(200, cloud.getX(), "Cloud X-coordinate should be updated by setAttribute");
    }

    @Test
    void testCloudInitialization() {
        // 验证初始位置
        assertEquals(100, cloud.getX(), "Initial X-coordinate should match constructor argument");
    }

}
