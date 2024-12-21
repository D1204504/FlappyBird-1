package com.kingyu.flappybird.component;

import java.io.*;

import com.kingyu.flappybird.util.Constant;
import com.kingyu.flappybird.util.MusicUtil;

/**
 * 游戏计时器, 使用静态内部类实现了单例模式
 *
 * @author Kingyu
 *
 */
public class ScoreCounter {

	private final String scoreFilePath;

	private ScoreCounter(String filePath) {
		this.scoreFilePath = filePath;
		bestScore = -1;
		try {
			loadBestScore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class ScoreCounterHolder {
		private static final ScoreCounter scoreCounter = new ScoreCounter(Constant.SCORE_FILE_PATH);
	}

	public static ScoreCounter getInstance() {
		return ScoreCounterHolder.scoreCounter;
	}

	// Add a factory method for testing
	public static ScoreCounter createInstanceForTesting(String filePath) {
		return new ScoreCounter(filePath);
	}

	private long score = 0; // 分数
	private long bestScore; // 最高分数

	// 装载最高纪录
	private void loadBestScore() throws Exception {
		File file = new File(scoreFilePath); // Use instance variable
		if (file.exists()) {
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			bestScore = dis.readLong();
			dis.close();
		}
	}

	public void saveScore() {
		bestScore = Math.max(bestScore, getCurrentScore());
		try {
			writeBestScoreToFile(bestScore);
		} catch (IOException e) {
			System.err.println("Failed to save score: " + e.getMessage());
		}
	}

	// Helper method to handle file writing
	protected void writeBestScoreToFile(long score) throws IOException {
		File file = new File(scoreFilePath); // Use instance variable
		try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
			dos.writeLong(score);
		}
	}



	public void score(Bird bird) {
		if (!bird.isDead()) {
			MusicUtil.playScore();
			score += 1;
		}
	}

	public long getBestScore() {
		return bestScore;
	}

	public long getCurrentScore() {
		return score;
	}

	public void reset() {
		score = 0;
	}
}
