package com.kingyu.flappybird.app;

/**
 * 游戏入口
 *
 * @author Kingyu
 *
 */
public class App {
	public static void main(String[] args) {
		// 默認模式下不啟用靜默模式
		boolean silentMode = false;
		new Game(silentMode);
	}
}
