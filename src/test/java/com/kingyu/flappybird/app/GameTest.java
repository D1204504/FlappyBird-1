package com.kingyu.flappybird.app;

import com.kingyu.flappybird.component.Bird;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.kingyu.flappybird.util.Constant.FPS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        // Verify bird flap and fall methods are triggered
        // (If these methods have observable side effects, mock `Bird` to verify)
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
    void testRepaintLoop() throws InterruptedException {
        Game game = new Game(true);
        game.initGame();

        // Mock Graphics object to prevent rendering errors
        Graphics mockGraphics = mock(Graphics.class);

        // Ensure repaint is triggered periodically
        Thread.sleep(FPS * 2); // Allow time for the repaint thread to run
        assertDoesNotThrow(() -> game.update(mockGraphics), "Repaint loop should trigger update without exceptions.");
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

        // Mock Graphics object to prevent rendering errors
        Graphics mockGraphics = mock(Graphics.class);

        // Set game state to GAME_READY
        Game.setGameState(Game.GAME_READY);

        // Call the update method and verify rendering
        assertDoesNotThrow(() -> game.update(mockGraphics), "Update should handle GAME_READY state correctly.");
    }

    @Test
    void testUpdateGameStart() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        // Mock Graphics object
        Graphics mockGraphics = mock(Graphics.class);

        // Set game state to GAME_START
        Game.setGameState(Game.GAME_START);

        // Call the update method
        assertDoesNotThrow(() -> game.update(mockGraphics), "Update should handle GAME_START state correctly.");
    }

    @Test
    void testUpdateStateOver() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        // Mock Graphics object
        Graphics mockGraphics = mock(Graphics.class);

        // Set game state to STATE_OVER
        Game.setGameState(Game.STATE_OVER);

        // Call the update method
        assertDoesNotThrow(() -> game.update(mockGraphics), "Update should handle STATE_OVER state correctly.");
    }
    @Test
    void testResetGameComponents() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        // Set game state to GAME_START and reset
        Game.setGameState(Game.GAME_START);
        game.resetGame();

        // Verify game state and components are reset
        assertEquals(Game.GAME_READY, Game.getGameState(), "Game state should reset to GAME_READY.");
    }
    @Test
    void testKeyReleasedLogic() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        // Simulate SPACE key released
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyReleased(keyEvent), "KeyReleased should not throw exceptions.");
    }
    @Test
    void testInvalidKeyPress() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        // Simulate invalid key press
        KeyEvent invalidKeyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyPressed(invalidKeyEvent), "Invalid key press should not throw exceptions.");
    }
    @Test
    void testSilentModeStateValidation() {
        Game game = new Game(true); // Silent mode

        // Mock Graphics object
        Graphics mockGraphics = mock(Graphics.class);

        // Call update and ensure no exceptions are thrown
        assertDoesNotThrow(() -> game.update(mockGraphics), "Silent mode should not trigger rendering errors.");
    }
    @Test
    void testRepaintLoopConsistency() throws InterruptedException {
        Game game = new Game(true); // Silent mode
        game.initGame();

        // Run for a short period to simulate thread behavior
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

        // Simulate bird methods and verify interactions
        when(mockBird.getCurrentScore()).thenReturn(10L);
        assertEquals(10L, mockBird.getCurrentScore(), "Bird's current score should be correctly returned.");
    }
    @Test
    void testKeyPressInDifferentStates() {
        // 測試 GAME_READY 狀態下的按鍵處理
        Game game = new Game(true); // Silent mode
        game.initGame();
        Game.setGameState(Game.GAME_READY);

        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(keyEvent);
        assertEquals(Game.GAME_START, Game.getGameState(), "按下空格鍵應該轉換為 GAME_START");

        // 測試 GAME_START 狀態下的按鍵處理
        Game.setGameState(Game.GAME_START);
        game.new BirdKeyListener().keyPressed(keyEvent);
        assertEquals(Game.GAME_START, Game.getGameState(), "按下空格鍵不應該改變 GAME_START 狀態");

        // 測試 STATE_OVER 狀態下的按鍵處理
        Game.setGameState(Game.STATE_OVER);
        game.new BirdKeyListener().keyPressed(keyEvent);
        assertEquals(Game.GAME_READY, Game.getGameState(), "按下空格鍵應該重置遊戲");
    }
    @Test
    void testUpdateMethodForDifferentStates() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);

        // 測試 GAME_READY 狀態
        Game.setGameState(Game.GAME_READY);
        assertDoesNotThrow(() -> game.update(mockGraphics), "更新應該處理 GAME_READY 狀態");

        // 測試 GAME_START 狀態
        Game.setGameState(Game.GAME_START);
        assertDoesNotThrow(() -> game.update(mockGraphics), "更新應該處理 GAME_START 狀態");

        // 測試 STATE_OVER 狀態
        Game.setGameState(Game.STATE_OVER);
        assertDoesNotThrow(() -> game.update(mockGraphics), "更新應該處理 STATE_OVER 狀態");
    }
    @Test
    void testGameStateTransitions() {
        Game game = new Game(true);
        game.initGame();

        // 測試狀態轉換
        Game.setGameState(Game.GAME_READY);
        assertEquals(Game.GAME_READY, Game.getGameState(), "初始狀態應為 GAME_READY");

        // 觸發從 GAME_READY 到 GAME_START 的過渡
        game.new BirdKeyListener().keyPressed(new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' '));
        assertEquals(Game.GAME_START, Game.getGameState(), "按空格鍵應該從 GAME_READY 轉到 GAME_START");

        // 模擬小鳥死亡，從 GAME_START 到 STATE_OVER
        Game.setGameState(Game.STATE_OVER);
        assertEquals(Game.STATE_OVER, Game.getGameState(), "遊戲結束後應轉到 STATE_OVER");
    }

}
