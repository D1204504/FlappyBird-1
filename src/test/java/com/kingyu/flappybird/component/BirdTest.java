package com.kingyu.flappybird.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BirdTest {

    @Test
    public void testBirdInitialization() {
        Bird bird = new Bird();
        assertEquals(0, bird.getCurrentScore());
        assertFalse(bird.isDead());
    }

    @Test
    public void testBirdFlap() {
        Bird bird = new Bird();
        bird.birdFlap();
        assertEquals(Bird.BIRD_UP, bird.keyIsReleased() ? Bird.BIRD_UP : -1);
    }
}
