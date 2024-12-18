package com.kingyu.flappybird.util;

public class ExitManager {

    private static boolean exitCalled = false; // 標記是否調用了 exit
    private static ExitHandler exitHandler = status -> System.exit(status); // 默認處理器
    private static boolean enableSystemExit = true; // 控制是否允許 System.exit

    public static void exit(int status) {
        exitCalled = true; // 標記 exit 已被調用
        if (enableSystemExit) {
            exitHandler.exit(status); // 調用處理器
        }
    }

    public static void setExitHandler(ExitHandler handler) {
        exitHandler = handler; // 設置測試用的處理器
    }

    public static boolean isExitCalled() {
        return exitCalled; // 返回 exit 是否被調用
    }

    public static void reset() {
        exitCalled = false; // 重置 exitCalled 標記
    }

    public static void setEnableSystemExit(boolean enable) {
        enableSystemExit = enable; // 設置是否允許系統退出
    }

    @FunctionalInterface
    public interface ExitHandler {
        void exit(int status);
    }
}
