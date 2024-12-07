package com.kingyu.flappybird.component;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.awt.Rectangle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BirdTest {

    @Test
    public void testBirdInitialization() {
        Bird bird = new Bird();
        assertEquals(0, bird.getCurrentScore(), "初始分數應為 0");
        assertFalse(bird.isDead(), "初始狀態應該為未死亡");
    }

    @Test
    public void testBirdFlap() {
        Bird bird = new Bird();
        bird.birdFlap();
        assertEquals(Bird.BIRD_UP, bird.getState(), "振翅後狀態應為上升");
    }

    @Test
    public void testBirdFall() {
        Bird bird = new Bird();
        bird.birdFall();
        assertEquals(Bird.BIRD_FALL, bird.getState(), "下落後狀態應為下降");
    }

    @Test
    public void testBirdCollisionRect() {
        Bird bird = new Bird();
        Rectangle rect = bird.getBirdCollisionRect();
        assertNotNull(rect, "碰撞矩形應被初始化");
    }

    @Test
    public void testBirdDeath() {
        Bird bird = new Bird();
        bird.deadBirdFall();
        assertTrue(bird.isDead(), "死亡後狀態應該為死亡");
    }
}
