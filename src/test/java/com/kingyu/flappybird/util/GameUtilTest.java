package com.kingyu.flappybird.util;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
class GameUtilTest {
    @Test
    void testRandomNumber() {
        int num = GameUtil.getRandomNumber(1, 10);
        assertTrue(num >= 1 && num < 10);
    }

    @Test
    void testIsInProbability() throws Exception {
        // Mock Random to control randomness
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
        Exception exception = assertThrows(Exception.class, () -> GameUtil.isInProbability(0, 10));
        assertEquals("传入了非法的参数", exception.getMessage());
    }

}