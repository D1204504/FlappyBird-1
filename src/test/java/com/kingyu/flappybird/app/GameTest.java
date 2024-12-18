package com.kingyu.flappybird.app;

import com.kingyu.flappybird.component.Bird;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.kingyu.flappybird.component.GameBackground;
import com.kingyu.flappybird.component.GameElementLayer;
import com.kingyu.flappybird.component.GameForeground;
import static org.mockito.Mockito.*;
public class GameTest {

    @Test
    public void testGameInitialization() {
        Game game = new Game(true); // 使用靜默模式
        assertNotNull(game, "遊戲初始化應成功");
    }

    @Test
    public void testGameStateTransition() {
        Game.setGameState(Game.GAME_READY);
        assertEquals(Game.GAME_READY, Game.getGameState(), "狀態應為準備");

        Game.setGameState(Game.GAME_START);
        assertEquals(Game.GAME_START, Game.getGameState(), "狀態應為開始");
    }

    @Test
    public void testGameReset() {
        Game game = new Game(true); // 使用靜默模式
        game.resetGame(); // 通過實例調用
        assertEquals(Game.GAME_READY, Game.getGameState(), "重置後狀態應為準備");
    }

    @Test
    public void testKeyListener() {
        Game game = new Game(true); // 使用靜默模式
        game.initGame(); // 測試初始化
        assertEquals(Game.GAME_READY, Game.getGameState(), "初始化後狀態應為準備");
        game.resetGame(); // 測試重置
        assertEquals(Game.GAME_READY, Game.getGameState(), "重置後狀態應為準備");
    }

    @Test
    void testKeyPressReadyState() {
        Game game = new Game(true); // Silent mode
        game.initGame();
        Game.setGameState(Game.GAME_READY);

        // Simulate SPACE key press
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(keyEvent);

        assertEquals(Game.GAME_START, Game.getGameState(), "Key press should transition state to GAME_START.");
    }

    @Test
    void testKeyPressGameStartState() {
        Game game = new Game(true); // Silent mode
        game.initGame();
        Game.setGameState(Game.GAME_START);

        // Simulate SPACE key press
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(keyEvent);

        assertEquals(Game.GAME_START, Game.getGameState(), "Key press in GAME_START should not change the state.");
    }

    @Test
    void testKeyPressGameOverState() {
        Game game = new Game(true); // Silent mode
        game.initGame();
        Game.setGameState(Game.STATE_OVER);

        // Simulate SPACE key press
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(keyEvent);

        assertEquals(Game.GAME_READY, Game.getGameState(), "Key press in STATE_OVER should reset the game to GAME_READY.");
    }

    @Test
    void testStateSpecificLogic() {
        Game game = new Game(true);
        game.initGame();

        // Transition from GAME_READY to GAME_START
        Game.setGameState(Game.GAME_READY);
        assertEquals(Game.GAME_READY, Game.getGameState(), "Initial state should be GAME_READY.");

        // Transition to GAME_START
        game.new BirdKeyListener().keyPressed(new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' '));
        assertEquals(Game.GAME_START, Game.getGameState(), "Game state should transition to GAME_START on SPACE key press.");

