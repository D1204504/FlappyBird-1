package com.kingyu.flappybird.util;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static org.mockito.Mockito.*;
class GameUtilTest {
    @Test
    void testRandomNumber() {
        int num = GameUtil.getRandomNumber(1, 10);
        assertTrue(num >= 1 && num < 10);
    }

    @Test
    void testIsInProbability() throws Exception {
        Random mockRandom = new Random() {
            @Override
            public double nextDouble() {
                return 0.4; // Always return 0.4 for predictability
            }
        };
        GameUtil.setRandom(mockRandom);

        // Valid test cases
        assertTrue(GameUtil.isInProbability(5, 10), "5/10 probability should return true when random < 0.5");
        assertFalse(GameUtil.isInProbability(1, 100), "1/100 probability should return false when random > 0.01");

        // Invalid input tests

        Exception exception2 = assertThrows(Exception.class, () -> GameUtil.isInProbability(-1, 10));
        assertEquals("传入了非法的参数", exception2.getMessage(), "分子为负数应抛出异常");

        Exception exception3 = assertThrows(Exception.class, () -> GameUtil.isInProbability(5, 0));
        assertEquals("传入了非法的参数", exception3.getMessage(), "分母为0应抛出异常");
    }

    @Test
    void testLoadBufferedImage() {
        // 模拟有效的图片路径
        BufferedImage validImage = GameUtil.loadBufferedImage("resources/img/dead.png");
        assertNotNull(validImage, "应成功加载有效的图片");
        // 模拟无效的图片路径
        BufferedImage invalidImage = GameUtil.loadBufferedImage("resources/invalid_path.png");
        assertNull(invalidImage, "无效图片路径应返回 null");
    }



    @Test
    void testGetStringDimensions() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        String testString = "Flappy Bird";

        int width = GameUtil.getStringWidth(font, testString);
        int height = GameUtil.getStringHeight(font, testString);

        assertTrue(width > 0, "字串寬度應大於0");
        assertTrue(height > 0, "字串高度應大於0");
    }
    @Test
    void testDrawImage() {
        BufferedImage mockImage = mock(BufferedImage.class);
        Graphics mockGraphics = mock(Graphics.class);

        GameUtil.drawImage(mockImage, 10, 20, mockGraphics);

        // 驗證 Graphics 的 drawImage 方法被正確調用
        verify(mockGraphics, times(1)).drawImage(mockImage, 10, 20, null);
    }
    @Test
    void testGetRandomNumberEdgeCases() {
        int result = GameUtil.getRandomNumber(5, 5);
        assertEquals(5, result, "當 min 和 max 相等時，應返回 min 的值");

        result = GameUtil.getRandomNumber(10, 15);
        assertTrue(result >= 10 && result < 15, "隨機數應在範圍內");

        int reversedResult = GameUtil.getRandomNumber(15, 10);
        assertTrue(reversedResult >= 10 && reversedResult < 15, "當範圍反轉時，應返回正確的範圍內值");
    }

    @Test
    void testIsInProbabilityEdgeCases() throws Exception {
        Random mockRandom = new Random() {
            @Override
            public double nextDouble() {
                return 0.0; // 確保隨機數總是最小值
            }
        };
        GameUtil.setRandom(mockRandom);

        assertTrue(GameUtil.isInProbability(1, 1), "1/1 應該總是返回 true");
        assertFalse(GameUtil.isInProbability(0, 1), "0/1 應該總是返回 false");

        // 測試分母為 0 的異常
        Exception exception = assertThrows(Exception.class, () -> GameUtil.isInProbability(1, 0));
        assertEquals("传入了非法的参数", exception.getMessage(), "分母為0應拋出異常");

        // 測試分子為負數的異常
        Exception exception2 = assertThrows(Exception.class, () -> GameUtil.isInProbability(-1, 10));
        assertEquals("传入了非法的参数", exception2.getMessage(), "分子為負數應拋出異常");
    }
    @Test
    void testLoadBufferedImageEdgeCases() {
        BufferedImage nullPathImage = GameUtil.loadBufferedImage(null);
        assertNull(nullPathImage, "路径为 null 应返回 null");

        BufferedImage emptyPathImage = GameUtil.loadBufferedImage("");
        assertNull(emptyPathImage, "路径为空字符串应返回 null");
    }
    @Test
    void testIsInProbabilityBoundaryCase() throws Exception {
        Random mockRandom = new Random() {
            @Override
            public double nextDouble() {
                return 0.999; // 接近于 1，但小于 1
            }
        };
        GameUtil.setRandom(mockRandom);

        assertTrue(GameUtil.isInProbability(10, 10), "10/10 概率应始终返回 true");
    }
    @Test
    void testGetRandomNumberReversedRange() {
        int result = GameUtil.getRandomNumber(15, 10);
        assertTrue(result >= 10 && result < 15, "范围反转时应返回正确的值");
    }
    @Test
    void testGetStringDimensionsEdgeCase() {
        Font font = new Font("Arial", Font.PLAIN, 12);

        int width = GameUtil.getStringWidth(font, "");
        int height = GameUtil.getStringHeight(font, "");

        assertEquals(0, width, "空字符串的宽度应为 0");
        assertTrue(height > 0, "空字符串的高度应与字体高度一致");
    }

    @Test
    void testDrawImageNull() {
        Graphics mockGraphics = mock(Graphics.class);

        // Call the method with a null image
        GameUtil.drawImage(null, 10, 20, mockGraphics);

        // Verify no drawImage method is invoked on the mockGraphics object
        verify(mockGraphics, never()).drawImage(any(), anyInt(), anyInt(), isNull());
    }
    @Test
    void testLoadBufferedImageWithInvalidFile() {
        BufferedImage nonImageFile = GameUtil.loadBufferedImage("resources/non_image_file.txt");
        assertNull(nonImageFile, "非图片文件路径应返回 null");

        BufferedImage nullPathImage = GameUtil.loadBufferedImage(null);
        assertNull(nullPathImage, "null 文件路径应返回 null");
    }
    @Test
    void testGetRandomNumberExtremeValues() {
        int result = GameUtil.getRandomNumber(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertTrue(result >= Integer.MIN_VALUE && result < Integer.MAX_VALUE, "随机数应在范围内");

        result = GameUtil.getRandomNumber(Integer.MAX_VALUE, Integer.MIN_VALUE);
        assertTrue(result >= Integer.MIN_VALUE && result < Integer.MAX_VALUE, "随机数反转范围应处理正确");
    }
    @Test
    void testIsInProbabilityBoundary() throws Exception {
        Random mockRandom = new Random() {
            @Override
            public double nextDouble() {
                return 1.0; // 确保随机值是边界值
            }
        };
        GameUtil.setRandom(mockRandom);

        assertFalse(GameUtil.isInProbability(5, 5), "当随机值等于 1.0 时，应返回 false");
    }

}