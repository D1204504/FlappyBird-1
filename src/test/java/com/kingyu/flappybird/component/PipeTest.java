package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.kingyu.flappybird.util.Constant;
import static org.junit.jupiter.api.Assertions.*;

class PipeTest {

    private Pipe pipe;

    @BeforeEach
    void setUp() {
        pipe = new Pipe();
    }

    @Test
    void testInitialAttributes() {
        assertEquals(Constant.GAME_SPEED, pipe.speed, "初始速度應為 GAME_SPEED");
        assertTrue(pipe.getPipeRect().width > 0, "水管碰撞矩形的寬度應大於 0");
    }

    @Test
    void testSetAttributes() {
        pipe.setAttribute(100, 200, 300, Pipe.TYPE_TOP_NORMAL, true);
        assertEquals(100, pipe.getX(), "X 坐標應為 100");
        assertEquals(200, pipe.getPipeRect().y, "碰撞矩形 Y 坐標應為 200");
        assertTrue(pipe.isVisible(), "水管應該可見");
    }

    @Test
    void testVisibility() {
        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();

        Bird bird = new Bird();
        pipe.setAttribute(100, 0, 100, Pipe.TYPE_TOP_NORMAL, true);

        while (pipe.getX() >= -Pipe.PIPE_HEAD_WIDTH) {
            pipe.draw(g, bird);
        }
        assertFalse(pipe.isVisible(), "水管移出屏幕後應不可見");
    }

}