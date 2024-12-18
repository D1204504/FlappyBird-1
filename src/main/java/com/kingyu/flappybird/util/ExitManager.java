package com.kingyu.flappybird.util;

public class ExitManager {
    private static boolean exitCalled = false; // 用來檢查是否觸發了 exit
    private static boolean enableSystemExit = true; // 控制是否執行 System.exit

    public static void exit(int status) {
        exitCalled = true; // 標記 exit 被觸發
        if (enableSystemExit) {
            System.exit(status); // 只在生產環境中執行
        }
    }

    public static boolean isExitCalled() {
        return exitCalled;
    }

    public static void reset() {
        exitCalled = false;
    }

    public static void setEnableSystemExit(boolean enable) {
        enableSystemExit = enable; // 控制是否啟用 System.exit
    }
}
