package com.kingyu.flappybird.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExitManagerTest {

    @BeforeEach
    void setUp() {
        ExitManager.reset();
        ExitManager.setEnableSystemExit(false); // 禁止真正退出
        ExitManager.setExitHandler(status -> {
            throw new UnsupportedOperationException("Mocked System.exit called with status " + status);
        });
    }


    @Test
    void testExitCalled() {
        // 設置自定義 ExitHandler
        ExitManager.setExitHandler(status -> assertEquals(0, status, "ExitHandler 應該接收到正確的退出狀態碼"));

        // 調用 exit
        ExitManager.exit(0);

        // 驗證 exit 是否被調用
        assertTrue(ExitManager.isExitCalled(), "ExitManager 應該標記 exit 已被調用");
    }

    @Test
    void testReset() {
        // 模擬一次退出
        ExitManager.exit(0);
        assertTrue(ExitManager.isExitCalled(), "ExitManager 應該標記 exit 已被調用");

        // 測試 reset 方法
        ExitManager.reset();
        assertFalse(ExitManager.isExitCalled(), "ExitManager 應該在 reset 後標記為未調用");
    }

    @Test
    void testSetExitHandler() {
        // 設置自定義 ExitHandler
        ExitManager.setExitHandler(status -> assertEquals(1, status, "ExitHandler 應該接收到正確的退出狀態碼"));

        // 調用 exit
        ExitManager.exit(1);

        // 驗證 exit 是否被調用
        assertTrue(ExitManager.isExitCalled(), "ExitManager 應該標記 exit 已被調用");
    }

    @Test
    void testDefaultExitHandlerDoesNotThrow() {
        // 恢復默認處理器
        ExitManager.setExitHandler(status -> {
            // 模擬默認行為（System.exit），但不執行實際退出
        });

        // 調用 exit 並確保不會拋出異常
        assertDoesNotThrow(() -> ExitManager.exit(0), "默認 ExitHandler 不應拋出異常");
    }

    @Test
    void testSetEnableSystemExit() {
        // 確保 setEnableSystemExit 方法可被調用，不執行任何實際邏輯
        assertDoesNotThrow(() -> ExitManager.setEnableSystemExit(false), "setEnableSystemExit 方法不應拋出異常");

        // 測試啟用 System.exit
        ExitManager.setEnableSystemExit(true);
        ExitManager.setExitHandler(status -> {
            // 不進行真正退出的模擬行為
            assertEquals(1, status, "應該正確傳遞退出狀態碼");
        });
        ExitManager.exit(1);
        assertTrue(ExitManager.isExitCalled(), "ExitManager 應該標記 exit 已被調用");
    }
}
