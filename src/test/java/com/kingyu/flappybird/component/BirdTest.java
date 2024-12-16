package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.kingyu.flappybird.component.Bird;
import com.kingyu.flappybird.util.Constant;

import static org.junit.jupiter.api.Assertions.*;

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
    void testFreeFall() {
        bird.birdFall();
        bird.deadBirdFall();
        assertEquals(Bird.BIRD_DEAD_FALL, bird.getState());
        bird.reset();
        assertEquals(Bird.BIRD_NORMAL, bird.getState());
    }

}
