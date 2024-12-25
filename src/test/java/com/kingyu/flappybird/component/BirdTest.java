package com.kingyu.flappybird.component;

import com.kingyu.flappybird.app.Game;
import com.kingyu.flappybird.util.MusicUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.kingyu.flappybird.util.Constant;
import org.mockito.MockedStatic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;


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
    @Test
    void testDieWithMockedDependencies() {
        // Mock dependencies
        ScoreCounter mockCounter = mock(ScoreCounter.class);
        Bird bird = new Bird(mockCounter);

        try (MockedStatic<MusicUtil> mockedMusicUtil = mockStatic(MusicUtil.class)) {
            // Simulate bird falling below the bottom boundary
            bird.birdFall(); // Set state to FALL
            for (int i = 0; i < 50; i++) {
                bird.movement(); // Simulate multiple movements to trigger die
            }

            // Verify MusicUtil.playCrash is called
            mockedMusicUtil.verify(() -> MusicUtil.playCrash(), times(1));

            // Verify die behavior
            verify(mockCounter, times(1)).saveScore();
            assertEquals(Game.STATE_OVER, Game.getGameState(), "Game state should be STATE_OVER after die.");
            assertEquals(Bird.BIRD_DEAD, bird.getState(), "Bird state should be DEAD after die.");
        }
    }


    @Test
    void testVelocityConstraints() {
        bird.birdFall();

        // Simulate multiple movements and verify velocity constraints
        for (int i = 0; i < 10; i++) {
            bird.movement();
            assertTrue(bird.getVelocity() <= Bird.MAX_VEL_Y, "Velocity should not exceed maximum velocity during fall.");
        }

        // Test flapping resets velocity correctly
        bird.birdFlap();
        assertEquals(Bird.ACC_FLAP, bird.getVelocity(), "Flapping should set velocity to the upward flap acceleration.");
    }
    @Test
    void testCollisionWithTopBoundary() {
        bird.reset();

        // 模擬多次振翅導致小鳥接近頂部邊界
        for (int i = 0; i < 10; i++) {
            bird.birdFlap();
            bird.movement();
        }

        // 驗證小鳥不會超過頂部邊界
        assertTrue(bird.getBirdCollisionRect().y >= Constant.TOP_BAR_HEIGHT, "小鳥應該保持在頂部邊界內");


    }
    @Test
    void testGetCurrentScore() {
        // Create a mock for ScoreCounter
        ScoreCounter mockCounter = mock(ScoreCounter.class);
        when(mockCounter.getCurrentScore()).thenReturn(100L);

        // Inject the mock into the Bird object
        Bird bird = new Bird(mockCounter);

        // Assert the current score
        assertEquals(100L, bird.getCurrentScore(), "Should return the mocked current score");

        // Verify the mock interaction
        verify(mockCounter, times(1)).getCurrentScore();
    }

    @Test
    void testGetBestScore() {
        // Create a mock for ScoreCounter
        ScoreCounter mockCounter = mock(ScoreCounter.class);
        when(mockCounter.getBestScore()).thenReturn(500L);

        // Inject the mock into the Bird object
        Bird bird = new Bird(mockCounter);

        // Assert the best score
        assertEquals(500L, bird.getBestScore(), "Should return the mocked best score");

        // Verify the mock interaction
        verify(mockCounter, times(1)).getBestScore();
    }
    @Test
    void testResetRestoresInitialState() {
        bird.birdFlap(); // Change the bird's state
        bird.movement(); // Simulate movement to change position

        bird.reset();

        int expectedY = (Constant.FRAME_HEIGHT >> 1) - (Bird.BIRD_HEIGHT / 2) + Bird.RECT_DESCALE * 2;

        assertEquals(Bird.BIRD_NORMAL, bird.getState(), "Bird state should be reset to NORMAL.");
        assertEquals(expectedY, bird.getBirdCollisionRect().y, "Bird position should be reset to initial Y.");
        assertEquals(0, bird.getVelocity(), "Bird velocity should be reset to 0.");
    }

    @Test
    void testMovementTopBoundary() {
        bird.reset(); // Ensure bird starts in the initial position
        for (int i = 0; i < 50; i++) {
            bird.birdFlap(); // Simulate continuous flapping
            bird.movement();
        }

        // Verify bird does not move above the top boundary
        assertTrue(bird.getBirdCollisionRect().y >= Constant.TOP_BAR_HEIGHT,
                "Bird should not move above the top boundary.");
    }

    @Test
    void testKeyPressReleaseToggle() {
        bird.keyPressed();
        assertFalse(bird.keyIsReleased(), "Key flag should be false after pressing.");

        bird.keyReleased();
        assertTrue(bird.keyIsReleased(), "Key flag should be true after releasing.");
    }
    @Test
    void testImageSetForPositiveVelocity() {
        Graphics mockGraphics = mock(Graphics.class);
        bird.reset();

        // Simulate positive velocity
        bird.birdFlap(); // Sets velocity to ACC_FLAP
        bird.draw(mockGraphics);

        try {
            // Use reflection to access the private `image` field
            java.lang.reflect.Field imageField = Bird.class.getDeclaredField("image");
            imageField.setAccessible(true);
            BufferedImage currentImage = (BufferedImage) imageField.get(bird);

            // Use reflection to access the private `birdImages` field
            java.lang.reflect.Field birdImagesField = Bird.class.getDeclaredField("birdImages");
            birdImagesField.setAccessible(true);
            BufferedImage[][] birdImages = (BufferedImage[][]) birdImagesField.get(bird);

            // Assert that the correct image is set
            assertEquals(birdImages[Bird.BIRD_UP][0], currentImage, "Image should be set to birdImages[BIRD_UP][0] when velocity > 0.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to access private fields 'image' or 'birdImages'");
        }
    }
    @Test
    void testGameOverAnimationDraw() {
        // Mock the Graphics object and GameOverAnimation
        Graphics mockGraphics = mock(Graphics.class);
        GameOverAnimation mockAnimation = mock(GameOverAnimation.class);

        // Create a bird instance and inject the mock GameOverAnimation
        Bird bird = new Bird();
        try {
            Field animationField = Bird.class.getDeclaredField("gameOverAnimation");
            animationField.setAccessible(true);
            animationField.set(bird, mockAnimation);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject mock GameOverAnimation.");
        }

        // Set bird to BIRD_FALL state
        bird.reset();
        bird.birdFall(); // Set state to BIRD_FALL

        // Simulate the bird falling below the bottom boundary
        try {
            Field yField = Bird.class.getDeclaredField("y");
            yField.setAccessible(true);
            yField.setInt(bird, Constant.FRAME_HEIGHT + 100); // Move bird below the bottom boundary

            Field birdCollisionRectField = Bird.class.getDeclaredField("birdCollisionRect");
            birdCollisionRectField.setAccessible(true);
            Rectangle birdCollisionRect = (Rectangle) birdCollisionRectField.get(bird);
            birdCollisionRect.y = Constant.FRAME_HEIGHT + 100; // Update collision rectangle position
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set private fields 'y' or 'birdCollisionRect'.");
        }

        bird.movement(); // Trigger die() and transition to BIRD_DEAD

        // Verify the bird is now in the BIRD_DEAD state
        assertEquals(Bird.BIRD_DEAD, bird.getState(), "Bird state should be BIRD_DEAD.");

        // Call draw and verify that gameOverAnimation.draw() is executed
        bird.draw(mockGraphics);
        verify(mockAnimation, times(1)).draw(mockGraphics, bird);
    }


}


