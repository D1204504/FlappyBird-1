package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreCounterTest {

    @BeforeEach
    public void resetScoreCounter() {
        ScoreCounter scoreCounter = ScoreCounter.getInstance();
        scoreCounter.reset(); // 重置分數狀態
    }

    @Test
    public void testInitialScore() {
        ScoreCounter scoreCounter = ScoreCounter.getInstance();
        assertEquals(0, scoreCounter.getCurrentScore(), "初始分數應該為 0");
    }

    @Test
    public void testScoreIncrement() {
        ScoreCounter scoreCounter = ScoreCounter.getInstance();
        Bird bird = new Bird(); // 模擬一隻未死亡的鳥
        scoreCounter.score(bird);
        assertEquals(1, scoreCounter.getCurrentScore(), "得分應該增加 1");
    }

    @Test
    public void testSaveBestScore() {
        ScoreCounter scoreCounter = ScoreCounter.getInstance();
        long initialBest = scoreCounter.getBestScore();
        Bird bird = new Bird(); // 模擬一隻未死亡的鳥
        scoreCounter.score(bird);
        scoreCounter.saveScore();
        assertTrue(scoreCounter.getBestScore() >= initialBest, "最高分數應該更新");
    }
}
