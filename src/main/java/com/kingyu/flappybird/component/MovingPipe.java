package com.kingyu.flappybird.component;

import java.awt.Graphics;

import com.kingyu.flappybird.util.Constant;

public class MovingPipe extends Pipe {

    private int dealtY; // 移动水管的坐标
    public static final int MAX_DELTA = 50; // 最大移动距离
    private int direction;
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;

    // 添加 getter 方法
   public int getDirection() {
        return this.direction;
    }

    public int getY() {
        return this.y;
    }

    public int getHeight() {
        return this.height;
    }

    public int getType() {
        return this.type;
    }

    // 重置方法
    public void reset() {
        this.x = 0; // 重置位置
        this.y = 0;
        this.setVisible(true); // 重置为可见
    }

    // 构造器
    public MovingPipe() {
        super();
    }

    // 设置水管参数
    public void setAttribute(int x, int y, int height, int type, boolean visible) {
        super.setAttribute(x, y, height, type, visible);
        dealtY = 0;
        direction = DIR_DOWN;
        if (type == TYPE_TOP_HARD) {
            direction = DIR_UP;
        }
    }

    // 绘制方法
    public void draw(Graphics g, Bird bird) {
        switch (type) {
            case TYPE_HOVER_HARD:
                drawHoverHard(g);
                break;
            case TYPE_TOP_HARD:
                drawTopHard(g);
                break;
            case TYPE_BOTTOM_HARD:
                drawBottomHard(g);
                break;
        }
        // 鸟死后水管停止移动
        if (bird.isDead()) {
            return;
        }
        movement();
    }

    // 绘制移动的悬浮水管
    private void drawHoverHard(Graphics g) {
        int count = (height - 2 * PIPE_HEAD_HEIGHT) / PIPE_HEIGHT + 1;
        g.drawImage(imgs[2], x - ((PIPE_HEAD_WIDTH - width) >> 1), y + dealtY, null);
        for (int i = 0; i < count; i++) {
            g.drawImage(imgs[0], x, y + dealtY + i * PIPE_HEIGHT + PIPE_HEAD_HEIGHT, null);
        }
        int y = this.y + height - PIPE_HEAD_HEIGHT;
        g.drawImage(imgs[1], x - ((PIPE_HEAD_WIDTH - width) >> 1), y + dealtY, null);
    }

    // 绘制从上往下的移动水管
    private void drawTopHard(Graphics g) {
        int count = (height - PIPE_HEAD_HEIGHT) / PIPE_HEIGHT + 1;
        for (int i = 0; i < count; i++) {
            g.drawImage(imgs[0], x, y + dealtY + i * PIPE_HEIGHT, null);
        }
        g.drawImage(imgs[1], x - ((PIPE_HEAD_WIDTH - width) >> 1),
                height - Constant.TOP_PIPE_LENGTHENING - PIPE_HEAD_HEIGHT + dealtY, null);
    }

    // 绘制从下往上的移动水管
    private void drawBottomHard(Graphics g) {
        int count = (height - PIPE_HEAD_HEIGHT) / PIPE_HEIGHT + 1;
        for (int i = 0; i < count; i++) {
            g.drawImage(imgs[0], x, Constant.FRAME_HEIGHT - PIPE_HEIGHT - i * PIPE_HEIGHT + dealtY, null);
        }
        g.drawImage(imgs[2], x - ((PIPE_HEAD_WIDTH - width) >> 1), Constant.FRAME_HEIGHT - height + dealtY, null);
    }

    // 可动水管的运动逻辑
    public void movement() {
        x -= speed;
        pipeRect.x -= speed;
        if (x < -PIPE_HEAD_WIDTH) { // 檢查是否超出屏幕
            visible = false;
        }

        if (direction == DIR_DOWN) {
            dealtY++;
            if (dealtY >= MAX_DELTA) { // 應該使用 >= 而非 >
                direction = DIR_UP;
            }
        } else {
            dealtY--;
            if (dealtY <= 0) { // 應該使用 <= 而非 <
                direction = DIR_DOWN;
            }
        }
        pipeRect.y = this.y + dealtY; // 更新碰撞矩形的 y 坐標
    }

}
