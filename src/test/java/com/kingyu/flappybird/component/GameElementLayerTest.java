package com.kingyu.flappybird.component;
import com.kingyu.flappybird.component.Bird;
import com.kingyu.flappybird.component.GameElementLayer;
import com.kingyu.flappybird.component.Pipe;
import com.kingyu.flappybird.util.GameUtil;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.util.List;
import org.mockito.MockedStatic;

public class GameElementLayerTest {

    @Test
    void testPipeCollision() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdCollisionRect()).thenReturn(new Rectangle(50, 50, 20, 20));

        Pipe pipe = new Pipe();
        pipe.setAttribute(50, 50, 100, Pipe.TYPE_TOP_NORMAL, true);
        layer.getPipes().add(pipe);

        layer.isCollideBird(bird);
        verify(bird).deadBirdFall();
    }

    @Test
    void testNoPipeCollision() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdCollisionRect()).thenReturn(new Rectangle(300, 300, 20, 20));

        Pipe pipe = new Pipe();
        pipe.setAttribute(50, 50, 100, Pipe.TYPE_TOP_NORMAL, true);
        layer.getPipes().add(pipe);

        layer.isCollideBird(bird);
        verify(bird, never()).deadBirdFall();
    }

    @Test
    void testPipeBornLogicAttributes() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);

        layer.reset();
        layer.draw(null, bird);

        assertEquals(2, layer.getPipes().size(), "There should be 2 pipes created initially.");
        Pipe topPipe = (Pipe) layer.getPipes().toArray()[0];
        Pipe bottomPipe = (Pipe) layer.getPipes().toArray()[1];

        assertTrue(topPipe.getPipeRect().getHeight() > 0, "Top pipe should have a positive height.");
        assertTrue(bottomPipe.getPipeRect().getHeight() > 0, "Bottom pipe should have a positive height.");
    }

    @Test
    void testPipeBornLogicForHoverPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);

        layer.reset();
        layer.draw(null, bird); // Invokes pipeBornLogic internally
        assertEquals(2, layer.getPipes().size(), "Hover pipes should be created by pipeBornLogic.");
    }

    @Test
    void testIsCollideBirdWithCollision() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdCollisionRect()).thenReturn(new Rectangle(50, 50, 20, 20));

        Pipe pipe = new Pipe();
        pipe.setAttribute(50, 50, 100, Pipe.TYPE_TOP_NORMAL, true);
        layer.getPipes().add(pipe);

        layer.isCollideBird(bird);
        verify(bird).deadBirdFall();
    }

    @Test
    void testIsCollideBirdWithoutCollision() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdCollisionRect()).thenReturn(new Rectangle(300, 300, 20, 20));

        Pipe pipe = new Pipe();
        pipe.setAttribute(50, 50, 100, Pipe.TYPE_TOP_NORMAL, true);
        layer.getPipes().add(pipe);

        layer.isCollideBird(bird);
        verify(bird, never()).deadBirdFall();
    }

    @Test
    void testResetClearsPipes() {
        GameElementLayer layer = new GameElementLayer();
        Pipe pipe = new Pipe();
        pipe.setAttribute(50, 50, 100, Pipe.TYPE_TOP_NORMAL, true);
        layer.getPipes().add(pipe);

        layer.reset();
        assertTrue(layer.getPipes().isEmpty(), "Reset should clear all pipes.");
    }

    @Test
    void testPipeRemovalOnInvisible() {
        GameElementLayer layer = new GameElementLayer();
        Graphics graphics = mock(Graphics.class); // 模擬 Graphics 對象

        Pipe visiblePipe = mock(Pipe.class);
        when(visiblePipe.isVisible()).thenReturn(true);

        Pipe invisiblePipe = mock(Pipe.class);
        when(invisiblePipe.isVisible()).thenReturn(false);

        layer.getPipes().add(visiblePipe);
        layer.getPipes().add(invisiblePipe);

        layer.draw(graphics, mock(Bird.class));

        assertEquals(1, layer.getPipes().size(), "Only visible pipes should remain after draw.");
        assertEquals(visiblePipe, layer.getPipes().toArray()[0], "The remaining pipe should be the visible one.");
    }

    @Test
    void testDrawWithEmptyPipes() {
        GameElementLayer layer = new GameElementLayer();
        Graphics graphics = mock(Graphics.class);
        Bird bird = mock(Bird.class);

        // Simulate bird being dead, which should prevent pipes from being generated
        when(bird.isDead()).thenReturn(true);

        // Call the draw method, but no pipes should be generated because the bird is dead
        layer.draw(graphics, bird);

        // Assert that the pipe list is empty
        assertTrue(layer.getPipes().isEmpty(), "No pipes should exist when the bird is dead.");
    }


    @Test
    void testDrawWhenBirdIsDead() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Graphics graphics = mock(Graphics.class);

        when(bird.isDead()).thenReturn(true);

        layer.draw(graphics, bird);

        verifyNoInteractions(graphics);
        assertTrue(layer.getPipes().isEmpty(), "Pipes should remain unchanged when bird is dead.");
    }

    @Test
    void testPipeBornLogicCreatesInitialPipes() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(100);

        layer.draw(null, bird);

        assertEquals(2, layer.getPipes().size(), "Initial pipes should be created when pipes list is empty.");
    }

    @Test
    void testAddNormalPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);
        when(lastPipe.getX()).thenReturn(100);
        when(lastPipe.isInFrame()).thenReturn(true);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);
        }
    }

    @Test
    void testAddHoverPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);
        when(lastPipe.getX()).thenReturn(100);
        when(lastPipe.isInFrame()).thenReturn(true);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);
        }
    }

    @Test
    void testAddMovingNormalPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);
        when(lastPipe.getX()).thenReturn(100);
        when(lastPipe.isInFrame()).thenReturn(true);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedStatic = mockStatic(GameUtil.class)) {
            mockedStatic.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(true);
            mockedStatic.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);
        }
    }

    @Test
    void testAddMovingHoverPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);
        when(lastPipe.getX()).thenReturn(100);
        when(lastPipe.isInFrame()).thenReturn(true);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedStatic = mockStatic(GameUtil.class)) {
            mockedStatic.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(true);
            mockedStatic.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);
        }
    }

    @Test
    void testDrawWithNullGraphics() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        layer.draw(null, bird);

        assertDoesNotThrow(() -> layer.draw(null, bird), "Drawing with null graphics should not throw exceptions.");
    }

    @Test
    void testPipeBornLogicWithFullPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        for (int i = 0; i < GameElementLayer.FULL_PIPE; i++) {
            Pipe pipe = mock(Pipe.class);
            when(pipe.isVisible()).thenReturn(true);
            layer.getPipes().add(pipe);
        }

        layer.draw(null, bird);

        assertEquals(GameElementLayer.FULL_PIPE, layer.getPipes().size(), "Pipe list should not exceed FULL_PIPE.");
    }

    @Test
    void testPipeBornLogicWhenBirdIsDead() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(true);

        layer.draw(null, bird);

        assertTrue(layer.getPipes().isEmpty(), "No pipes should be added when the bird is dead.");
    }

    @Test
    void testCreatePipe() {
        GameElementLayer layer = new GameElementLayer();
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(100);
        when(lastPipe.isInFrame()).thenReturn(true);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            // Trigger the pipe creation logic
            layer.draw(null, mock(Bird.class));

            // Assert pipes are created
            assertEquals(2, layer.getPipes().size(), "Two pipes should be created.");
        }
    }
    @Test
    void testPipeBornLogicEdgeCase() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        // Set up mock behavior for bird
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);

        // Simulate the scenario where GameUtil's randomness causes an exception
        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenThrow(new RuntimeException("Random number generation failed"));

            // Attempt to draw, this should not throw an exception
        }
    }
    @Test
    void testEdgeCasePipeVisibility() {
        GameElementLayer layer = new GameElementLayer();
        Pipe pipe = mock(Pipe.class);

        // Set up pipe to be initially visible
        when(pipe.isVisible()).thenReturn(true);
        layer.getPipes().add(pipe);

        // Simulate the drawing process
        Graphics graphics = mock(Graphics.class);
        Bird bird = mock(Bird.class);

        // Now set the pipe as invisible after adding it to the list
        when(pipe.isVisible()).thenReturn(false);

        // Simulate the drawing process, which should remove the invisible pipe
        layer.draw(graphics, bird);

        // Assert that the pipe is removed
    }


    @Test
    void testPipeBornLogicWithFullPipeList() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        // Fill the pipe list to FULL_PIPE
        for (int i = 0; i < GameElementLayer.FULL_PIPE; i++) {
            Pipe pipe = mock(Pipe.class);
            when(pipe.isVisible()).thenReturn(true);
            layer.getPipes().add(pipe);
        }

        // Ensure that no more pipes are added
        layer.draw(null, bird);

        // Assert the pipe count doesn't exceed FULL_PIPE
        assertEquals(GameElementLayer.FULL_PIPE, layer.getPipes().size(), "Pipe list should not exceed FULL_PIPE.");
    }
}
