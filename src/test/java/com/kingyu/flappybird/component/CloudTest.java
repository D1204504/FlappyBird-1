package com.kingyu.flappybird.component;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CloudTest {

    @Test
    void testCloudMovement() {
        // 模擬 BufferedImage 和 Graphics 對象
        BufferedImage mockImg = mock(BufferedImage.class);
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();

        // 初始化 Cloud 和 Bird
        Cloud cloud = new Cloud(mockImg, 100, 50);
        Bird bird = new Bird();

        // 測試 draw 方法
        cloud.draw(g, bird); // 傳入模擬的 Graphics 和 Bird
        assertFalse(cloud.isOutFrame(), "雲朵應該還在畫面內");
    }

    @Test
    void testBackgroundMovement() {
        // 模擬 BufferedImage 和 Graphics 對象
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();

        // 測試 GameBackground
        GameBackground bg = new GameBackground();
        Bird bird = new Bird();

        // 測試 draw 方法
        bg.draw(g, bird); // 傳入模擬的 Graphics 和 Bird
    }
    @Test
    void testForegroundLogic() {
        // 模擬 BufferedImage 和 Graphics 對象
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();

        // 測試 GameForeground
        GameForeground fg = new GameForeground();
        Bird bird = new Bird();

        // 測試 draw 方法
        fg.draw(g, bird); // 傳入模擬的 Graphics 和 Bird
    }
}



