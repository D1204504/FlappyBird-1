package com.kingyu.flappybird.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}
