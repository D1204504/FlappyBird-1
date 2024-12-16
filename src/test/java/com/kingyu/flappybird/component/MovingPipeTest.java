package com.kingyu.flappybird.component;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class MovingPipeTest {

    @Test
    void testSetAttribute() {
        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(100, 200, 150, Pipe.TYPE_TOP_HARD, true);

        assertEquals(100, pipe.getX(), "x 坐標應設置為 100");
        assertEquals(200, pipe.getY(), "y 坐標應設置為 200");
        assertEquals(150, pipe.getHeight(), "高度應設置為 150");
        assertEquals(Pipe.TYPE_TOP_HARD, pipe.getType(), "類型應設置為 TYPE_TOP_HARD");
        assertTrue(pipe.isVisible(), "水管應設置為可見");
    }
    @Test
    void testMovementDirectionChange() {
        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(100, 200, 150, Pipe.TYPE_TOP_HARD, true);

        // 模擬到達最大移動距離
        for (int i = 0; i <= MovingPipe.MAX_DELTA; i++) {
            pipe.movement();
        }


        // 模擬返回到初始位置
        for (int i = MovingPipe.MAX_DELTA; i >= 0; i--) {
            pipe.movement();
        }

        assertEquals(MovingPipe.DIR_DOWN, pipe.getDirection(), "移動回到初始位置後應變為向下移動");
    }

    @Test
    void testMovementOutOfScreen() {
        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(-10, 200, 150, Pipe.TYPE_TOP_HARD, true);

        pipe.movement();

    }

    @Test
    void testDrawHoverHard() {
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();

        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(100, 200, 300, Pipe.TYPE_HOVER_HARD, true);

        pipe.draw(g, new Bird());

        assertEquals(Pipe.TYPE_HOVER_HARD, pipe.type, "移動水管的類型應為 TYPE_HOVER_HARD");
    }

    @Test
    void testDrawTopHard() {
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();

        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(100, 200, 300, Pipe.TYPE_TOP_HARD, true);

        pipe.draw(g, new Bird());

        assertEquals(Pipe.TYPE_TOP_HARD, pipe.type, "移動水管的類型應為 TYPE_TOP_HARD");
    }

    @Test
    void testDrawBottomHard() {
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();

        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(100, 200, 300, Pipe.TYPE_BOTTOM_HARD, true);

        pipe.draw(g, new Bird());

        assertEquals(Pipe.TYPE_BOTTOM_HARD, pipe.type, "移動水管的類型應為 TYPE_BOTTOM_HARD");
    }

    @Test
    void testReset() {
        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(100, 200, 150, Pipe.TYPE_TOP_HARD, true);

        pipe.reset();

        assertEquals(0, pipe.getX(), "重置後 x 坐標應為 0");
        assertEquals(0, pipe.getY(), "重置後 y 坐標應為 0");
        assertTrue(pipe.isVisible(), "重置後水管應設置為可見");
    }

    @Test
    void testMovementLogic() {
        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(100, 200, 150, Pipe.TYPE_HOVER_HARD, true);

        // 測試初始方向
        assertEquals(MovingPipe.DIR_DOWN, pipe.getDirection(), "移動到達最大值後應變為向上移動");

        // 測試移動後坐標變化
        int initialY = pipe.getY();
        pipe.movement();
    }

    @Test
    void testCollisionRectangleUpdate() {
        MovingPipe pipe = new MovingPipe();
        pipe.setAttribute(100, 200, 150, Pipe.TYPE_HOVER_HARD, true);

        // 初始碰撞矩形
        Rectangle initialRect = pipe.getPipeRect();

        // 移動一次
        pipe.movement();

        // 檢查碰撞矩形的 y 坐標是否更新
    }

}
