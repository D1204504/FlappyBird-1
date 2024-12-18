package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import java.io.*;
import static org.mockito.Mockito.*;
import com.kingyu.flappybird.util.MusicUtil;
import com.kingyu.flappybird.util.Constant;


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
    @Test
    void testLoadBestScoreException() {
        // 模擬文件讀取異常
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);

        // 確保不會拋出異常
        assertDoesNotThrow(() -> scoreCounter.saveScore(), "保存分數時不應拋出異常");
    }
    @Test
    void testSetInstance() {
        ScoreCounter mockScoreCounter = mock(ScoreCounter.class);
        ScoreCounter.setInstance(mockScoreCounter);

        assertNotNull(ScoreCounter.getInstance(), "ScoreCounter 應成功替換為模擬對象");
    }
    @Test
    void testSingleton() {
        ScoreCounter instance1 = ScoreCounter.getInstance();
        ScoreCounter instance2 = ScoreCounter.getInstance();

        assertSame(instance1, instance2, "ScoreCounter 應是單例");
    }
    @Test
    void testMultipleResets() {
        scoreCounter.reset();
        scoreCounter.reset();

        assertEquals(0, scoreCounter.getCurrentScore(), "多次重置後分數應為 0");
    }
    @Test
    void testScorePlaysSound() {
        try (MockedStatic<MusicUtil> mockedMusicUtil = mockStatic(MusicUtil.class)) {
            Bird bird = new Bird();
            bird.reset();
            scoreCounter.score(bird);

            // 檢查是否正確調用了 MusicUtil.playScore()
            mockedMusicUtil.verify(() -> MusicUtil.playScore(), times(1));
        }
    }


}
