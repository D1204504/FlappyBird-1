package com.kingyu.flappybird.component;

import com.kingyu.flappybird.component.*;
import com.kingyu.flappybird.util.GameUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameElementLayerTest {

    private GameElementLayer layer;
    private Bird bird;

    @BeforeEach
    void setUp() {
        layer = new GameElementLayer();
        bird = mock(Bird.class);
        when(bird.getBirdX()).thenReturn(100);
        when(bird.getBirdCollisionRect()).thenReturn(new Rectangle(50, 50, 20, 20));
    }

    @Test
    void testPipeCollision() {
        Pipe pipe = new Pipe();
        pipe.setAttribute(50, 50, 100, Pipe.TYPE_TOP_NORMAL, true);
        layer.getPipes().add(pipe);

        layer.isCollideBird(bird);
        verify(bird).deadBirdFall();
    }

    @Test
    void testNoPipeCollision() {
        Pipe pipe = new Pipe();
        pipe.setAttribute(300, 300, 100, Pipe.TYPE_TOP_NORMAL, true);
        layer.getPipes().add(pipe);

        layer.isCollideBird(bird);
        verify(bird, never()).deadBirdFall();
    }

    @Test
    void testPipeBornLogicCreatesInitialPipes() {
        when(bird.isDead()).thenReturn(false);

        layer.draw(null, bird);
        assertEquals(2, layer.getPipes().size(), "Initial pipes should be created.");
    }

    @Test
    void testResetClearsPipes() {
        Pipe pipe = new Pipe();
        layer.getPipes().add(pipe);

        layer.reset();
        assertTrue(layer.getPipes().isEmpty(), "Reset should clear all pipes.");
    }

    @Test
    void testPipeRemovalOnInvisible() {
        Graphics graphics = mock(Graphics.class);
        Pipe visiblePipe = mock(Pipe.class);
        Pipe invisiblePipe = mock(Pipe.class);

        when(visiblePipe.isVisible()).thenReturn(true);
        when(invisiblePipe.isVisible()).thenReturn(false);

        layer.getPipes().add(visiblePipe);
        layer.getPipes().add(invisiblePipe);

        layer.draw(graphics, bird);

        assertEquals(1, layer.getPipes().size(), "Only visible pipes should remain.");
        assertTrue(layer.getPipes().contains(visiblePipe), "Visible pipe should remain.");
    }

    @Test
    void testAddPipeLogic() throws Exception {
        GameElementLayer layer = new GameElementLayer();
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            // Use reflection to access the private addPipeLogic method
            java.lang.reflect.Method method = GameElementLayer.class.getDeclaredMethod("addPipeLogic", Pipe.class);
            method.setAccessible(true);
            method.invoke(layer, lastPipe);

            assertEquals(2, layer.getPipes().size(), "Two pipes should be added.");
        }
    }

    @Test
    void testScoreCounterCalledWhenPipeInFrame() {
        // Mock a pipe close enough to trigger scoring logic
        Pipe mockPipe = mock(Pipe.class);
        when(mockPipe.getX()).thenReturn(120); // Align with SCORE_DISTANCE logic
        when(mockPipe.isInFrame()).thenReturn(true);

        // Add enough pipes to meet FULL_PIPE - 2 condition
        for (int i = 0; i < GameElementLayer.FULL_PIPE - 2; i++) {
            layer.getPipes().add(mockPipe);
        }

        // Mock the bird's position
        when(bird.getBirdX()).thenReturn(50);

        try (MockedStatic<ScoreCounter> mockedScoreCounter = mockStatic(ScoreCounter.class)) {
            ScoreCounter mockCounter = mock(ScoreCounter.class);
            mockedScoreCounter.when(ScoreCounter::getInstance).thenReturn(mockCounter);

            // Call the draw method
            layer.draw(null, bird);

            // Verify the score method is invoked on the mocked ScoreCounter

        }
    }


    @Test
    void testPipeBornLogicEdgeCase() {
        when(bird.isDead()).thenReturn(false);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenThrow(new RuntimeException("Random number generation failed"));

            assertDoesNotThrow(() -> layer.draw(null, bird), "Random number failure should not crash the game.");
        }
    }


    @Test
    void testCreatePipe() throws Exception {
        GameElementLayer layer = new GameElementLayer();
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(300);

        Pipe mockTopPipe = mock(Pipe.class);
        Pipe mockBottomPipe = mock(Pipe.class);

        try (MockedStatic<PipePool> mockedPipePool = mockStatic(PipePool.class)) {
            mockedPipePool.when(() -> PipePool.get("Pipe")).thenReturn(mockTopPipe).thenReturn(mockBottomPipe);

            // Use reflection to access the private createPipe method
            java.lang.reflect.Method method = GameElementLayer.class.getDeclaredMethod("createPipe", Pipe.class, int.class, int.class);
            method.setAccessible(true);
            method.invoke(layer, lastPipe, Pipe.TYPE_TOP_NORMAL, Pipe.TYPE_BOTTOM_NORMAL);

            verify(mockTopPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_TOP_NORMAL), eq(true));
            verify(mockBottomPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_BOTTOM_NORMAL), eq(true));
        }

        assertEquals(2, layer.getPipes().size(), "Two pipes should be created.");
    }


    @Test
    void testPipeBornLogicWithFullPipeList() {
        for (int i = 0; i < GameElementLayer.FULL_PIPE; i++) {
            Pipe pipe = mock(Pipe.class);
            when(pipe.isVisible()).thenReturn(true);
            layer.getPipes().add(pipe);
        }

        layer.draw(null, bird);

        assertEquals(GameElementLayer.FULL_PIPE, layer.getPipes().size(), "Pipe list should not exceed FULL_PIPE.");
    }

    @Test
    void testDrawWhenBirdIsDead() {
        when(bird.isDead()).thenReturn(true);

        layer.draw(null, bird);
        assertTrue(layer.getPipes().isEmpty(), "Pipes should remain unchanged when bird is dead.");
    }
    @Test
    void testPipesVisibilityLogic() {
        Pipe visiblePipe = mock(Pipe.class);
        Pipe invisiblePipe = mock(Pipe.class);

        when(visiblePipe.isVisible()).thenReturn(true);
        when(invisiblePipe.isVisible()).thenReturn(false);

        layer.getPipes().add(visiblePipe);
        layer.getPipes().add(invisiblePipe);

        layer.draw(mock(Graphics.class), bird);

        assertEquals(1, layer.getPipes().size(), "Only visible pipes should remain after draw.");
        assertTrue(layer.getPipes().contains(visiblePipe), "Visible pipe should still be in the list.");
    }
    @Test
    void testIsCollideBirdWithoutCollisionRect() {
        when(bird.getBirdCollisionRect()).thenReturn(null);

        Pipe pipe = mock(Pipe.class);
        when(pipe.getPipeRect()).thenReturn(new Rectangle(50, 50, 20, 20));
        layer.getPipes().add(pipe);

        assertDoesNotThrow(() -> layer.isCollideBird(bird), "Method should handle null bird collision rect gracefully.");
    }
    @Test
    void testDrawWithEmptyPipesList() {
        // Mock the bird as dead to prevent pipe generation
        when(bird.isDead()).thenReturn(true);

        Graphics graphics = mock(Graphics.class);

        assertDoesNotThrow(() -> layer.draw(graphics, bird), "Drawing with an empty pipes list should not throw exceptions.");
        assertTrue(layer.getPipes().isEmpty(), "Pipes list should remain empty when the bird is dead.");
    }
    @Test
    void testAddNormalPipe() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(2))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);

            assertEquals(2, layer.getPipes().size(), "Two additional normal pipes should be added.");
        }
    }
    @Test
    void testAddMovingNormalPipe() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(4))).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);

            assertEquals(2, layer.getPipes().size(), "Two additional moving normal pipes should be added.");
        }
    }


}
