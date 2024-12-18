package com.kingyu.flappybird.util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExitManagerTest {

    private SecurityManager originalSecurityManager;

    @BeforeEach
    void setUp() {
        // 保存原始的 SecurityManager
        originalSecurityManager = System.getSecurityManager();
        // 設置自定義的 SecurityManager
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkExit(int status) {
                throw new SecurityException("System.exit 被阻止");
            }

            @Override
            public void checkPermission(java.security.Permission perm) {
                // 允許其他操作
            }
        });
    }

    @AfterEach
    void tearDown() {
        // 還原原始的 SecurityManager
        System.setSecurityManager(originalSecurityManager);
    }

    @Test
    void testExitCalledWithSystemExitDisabled() {
        ExitManager.reset();
        ExitManager.setEnableSystemExit(false); // 禁用 System.exit
        ExitManager.exit(0); // 調用 exit
        assertTrue(ExitManager.isExitCalled(), "ExitManager 應該標記 exit 已被調用");
    }

    @Test
    void testReset() {
        ExitManager.reset();
        ExitManager.setEnableSystemExit(false);
        ExitManager.exit(0);
        ExitManager.reset();
        assertFalse(ExitManager.isExitCalled(), "ExitManager 應該在 reset 後標記為未調用");
    }

    @Test
    void testSystemExitEnabled() {
        ExitManager.reset();
        ExitManager.setEnableSystemExit(true); // 啟用 System.exit

        Exception exception = assertThrows(SecurityException.class, () -> ExitManager.exit(0));
        assertEquals("System.exit 被阻止", exception.getMessage());
        assertTrue(ExitManager.isExitCalled(), "ExitManager 應該標記 exit 已被調用");
    }
}
