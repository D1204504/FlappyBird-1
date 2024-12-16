package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import java.io.*;
import static org.mockito.Mockito.*;


class ScoreCounterTest {

    private ScoreCounter scoreCounter;

    @BeforeEach
    void setUp() {
        scoreCounter = ScoreCounter.getInstance();
        scoreCounter.reset();
    }

    @Test
    void testInitialScore() {
        assertEquals(0, scoreCounter.getCurrentScore());
        assertTrue(scoreCounter.getBestScore() >= 0);
    }

    @Test
    void testScoreAccumulation() {
        Bird bird = new Bird(); // 創建有效的 Bird 對象
        bird.reset(); // 重置狀態
        scoreCounter.score(bird); // 模擬得分
        assertEquals(1, scoreCounter.getCurrentScore()); // 檢查分數是否正確
    }

    @Test
    void testBestScoreUpdate() {
        Bird bird = new Bird(); // 創建有效的 Bird 對象
        bird.reset(); // 確保 Bird 處於正常狀態
        scoreCounter.score(bird); // 模擬得分
        scoreCounter.saveScore(); // 保存最高分
        assertEquals(scoreCounter.getCurrentScore(), scoreCounter.getBestScore()); // 檢查最高分是否更新
    }

    @Test
    void testReset() {
        Bird bird = new Bird(); // 創建有效的 Bird 對象
        bird.reset();
        scoreCounter.score(bird); // 模擬得分
        scoreCounter.reset(); // 重置分數
        assertEquals(0, scoreCounter.getCurrentScore()); // 檢查分數是否重置
    }
    @Test
    void testScoreWithDeadBird() {
        // Test scoring with a dead bird
        Bird deadBird = new Bird();
        deadBird.deadBirdFall(); // Mark the bird as dead
        long initialScore = scoreCounter.getCurrentScore();
        scoreCounter.score(deadBird); // Try scoring with the dead bird
        assertEquals(initialScore, scoreCounter.getCurrentScore(), "Score should not increase if the bird is dead");
    }
}
