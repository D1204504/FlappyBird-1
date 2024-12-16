package com.kingyu.flappybird.component;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.kingyu.flappybird.util.Constant;
import com.kingyu.flappybird.util.GameUtil;

/**
 * 前景层，目前管理云朵的生成逻辑并绘制容器中的云朵
 *
 * @author Kingyu
 */
public class GameForeground {
    private final List<Cloud> clouds; // 云朵的容器
    private final BufferedImage[] cloudImages; // 图片资源
    private long time; // 控制云的逻辑运算周期
    private int speed;
    public static final int CLOUD_INTERVAL = 100; // 云朵刷新的逻辑运算周期

    public GameForeground() {
        clouds = new ArrayList<>();
        // 读入图片资源
        cloudImages = new BufferedImage[Constant.CLOUD_IMAGE_COUNT];
        for (int i = 0; i < Constant.CLOUD_IMAGE_COUNT; i++) {
            cloudImages[i] = GameUtil.loadBufferedImage(Constant.CLOUDS_IMG_PATH[i]);
        }
        time = System.currentTimeMillis();
        speed = Constant.GAME_SPEED;// 获取当前时间，用于控制云的逻辑运算周期
    }

    // 绘制方法
    public void draw(Graphics g, Bird bird) {
        cloudBornLogic();
        for (Cloud cloud : clouds) {
            cloud.draw(g, bird);
        }
    }

    // 云朵的控制逻辑
    private void cloudBornLogic() {
        // 每 100ms 运算一次
        if (System.currentTimeMillis() - time > CLOUD_INTERVAL) {
            time = System.currentTimeMillis(); // 重置 time
            // 如果屏幕的云朵数量小于允许的最大数量，根据给定的概率随机添加云朵
            if (clouds.size() < Constant.MAX_CLOUD_COUNT) {
                try {
                    if (GameUtil.isInProbability(Constant.CLOUD_BORN_PERCENT, 100)) {
                        int index = GameUtil.getRandomNumber(0, Constant.CLOUD_IMAGE_COUNT); // 随机选取云朵图片
                        int x = Constant.FRAME_WIDTH; // 云朵初始位置 x
                        int y = GameUtil.getRandomNumber(Constant.TOP_BAR_HEIGHT, Constant.FRAME_HEIGHT / 3); // y 坐标
                        clouds.add(new Cloud(cloudImages[index], x, y));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 移除飞出屏幕的云朵
            clouds.removeIf(Cloud::isOutFrame);
        }
    }

    // Getter for clouds list (for testing)
    public List<Cloud> getClouds() {
        return clouds;
    }

    // Setter for time (for testing)
    public void setTime(long time) {
        this.time = time;
    }
    public void setSpeed(int speed) {
        this.speed = speed; // Set the foreground's speed
        for (Cloud cloud : clouds) {
            cloud.setSpeed(speed); // Apply speed adjustment to clouds
        }
    }

    public int getSpeed() {
        return speed;
    }
}