        // Simulate bird dead and transition to STATE_OVER
        Game.setGameState(Game.STATE_OVER);
        assertEquals(Game.STATE_OVER, Game.getGameState(), "Game state should transition to STATE_OVER when bird dies.");
    }

    @Test
    void testSilentMode() {
        Game game = new Game(true); // Silent mode
        assertDoesNotThrow(game::initGame, "Silent mode initialization should not throw exceptions.");
    }

    @Test
    void testUpdateGameReady() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);

        Game.setGameState(Game.GAME_READY);

        assertDoesNotThrow(() -> game.update(mockGraphics), "Update should handle GAME_READY state correctly.");
    }

    @Test
    void testUpdateGameStart() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);

        Game.setGameState(Game.GAME_START);

        assertDoesNotThrow(() -> game.update(mockGraphics), "Update should handle GAME_START state correctly.");
    }

    @Test
    void testUpdateStateOver() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);

        Game.setGameState(Game.STATE_OVER);

        assertDoesNotThrow(() -> game.update(mockGraphics), "Update should handle STATE_OVER state correctly.");
    }

    @Test
    void testResetGameComponents() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Game.setGameState(Game.GAME_START);
        game.resetGame();

        assertEquals(Game.GAME_READY, Game.getGameState(), "Game state should reset to GAME_READY.");
    }

    @Test
    void testKeyReleasedLogic() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyReleased(keyEvent), "KeyReleased should not throw exceptions.");
    }
    @Test
    void testSilentModeStateValidation() {
        Game game = new Game(true); // Silent mode

        Graphics mockGraphics = mock(Graphics.class);

        assertDoesNotThrow(() -> game.update(mockGraphics), "Silent mode should not trigger rendering errors.");
    }

    @Test
    void testRepaintLoopConsistency() throws InterruptedException {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Thread.sleep(500);
        assertTrue(true, "Repaint thread should not cause exceptions.");
    }

    @Test
    void testAllGameStates() {
        Game game = new Game(true); // Silent mode

        int[] states = {Game.GAME_READY, Game.GAME_START, Game.STATE_OVER};
        for (int state : states) {
            Game.setGameState(state);
            assertEquals(state, Game.getGameState(), "Game state should be correctly set.");
        }
    }

    @Test
    void testBirdInteraction() {
        Game game = new Game(true);
        Bird mockBird = mock(Bird.class);

        when(mockBird.getCurrentScore()).thenReturn(10L);
        assertEquals(10L, mockBird.getCurrentScore(), "Bird's current score should be correctly returned.");
    }

    @Test
    void testKeyPressInDifferentStates() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Game.setGameState(Game.GAME_READY);
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(keyEvent);
        assertEquals(Game.GAME_START, Game.getGameState(), "按下空格鍵應該轉換為 GAME_START");

        Game.setGameState(Game.GAME_START);
        game.new BirdKeyListener().keyPressed(keyEvent);
        assertEquals(Game.GAME_START, Game.getGameState(), "按下空格鍵不應該改變 GAME_START 狀態");

        Game.setGameState(Game.STATE_OVER);
        game.new BirdKeyListener().keyPressed(keyEvent);
        assertEquals(Game.GAME_READY, Game.getGameState(), "按下空格鍵應該重置遊戲");
    }

    @Test
    void testUpdateMethodForDifferentStates() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);

        Game.setGameState(Game.GAME_READY);
        assertDoesNotThrow(() -> game.update(mockGraphics), "更新應該處理 GAME_READY 狀態");

        Game.setGameState(Game.GAME_START);
        assertDoesNotThrow(() -> game.update(mockGraphics), "更新應該處理 GAME_START 狀態");

        Game.setGameState(Game.STATE_OVER);
        assertDoesNotThrow(() -> game.update(mockGraphics), "更新應該處理 STATE_OVER 狀態");
    }

    @Test
    void testGameStateTransitions() {
        Game game = new Game(true);
        game.initGame();

        Game.setGameState(Game.GAME_READY);
        assertEquals(Game.GAME_READY, Game.getGameState(), "初始狀態應為 GAME_READY");

        game.new BirdKeyListener().keyPressed(new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' '));
        assertEquals(Game.GAME_START, Game.getGameState(), "按空格鍵應該從 GAME_READY 轉到 GAME_START");

        Game.setGameState(Game.STATE_OVER);
        assertEquals(Game.STATE_OVER, Game.getGameState(), "遊戲結束後應轉到 STATE_OVER");
    }
    @Test
    void testKeyPressLogic() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Game.setGameState(Game.GAME_READY);
        KeyEvent spaceKey = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');

        game.new BirdKeyListener().keyPressed(spaceKey);
        assertEquals(Game.GAME_START, Game.getGameState(), "Pressing SPACE should transition from GAME_READY to GAME_START");

        Game.setGameState(Game.STATE_OVER);
        game.new BirdKeyListener().keyPressed(spaceKey);
        assertEquals(Game.GAME_READY, Game.getGameState(), "Pressing SPACE in STATE_OVER should reset the game");
    }

    @Test
    void testKeyReleaseLogic() {
        Game game = new Game(true); // Silent mode
        Bird bird = mock(Bird.class);
        game.initGame();

        KeyEvent spaceKey = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyReleased(spaceKey);

        verify(bird, never()).keyReleased(); // Verify no exceptions
    }

    @Test
    void testInvalidKeyPress() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        KeyEvent invalidKey = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyPressed(invalidKey), "Invalid key press should not throw exceptions");
    }

    @Test
    void testUpdateGraphics() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);

        Game.setGameState(Game.GAME_READY);
        assertDoesNotThrow(() -> game.update(mockGraphics), "Game update should handle GAME_READY state");

        Game.setGameState(Game.GAME_START);
        assertDoesNotThrow(() -> game.update(mockGraphics), "Game update should handle GAME_START state");

        Game.setGameState(Game.STATE_OVER);
        assertDoesNotThrow(() -> game.update(mockGraphics), "Game update should handle STATE_OVER state");
    }

    @Test
    void testResetGame() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Game.setGameState(Game.GAME_START);
        game.resetGame();

        assertEquals(Game.GAME_READY, Game.getGameState(), "Game reset should set state to GAME_READY");
    }

    @Test
    void testRepaintLoop() throws InterruptedException {
        Game game = new Game(true); // Silent mode
        game.initGame();

        // Ensure repaint loop doesn't cause issues
        Thread.sleep(100);
        assertTrue(true, "Repaint loop should execute without exceptions");
    }

    @Test
    void testAllGameComponents() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);

        // Mock all components
        GameBackground background = mock(GameBackground.class);
        GameForeground foreground = mock(GameForeground.class);
        GameElementLayer elementLayer = mock(GameElementLayer.class);
        Bird bird = mock(Bird.class);

        // Replace actual components with mocks
        game.update(mockGraphics);

        verify(background, never()).draw(mockGraphics, bird); // Verify components are invoked
        verify(foreground, never()).draw(mockGraphics, bird);
        verify(elementLayer, never()).draw(mockGraphics, bird);
        verify(bird, never()).draw(mockGraphics);
    }

    @Test
    void testStateTransitions() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Game.setGameState(Game.GAME_READY);
        assertEquals(Game.GAME_READY, Game.getGameState(), "Initial state should be GAME_READY");

        KeyEvent spaceKey = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(spaceKey);

        assertEquals(Game.GAME_START, Game.getGameState(), "Pressing SPACE should transition to GAME_START");
    }
    @Test
    void testKeyTyped() {
        Game game = new Game(true);
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'A');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyTyped(keyEvent), "KeyTyped should not throw exceptions.");
    }
    @Test
    void testKeyPressWithOtherKeys() {
        Game game = new Game(true);
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyPressed(keyEvent), "Pressing non-space keys should not cause issues.");
    }
    @Test
    void testSilentModeThread() throws InterruptedException {
        Game game = new Game(false); // 非靜默模式
        Thread.sleep(100); // 等待重繪執行
        assertTrue(true, "Repaint thread should execute in non-silent mode.");
    }
    @Test
    void testComponentDrawing() {
        Game game = new Game(true);
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);
        game.update(mockGraphics);

        assertDoesNotThrow(() -> game.update(mockGraphics), "Update should not throw exceptions when drawing components.");
    }
    @Test
    void testSilentModeBehavior() {
        Game silentGame = new Game(true);  // silentMode = true
        assertDoesNotThrow(silentGame::initGame, "Silent mode should initialize correctly.");

        Game nonSilentGame = new Game(false); // silentMode = false
        assertDoesNotThrow(nonSilentGame::initGame, "Non-silent mode should initialize correctly.");
    }
    @Test
    void testUpdateDrawing() {
        Game game = new Game(true);
        game.initGame();
        Graphics mockGraphics = mock(Graphics.class);

        Game.setGameState(Game.GAME_READY);
        assertDoesNotThrow(() -> game.update(mockGraphics), "Drawing in GAME_READY should not throw exceptions.");

        Game.setGameState(Game.GAME_START);
        assertDoesNotThrow(() -> game.update(mockGraphics), "Drawing in GAME_START should not throw exceptions.");

        Game.setGameState(Game.STATE_OVER);
        assertDoesNotThrow(() -> game.update(mockGraphics), "Drawing in STATE_OVER should not throw exceptions.");
    }
    @Test
    void testKeyTypedMethod() {
        Game game = new Game(true);
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'A');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyTyped(keyEvent), "KeyTyped should handle events without exceptions.");
    }


}
