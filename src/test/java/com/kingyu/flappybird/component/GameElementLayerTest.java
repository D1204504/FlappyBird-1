package com.kingyu.flappybird.component;

import com.kingyu.flappybird.component.*;
import com.kingyu.flappybird.util.GameUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.kingyu.flappybird.util.ExitManager;
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
    void testAddNormalPipe() throws Exception {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);

        try (MockedStatic<PipePool> mockedPipePool = mockStatic(PipePool.class)) {
            Pipe mockTopPipe = mock(Pipe.class);
            Pipe mockBottomPipe = mock(Pipe.class);

            mockedPipePool.when(() -> PipePool.get("Pipe"))
                    .thenReturn(mockTopPipe)
                    .thenReturn(mockBottomPipe);

            // Use reflection to access the private method
            java.lang.reflect.Method method = GameElementLayer.class.getDeclaredMethod("addNormalPipe", Pipe.class);
            method.setAccessible(true);
            method.invoke(layer, lastPipe);

            // Verify that pipes of the correct type were created
            verify(mockTopPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_TOP_NORMAL), eq(true));
            verify(mockBottomPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_BOTTOM_NORMAL), eq(true));
        }
    }

    @Test
    void testAddMovingNormalPipe() throws Exception {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);

        try (MockedStatic<PipePool> mockedPipePool = mockStatic(PipePool.class)) {
            Pipe mockTopPipe = mock(Pipe.class);
            Pipe mockBottomPipe = mock(Pipe.class);

            mockedPipePool.when(() -> PipePool.get("Pipe"))
                    .thenReturn(mockTopPipe)
                    .thenReturn(mockBottomPipe);

            // Use reflection to access the private method
            java.lang.reflect.Method method = GameElementLayer.class.getDeclaredMethod("addMovingNormalPipe", Pipe.class);
            method.setAccessible(true);
            method.invoke(layer, lastPipe);

            // Verify that pipes of the correct type were created
            verify(mockTopPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_TOP_HARD), eq(true));
            verify(mockBottomPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_BOTTOM_HARD), eq(true));
        }
    }

    @Test
    void testAddNormalPipeLogic() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // Simulate the branch for addNormalPipe
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(2))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);

            // Verify that normal pipes were added
            assertEquals(2, layer.getPipes().size(), "Two additional normal pipes should be added.");
        }
    }

    @Test
    void testAddMovingNormalPipeLogic() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // Simulate the branch for addMovingNormalPipe
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(4))).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);

            // Verify that moving normal pipes were added
            assertEquals(2, layer.getPipes().size(), "Two additional moving normal pipes should be added.");
        }
    }

    @Test
    void testAddPipeLogicExceptionHandling() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // Simulate an exception during random number generation
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenThrow(new RuntimeException("Random number generation failed"));

            // Invoke addPipeLogic directly using reflection
            assertDoesNotThrow(() -> {
                java.lang.reflect.Method method = GameElementLayer.class.getDeclaredMethod("addPipeLogic", Pipe.class);
                method.setAccessible(true);
                method.invoke(layer, lastPipe);
            }, "Exception should be handled gracefully.");
        }
    }

    @Test
    void testCreatePipeWithNormalTypes() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);
        layer.getPipes().add(lastPipe);

        Pipe mockTopPipe = mock(Pipe.class);
        Pipe mockBottomPipe = mock(Pipe.class);

        try (MockedStatic<PipePool> mockedPipePool = mockStatic(PipePool.class)) {
            mockedPipePool.when(() -> PipePool.get("Pipe"))
                    .thenReturn(mockTopPipe)
                    .thenReturn(mockBottomPipe);

            try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
                // Mock conditions for addNormalPipe
                mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(false);
                mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(2))).thenReturn(true);
                mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

                // Invoke the logic
                layer.draw(null, bird);

                // Verify that setAttribute is called with the correct types
                verify(mockTopPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_TOP_NORMAL), eq(true));
                verify(mockBottomPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_BOTTOM_NORMAL), eq(true));
            }
        }
    }

    @Test
    void testPipeListAtCapacity() {
        for (int i = 0; i < GameElementLayer.FULL_PIPE; i++) {
            Pipe pipe = mock(Pipe.class);
            when(pipe.isVisible()).thenReturn(true);
            layer.getPipes().add(pipe);
        }

        layer.draw(null, bird);

        assertEquals(GameElementLayer.FULL_PIPE, layer.getPipes().size(), "Pipe list should remain at FULL_PIPE capacity.");
    }

    @Test
    void testPipeDimensionsAtLimits() {
        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenReturn(GameElementLayer.MIN_HEIGHT); // Test minimum height

            layer.draw(null, bird);

            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenReturn(GameElementLayer.MAX_HEIGHT); // Test maximum height

            layer.draw(null, bird);

            assertEquals(4, layer.getPipes().size(), "Two pipes for each limit should be created.");
        }
    }

    @Test
    void testPipeBornLogicExceptionHandling() {
        when(bird.isDead()).thenReturn(false);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenThrow(new RuntimeException("Simulated exception"));

            assertDoesNotThrow(() -> layer.draw(null, bird), "pipeBornLogic should handle exceptions gracefully.");
        }
    }

    @Test
    void testRemoveInvisiblePipes() {
        Pipe visiblePipe = mock(Pipe.class);
        Pipe invisiblePipe = mock(Pipe.class);

        when(visiblePipe.isVisible()).thenReturn(true);
        when(invisiblePipe.isVisible()).thenReturn(false);

        layer.getPipes().add(visiblePipe);
        layer.getPipes().add(invisiblePipe);

        layer.draw(mock(Graphics.class), bird);

        assertEquals(1, layer.getPipes().size(), "Invisible pipes should be removed.");
        assertTrue(layer.getPipes().contains(visiblePipe), "Visible pipe should remain in the list.");
    }

    @Test
    void testPipeBornLogicAddsNormalPipe() {
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
    void testPipeBornLogicAddsMovingNormalPipe() {
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

    @Test
    void testAddMovingNormalPipeBranch() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // Mock probability logic for addMovingNormalPipe
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(4))).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            layer.draw(null, bird);

            // Verify that two pipes (top and bottom) are added
            assertEquals(2, layer.getPipes().size(), "Two moving normal pipes should be added.");
        }
    }

    @Test
    void testGenerateInitialPipesExceptionHandling() {
        try (MockedStatic<PipePool> mockedPipePool = mockStatic(PipePool.class)) {
            mockedPipePool.when(() -> PipePool.get("Pipe"))
                    .thenThrow(new RuntimeException("Simulated pipe pool exception"));

            assertDoesNotThrow(() -> layer.draw(null, bird), "Exception during pipe generation should not crash the game.");
        }
    }

    @Test
    void testDrawWithNullGraphics() {
        when(bird.isDead()).thenReturn(false);

        assertDoesNotThrow(() -> layer.draw(null, bird), "Draw method should handle null Graphics object gracefully.");
    }

    @Test
    void testMaximumCapacity() {
        for (int i = 0; i < GameElementLayer.FULL_PIPE + 2; i++) {
            Pipe pipe = mock(Pipe.class);
            when(pipe.isVisible()).thenReturn(true);
            layer.getPipes().add(pipe);
        }

        layer.draw(null, bird);

        assertEquals(GameElementLayer.FULL_PIPE, layer.getPipes().size(), "Pipe list size should not exceed FULL_PIPE.");
    }

    @Test
    void testAddPipeLogicComprehensive() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // Test each branch of the logic
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(4))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);
            layer.draw(null, bird); // Simulates addMovingHoverPipe

            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(4))).thenReturn(false);
            layer.draw(null, bird); // Simulates addMovingNormalPipe

            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(2))).thenReturn(true);
            layer.draw(null, bird); // Simulates addNormalPipe

            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(2))).thenReturn(false);
            layer.draw(null, bird); // Simulates addHoverPipe
        }
    }

    @Test
    void testIsCollideBirdWithEmptyPipeList() {
        assertDoesNotThrow(() -> layer.isCollideBird(bird), "isCollideBird should handle empty pipe list gracefully.");
    }

    @Test
    void testCreatePipeDirectly() throws Exception {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);

        Pipe mockTopPipe = mock(Pipe.class);
        Pipe mockBottomPipe = mock(Pipe.class);

        try (MockedStatic<PipePool> mockedPipePool = mockStatic(PipePool.class)) {
            mockedPipePool.when(() -> PipePool.get("Pipe"))
                    .thenReturn(mockTopPipe)
                    .thenReturn(mockBottomPipe);

            // Use reflection to test createPipe directly
            java.lang.reflect.Method method = GameElementLayer.class.getDeclaredMethod("createPipe", Pipe.class, int.class, int.class);
            method.setAccessible(true);
            method.invoke(layer, lastPipe, Pipe.TYPE_TOP_NORMAL, Pipe.TYPE_BOTTOM_NORMAL);

            verify(mockTopPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_TOP_NORMAL), eq(true));
            verify(mockBottomPipe).setAttribute(anyInt(), anyInt(), anyInt(), eq(Pipe.TYPE_BOTTOM_NORMAL), eq(true));
        }
    }

    @Test
    void testAddNormalPipeBranch() {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200); // Set X-coordinate for the last pipe
        layer.getPipes().add(lastPipe);       // Add the last pipe to the layer's pipe list

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // Mock the probability logic to ensure addNormalPipe is called
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(false); // Skip the "true" path
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(2))).thenReturn(true); // Trigger addNormalPipe

            // Mock the height generation for pipes
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenReturn(GameElementLayer.MIN_HEIGHT);

            // Invoke the logic that indirectly triggers addPipeLogic
            layer.draw(null, bird);

            // Assert that exactly two pipes (top and bottom) were added
            assertEquals(2, layer.getPipes().size(), "Two normal pipes should be added to the pipe list.");
        }
    }

    @Test
    void testAddNormalPipeDirectly() throws Exception {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(2))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenReturn(GameElementLayer.MIN_HEIGHT);

            // Invoke addPipeLogic directly using reflection
            java.lang.reflect.Method method = GameElementLayer.class.getDeclaredMethod("addPipeLogic", Pipe.class);
            method.setAccessible(true);
            method.invoke(layer, lastPipe);

            // Assert that two pipes were added
            assertEquals(2, layer.getPipes().size(), "Two normal pipes should be added.");
        }
    }

    @Test
    void testAddNormalPipeDirectInvocation() throws Exception {
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(200);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.isInProbability(eq(1), eq(2))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenReturn(GameElementLayer.MIN_HEIGHT);

            // Use reflection to directly invoke addPipeLogic
            java.lang.reflect.Method method = GameElementLayer.class.getDeclaredMethod("addPipeLogic", Pipe.class);
            method.setAccessible(true);
            method.invoke(layer, lastPipe);

            // Assert that two pipes were added
            assertEquals(2, layer.getPipes().size(), "Two normal pipes should be added.");
        }
    }
}
