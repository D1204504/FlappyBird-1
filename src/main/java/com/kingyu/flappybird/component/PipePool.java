package com.kingyu.flappybird.component;

import java.util.ArrayList;
import java.util.List;

import com.kingyu.flappybird.util.Constant;


public class PipePool {
	private static final List<Pipe> pool = new ArrayList<>();
	private static final List<MovingPipe> movingPool = new ArrayList<>();
	public static final int MAX_PIPE_COUNT = 30;

	static {
		for (int i = 0; i < MAX_PIPE_COUNT; i++) { // 确保至少有 MAX_PIPE_COUNT 对象
			pool.add(new Pipe());
		}
		for (int i = 0; i < MAX_PIPE_COUNT; i++) {
			movingPool.add(new MovingPipe());
		}
		System.out.println("PipePool initialized with size: " + pool.size());
		System.out.println("MovingPipePool initialized with size: " + movingPool.size());
	}

	public static void initialize() {
		for (int i = 0; i < MAX_PIPE_COUNT; i++) {
			pool.add(new Pipe());
		}
		for (int i = 0; i < MAX_PIPE_COUNT; i++) {
			movingPool.add(new MovingPipe());
		}
	}

	public static void clear() {
		pool.clear();
		movingPool.clear();
	}
	public static Pipe get(String className) {
		if ("Pipe".equals(className)) {
			if (pool.isEmpty()) {
				return null;
			}
			return pool.remove(pool.size() - 1);
		} else {
			if (movingPool.isEmpty()) {
				return null;
			}
			return movingPool.remove(movingPool.size() - 1);
		}
	}

	public static void giveBack(Pipe pipe) {
		pipe.reset(); // Reset the pipe before returning it to the pool
		if (pipe instanceof MovingPipe) {
			if (movingPool.size() < MAX_PIPE_COUNT) {
				movingPool.add((MovingPipe) pipe);
				System.out.println("Returned MovingPipe to pool, new size: " + movingPool.size());
			}
		} else {
			if (pool.size() < MAX_PIPE_COUNT) {
				pool.add(pipe);
				System.out.println("Returned Pipe to pool, new size: " + pool.size());
			}
		}

	}
}
