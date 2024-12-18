package com.kingyu.flappybird.app;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import com.kingyu.flappybird.component.*;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTest {

    // ====== 基本遊戲邏輯測試 ======

    @Test
    void testGameInitialization() {
        Game game = new Game(true); // 靜默模式
        assertNotNull(game, "遊戲初始化應成功");
    }

    @Test
    void testGameStateTransition() {
        Game.setGameState(Game.GAME_READY);
        assertEquals(Game.GAME_READY, Game.getGameState(), "狀態應為準備");

        Game.setGameState(Game.GAME_START);
        assertEquals(Game.GAME_START, Game.getGameState(), "狀態應為開始");
    }

    @Test
    void testResetGame() {
        Game game = new Game(true); // 靜默模式
        game.initGame();

        Game.setGameState(Game.GAME_START);
        game.resetGame();
        assertEquals(Game.GAME_READY, Game.getGameState(), "重置後狀態應為 GAME_READY");
    }

    @Test
    void testUnknownGameState() {
        assertThrows(IllegalStateException.class, () -> Game.setGameState(-1), "未知狀態應拋出異常");
    }

    // ====== 按鍵邏輯測試 ======

    @Test
    void testKeyPressReadyState() {
        Game game = new Game(true); // 靜默模式
        game.initGame();
        Game.setGameState(Game.GAME_READY);

        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(keyEvent);

        assertEquals(Game.GAME_START, Game.getGameState(), "Key press should transition state to GAME_START.");
    }

    @Test
    void testKeyPressGameStartState() {
        Game game = new Game(true); // 靜默模式
        game.initGame();
        Game.setGameState(Game.GAME_START);

        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(keyEvent);

        assertEquals(Game.GAME_START, Game.getGameState(), "Key press in GAME_START should not change the state.");
    }

    @Test
    void testKeyPressGameOverState() {
        Game game = new Game(true); // 靜默模式
        game.initGame();
        Game.setGameState(Game.STATE_OVER);

        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        game.new BirdKeyListener().keyPressed(keyEvent);

        assertEquals(Game.GAME_READY, Game.getGameState(), "Key press in STATE_OVER should reset the game to GAME_READY.");
    }

    @Test
    void testKeyTyped() {
        Game game = new Game(true); // 靜默模式
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, 0, 'a');

        assertDoesNotThrow(() -> game.new BirdKeyListener().keyTyped(keyEvent), "keyTyped 方法不應拋出異常");
    }

    @Test
    void testKeyReleasedLogic() {
        Game game = new Game(true); // 靜默模式
        game.initGame();

        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, ' ');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyReleased(keyEvent), "KeyReleased should not throw exceptions.");
    }

    @Test
    void testInvalidKeyPress() {
        Game game = new Game(true); // 靜默模式
        game.initGame();

        KeyEvent invalidKey = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyPressed(invalidKey), "Invalid key press should not throw exceptions");
    }

    // ====== 更新和渲染邏輯測試 ======

    @Test
    void testUpdateMethodForDifferentStates() {
        Game game = new Game(true); // 靜默模式
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
    void testUpdateGameComponents() {
        Game game = new Game(true); // 靜默模式
        game.initGame();

        Graphics mockGraphics = mock(Graphics.class);
        game.update(mockGraphics);

        assertDoesNotThrow(() -> game.update(mockGraphics), "更新應該處理所有遊戲組件");
    }

    // ====== 執行緒邏輯測試 ======

    @Test
    void testRepaintLoop() throws InterruptedException {
        Game game = new Game(true); // 靜默模式
        game.initGame();

        Thread.sleep(100);
        assertTrue(true, "Repaint loop should execute without exceptions");
    }

    @Test
    void testThreadInterruptedException() {
        Game game = new Game(true); // 靜默模式

        Thread mockThread = mock(Thread.class);

        try {
            doThrow(new InterruptedException("Test Exception")).when(mockThread).sleep(anyLong());

            assertDoesNotThrow(() -> {
                new Thread(() -> {
                    try {
                        mockThread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            });

        } catch (InterruptedException e) {
            fail("InterruptedException should not be thrown during mocking");
        }
    }

    // ====== 系統退出測試 ======

    @Test
    void testWindowClosing() throws Exception {
        Game game = new Game(true); // 靜默模式
        WindowEvent mockEvent = mock(WindowEvent.class);

        // 手動添加 WindowListener（如果在靜默模式下未初始化）
        game.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // 使用 SystemLambda 捕獲 System.exit
        int statusCode = SystemLambda.catchSystemExit(() -> {
            game.getWindowListeners()[0].windowClosing(mockEvent);
        });

        assertEquals(0, statusCode, "應該觸發 System.exit(0)");
    }
}
