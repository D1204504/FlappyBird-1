package com.kingyu.flappybird.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PipeTest {

    @Test
    public void testPipeInitialization() {
        Pipe pipe = new Pipe();
        pipe.setAttribute(100, 200, 300, Pipe.TYPE_TOP_NORMAL, true);
        assertEquals(100, pipe.getX(), "x 坐標應該正確初始化");
        assertEquals(200, pipe.getPipeRect().y, "y 坐標應該正確初始化");
        assertTrue(pipe.isVisible(), "水管應該為可見狀態");
    }

    @Test
    public void testPipeMovement() {
        Pipe pipe = new Pipe();
        pipe.setAttribute(100, 200, 300, Pipe.TYPE_TOP_NORMAL, true);
        pipe.draw(null, new Bird()); // 模擬繪製
        assertTrue(pipe.getX() < 100, "繪製後應更新 x 坐標");
    }

    @Test
    public void testPipeOutOfFrame() {
        Pipe pipe = new Pipe();
        pipe.setAttribute(-50, 200, 300, Pipe.TYPE_TOP_NORMAL, true);
        assertFalse(pipe.isInFrame(), "水管應該已經移出視窗");
    }
}
