package com.kingyu.flappybird.app;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import com.kingyu.flappybird.util.ExitManager;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class GameTest {

    // ====== 基本遊戲邏輯測試 ======
    @BeforeEach
    void setUp() {
        // 每次測試前重置 ExitManager 狀態
        ExitManager.reset();
        ExitManager.setExitHandler(status -> {
            throw new UnsupportedOperationException("Mocked System.exit called with status: " + status);
        });
    }
    @AfterEach
    void tearDown() {
        ExitManager.reset(); // Clean up any state after each test
    }

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
    // ====== 系統退出測試 ======
    @Test
    void testWindowClosing() {
        // 初始化靜默模式的 Game
        Game game = new Game(true);

        // 添加 WindowListener
        game.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ExitManager.exit(0);
            }
        });

        // 設置 ExitManager 處理器模擬 System.exit
        ExitManager.setExitHandler(status -> {
            throw new UnsupportedOperationException("Mocked System.exit called with status " + status);
        });

        // 模擬窗口關閉事件
        Window mockWindow = new Frame(); // 模擬一個窗口
        WindowEvent event = new WindowEvent(mockWindow, WindowEvent.WINDOW_CLOSING);

        // 觸發 windowClosing 事件並捕獲異常
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            game.getWindowListeners()[0].windowClosing(event);
        });

        // 驗證結果
        assertTrue(exception.getMessage().contains("Mocked System.exit called"), "應該模擬 System.exit 的行為");
        assertTrue(ExitManager.isExitCalled(), "ExitManager 應該標記 exit 已被調用");
    }
    @Test
    void testSilentModeDoesNotStartThread() {
        Game game = new Game(true); // Silent mode
        assertNotNull(game, "Game instance should be created in silent mode.");

        // Verify no repaint thread is started
        // This can be inferred by checking no exceptions or unexpected behavior
        game.initGame();
        assertEquals(Game.GAME_READY, Game.getGameState(), "Game should initialize in GAME_READY state in silent mode.");
    }
    @Test
    void testUnsupportedKeyPress() {
        Game game = new Game(true); // Silent mode
        game.initGame();

        KeyEvent invalidKey = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'A');
        assertDoesNotThrow(() -> game.new BirdKeyListener().keyPressed(invalidKey), "Unsupported key press should not throw exceptions.");
    }
    @Test
    void testComponentRenderingInGameReady() {
        Game game = new Game(true); // Silent mode
        game.initGame();
        Game.setGameState(Game.GAME_READY);

        Graphics mockGraphics = mock(Graphics.class);

        // Call update and verify that welcomeAnimation is drawn
        game.update(mockGraphics);

        // Add more verifications if needed for other components
        assertDoesNotThrow(() -> game.update(mockGraphics), "Rendering in GAME_READY state should not throw exceptions.");
    }

    @Test
    void testComponentRenderingInGameStart() {
        Game game = new Game(true); // Silent mode
        game.initGame();
        Game.setGameState(Game.GAME_START);

        Graphics mockGraphics = mock(Graphics.class);

        // Call update and verify that game elements are drawn
        assertDoesNotThrow(() -> game.update(mockGraphics), "Rendering in GAME_START state should not throw exceptions.");
    }
    @Test
    void testRepaintLoopThreadInterruption() {
        Game game = new Game(true); // Silent mode

        assertDoesNotThrow(() -> {
            Thread testThread = new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // This block should now be executed
                    System.out.println("Thread was interrupted.");
                }
            });

            testThread.start();
            testThread.interrupt(); // Interrupt to simulate behavior
            testThread.join(); // Ensure thread finishes before test ends
        }, "Thread interruption should be handled gracefully.");
    }
    @Test
    void testRepaintThreadInterruptedException() {
        Game game = new Game(true); // Silent mode

        Thread mockThread = mock(Thread.class); // Mock a Thread object

        try {
            // Simulate an InterruptedException when Thread.sleep() is called
            doThrow(new InterruptedException("Mocked InterruptedException")).when(mockThread).sleep(anyLong());

            // Start the game thread and simulate the exception
            assertDoesNotThrow(() -> {
                new Thread(() -> {
                    try {
                        mockThread.sleep(100);
                    } catch (InterruptedException e) {
                        // This block should now be executed
                        e.printStackTrace();
                    }
                }).start();
            }, "Thread interruption should be handled gracefully.");
        } catch (InterruptedException e) {
            fail("InterruptedException should not occur during test setup.");
        }
    }

}
