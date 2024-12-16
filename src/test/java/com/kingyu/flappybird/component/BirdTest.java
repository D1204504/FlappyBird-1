package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.kingyu.flappybird.util.Constant;



class BirdTest {

    private Bird bird;

    @BeforeEach
    void setUp() {
        bird = new Bird();
    }

    @Test
    void testInitialPosition() {
        int expectedY = (Constant.FRAME_HEIGHT >> 1) - (Bird.BIRD_HEIGHT / 2) + Bird.RECT_DESCALE * 2;
        assertEquals(expectedY, bird.getBirdCollisionRect().y);
    }


    @Test
    void testFlap() {
        Bird bird = new Bird();
        bird.keyReleased(); // 確保按鍵釋放
        bird.birdFlap();    // 模擬振翅
        assertEquals(Bird.BIRD_UP, bird.getState());
    }



    @Test
    void testFall() {
        bird.birdFall();
        assertEquals(Bird.BIRD_FALL, bird.getState());
    }

    @Test
    void testCollisionRectangle() {
        assertNotNull(bird.getBirdCollisionRect());
        assertEquals(Bird.BIRD_WIDTH - Bird.RECT_DESCALE * 3, bird.getBirdCollisionRect().width);
    }

    @Test
    void testDie() {
        bird.deadBirdFall();
        assertEquals(Bird.BIRD_DEAD_FALL, bird.getState());
    }

    @Test
    void testMovement() {
        // 測試小鳥墜落
        bird.birdFall();
        bird.movement();
        assertEquals(Bird.BIRD_FALL, bird.getState(), "應該是墜落狀態");

        // 測試小鳥振翅
        bird.birdFlap();
        bird.movement();
        assertEquals(Bird.BIRD_UP, bird.getState(), "應該是振翅狀態");
    }
    @Test
    void testFreeFall() {
        // 測試小鳥墜落
        bird.birdFall();
        bird.movement(); // 模擬墜落
        assertTrue(bird.getBirdCollisionRect().y < Constant.FRAME_HEIGHT, "小鳥應該繼續墜落");

        // 測試墜落速度是否正確
        int initialY = bird.getBirdCollisionRect().y;
        bird.movement();
    }
    @Test
    void testKeyReleased() {
        bird.keyPressed();
        assertFalse(bird.keyIsReleased(), "按下鍵後應該是已按下狀態");

        bird.keyReleased();
        assertTrue(bird.keyIsReleased(), "釋放鍵後應該是已釋放狀態");
    }
    @Test
    void testReset() {
        bird.birdFlap(); // 小鳥振翅
        bird.reset();
        assertEquals(Bird.BIRD_NORMAL, bird.getState(), "小鳥應該恢復為正常狀態");
    }
    @Test
    void testIsDead() {
        bird.deadBirdFall();
        assertTrue(bird.isDead(), "小鳥死亡後應該返回 true");
    }
    @Test
    void testDeadBirdFall() {
        bird.deadBirdFall();
        assertEquals(Bird.BIRD_DEAD_FALL, bird.getState(), "Bird should enter dead fall state.");
    }

    @Test
    void testKeyReleasedLogic() {
        bird.keyPressed();
        assertFalse(bird.keyIsReleased(), "Key flag should be false after pressing.");
        bird.keyReleased();
        assertTrue(bird.keyIsReleased(), "Key flag should be true after releasing.");
    }
}


