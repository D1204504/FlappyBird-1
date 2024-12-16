package com.kingyu.flappybird.component;

import org.junit.jupiter.api.Test;

import com.kingyu.flappybird.component.Bird;
import com.kingyu.flappybird.component.Pipe;

import static org.junit.jupiter.api.Assertions.*;

class CollisionTest {

    @Test
    void testCollisionDetection() {
        Bird bird = new Bird();
        Pipe pipe = new Pipe();

        // 設置水管的碰撞矩形與小鳥重疊
        pipe.setAttribute(bird.getBirdX(), bird.getBirdCollisionRect().y, Bird.BIRD_HEIGHT, Pipe.TYPE_TOP_NORMAL, true);

        assertTrue(pipe.getPipeRect().intersects(bird.getBirdCollisionRect()));
    }

    @Test
    void testNoCollision() {
        Bird bird = new Bird();
        Pipe pipe = new Pipe();

        // 設置水管的碰撞矩形在小鳥範圍之外
        pipe.setAttribute(1000, 1000, 100, Pipe.TYPE_TOP_NORMAL, true);

        assertFalse(pipe.getPipeRect().intersects(bird.getBirdCollisionRect()));
    }
}
