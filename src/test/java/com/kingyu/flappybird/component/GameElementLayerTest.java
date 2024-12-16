package com.kingyu.flappybird.component;
import com.kingyu.flappybird.util.GameUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
        Bird bird = mock(Bird.class);
        Graphics graphics = mock(Graphics.class); // 模擬 Graphics 對象

        Pipe visiblePipe = mock(Pipe.class);
        when(visiblePipe.isVisible()).thenReturn(true);

        Pipe invisiblePipe = mock(Pipe.class);
        when(invisiblePipe.isVisible()).thenReturn(false);

        layer.getPipes().add(visiblePipe);
        layer.getPipes().add(invisiblePipe);

        layer.draw(graphics, bird);

        assertEquals(1, layer.getPipes().size(), "Only visible pipes should remain after draw.");
        assertEquals(visiblePipe, layer.getPipes().toArray()[0], "The remaining pipe should be the visible one.");
    }
    @Test
    void testDrawWithEmptyPipes() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Graphics graphics = mock(Graphics.class);

        // 模擬鳥已死亡，避免觸發 pipeBornLogic
        when(bird.isDead()).thenReturn(true);

        // Act
        layer.draw(graphics, bird);

        // Assert
        assertTrue(layer.getPipes().isEmpty(), "No pipes should exist when pipes list is empty.");
    }


    @Test
    void testDrawWhenBirdIsDead() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Graphics graphics = mock(Graphics.class);

        when(bird.isDead()).thenReturn(true);

        // Act
        layer.draw(graphics, bird);

        // Assert
        verifyNoInteractions(graphics);
        assertTrue(layer.getPipes().isEmpty(), "Pipes should remain unchanged when bird is dead.");
    }

    @Test
    void testPipeBornLogicCreatesInitialPipes() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(100);

        // Act
        layer.draw(null, bird);

        // Assert
        assertEquals(2, layer.getPipes().size(), "Initial pipes should be created when pipes list is empty.");
    }
    @Test
    void testAddNormalPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        // 模擬鳥和最後一根管道的屬性
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50); // 鳥的位置
        when(lastPipe.getX()).thenReturn(100); // 最後一根管道的位置
        when(lastPipe.isInFrame()).thenReturn(true); // 確保管道在屏幕內
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // 模擬隨機邏輯
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(GameElementLayer.MIN_HEIGHT);

            // Act
            layer.draw(null, bird);

            // Assert
        }
    }
    @Test
    void testAddHoverPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        // 模擬鳥和最後一根管道的屬性
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);
        when(lastPipe.getX()).thenReturn(100);
        when(lastPipe.isInFrame()).thenReturn(true);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // 模擬隨機邏輯以觸發 addHoverPipe
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(false);
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenReturn(GameElementLayer.MIN_HEIGHT);

            // Act
            layer.draw(null, bird);

            // Assert
        }
    }

    @Test
    void testAddMovingNormalPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        // 模擬鳥和最後一根管道的屬性
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);
        when(lastPipe.getX()).thenReturn(100);
        when(lastPipe.isInFrame()).thenReturn(true);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedStatic = mockStatic(GameUtil.class)) {
            // 模擬隨機邏輯以觸發 addMovingNormalPipe
            mockedStatic.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(true);
            mockedStatic.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenReturn(GameElementLayer.MIN_HEIGHT);

            // Act
            layer.draw(null, bird);

            // Assert
        }
    }

    @Test
    void testAddMovingHoverPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        // 模擬鳥和最後一根管道的屬性
        when(bird.isDead()).thenReturn(false);
        when(bird.getBirdX()).thenReturn(50);
        when(lastPipe.getX()).thenReturn(100);
        when(lastPipe.isInFrame()).thenReturn(true);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedStatic = mockStatic(GameUtil.class)) {
            // 模擬隨機邏輯以觸發 addMovingHoverPipe
            mockedStatic.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(true);
            mockedStatic.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenReturn(GameElementLayer.MIN_HEIGHT);

            // Act
            layer.draw(null, bird);

        }
    }


    @Test
    void testIsCollideBird() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe pipe = mock(Pipe.class);

        when(bird.isDead()).thenReturn(false);
        when(pipe.getPipeRect()).thenReturn(new Rectangle(50, 50, 100, 100));
        when(bird.getBirdCollisionRect()).thenReturn(new Rectangle(75, 75, 50, 50));

        layer.getPipes().add(pipe);

        // Act
        layer.isCollideBird(bird);

        // Assert
        verify(bird).deadBirdFall();
    }

    @Test
    void testReset() {
        GameElementLayer layer = new GameElementLayer();
        Pipe pipe = mock(Pipe.class);

        layer.getPipes().add(pipe);

        // Act
        layer.reset();

        // Assert
        assertTrue(layer.getPipes().isEmpty(), "Pipes should be cleared after reset.");
        verify(pipe, never()).draw(any(Graphics.class), any(Bird.class));
    }

    @Test
    void testPipeRemovalWhenInvisible() {
        GameElementLayer layer = new GameElementLayer();
        Pipe visiblePipe = mock(Pipe.class);
        Pipe invisiblePipe = mock(Pipe.class);

        when(visiblePipe.isVisible()).thenReturn(true);
        when(invisiblePipe.isVisible()).thenReturn(false);

        layer.getPipes().add(visiblePipe);
        layer.getPipes().add(invisiblePipe);

        // Act
        layer.draw(null, mock(Bird.class));

        // Assert
        assertEquals(1, layer.getPipes().size(), "Only visible pipes should remain.");
        assertTrue(layer.getPipes().contains(visiblePipe), "Visible pipe should remain.");
    }
    @Test
    void testPipeBornLogicExceptionHandling() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            mockedGameUtil.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt()))
                    .thenThrow(new RuntimeException("Test Exception"));

        }
    }
    @Test
    void testPipeBornLogicWithProbability() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);

        when(lastPipe.getX()).thenReturn(100);
        layer.getPipes().add(lastPipe);

        try (MockedStatic<GameUtil> mockedGameUtil = mockStatic(GameUtil.class)) {
            // Simulate adding moving hover pipe
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(20))).thenReturn(true);
            mockedGameUtil.when(() -> GameUtil.isInProbability(anyInt(), eq(4))).thenReturn(true);

            layer.draw(null, bird);

        }
    }
    @Test
    void testDrawWithEmptyPipeList() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        layer.draw(null, bird);

    }
    @Test
    void testPipeBornLogicWithFullPipe() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        // Fill the pipe list to FULL_PIPE
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
    void testFullPipeBoundaryCondition() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        when(bird.isDead()).thenReturn(false);

        // Fill pipes to FULL_PIPE
        for (int i = 0; i < GameElementLayer.FULL_PIPE; i++) {
            Pipe pipe = mock(Pipe.class);
            when(pipe.isVisible()).thenReturn(true);
            layer.getPipes().add(pipe);
        }

        // Act
        layer.draw(null, bird);

        // Assert
        assertEquals(GameElementLayer.FULL_PIPE, layer.getPipes().size(), "Number of pipes should not exceed FULL_PIPE.");
    }
    @Test
    void testAddHoverPipeWithRandomLogic() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(100);
        layer.getPipes().add(lastPipe);

        // Mock random logic
        try (MockedStatic<GameUtil> mockedStatic = mockStatic(GameUtil.class)) {
            mockedStatic.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(false);
            mockedStatic.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(50);

            // Act
            layer.draw(null, bird);

            // Assert
        }
    }
    @Test
    void testAddMovingHoverPipeWithRandomLogic() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);
        Pipe lastPipe = mock(Pipe.class);
        when(lastPipe.getX()).thenReturn(100);
        layer.getPipes().add(lastPipe);

        // Mock random logic
        try (MockedStatic<GameUtil> mockedStatic = mockStatic(GameUtil.class)) {
            mockedStatic.when(() -> GameUtil.isInProbability(anyInt(), anyInt())).thenReturn(true);
            mockedStatic.when(() -> GameUtil.getRandomNumber(anyInt(), anyInt())).thenReturn(50);

            // Act
            layer.draw(null, bird);

        }
    }
    @Test
    void testDrawWithNullGraphics() {
        GameElementLayer layer = new GameElementLayer();
        Bird bird = mock(Bird.class);

        // Act
        assertDoesNotThrow(() -> layer.draw(null, bird), "Drawing with null graphics should not throw exceptions.");
    }
}
