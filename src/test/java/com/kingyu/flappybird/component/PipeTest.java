package com.kingyu.flappybird.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.kingyu.flappybird.component.Pipe;
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
        // 測試水管初始屬性是否正確
        assertEquals(Constant.GAME_SPEED, pipe.speed);
        assertTrue(pipe.getPipeRect().width > 0);
    }

    @Test
    void testSetAttributes() {
        pipe.setAttribute(100, 200, 300, Pipe.TYPE_TOP_NORMAL, true);
        assertEquals(100, pipe.getX());
        assertEquals(200, pipe.getPipeRect().y);
        assertTrue(pipe.isVisible());
    }

    @Test
    void testVisibility() {
        pipe.setAttribute(100, 0, 100, Pipe.TYPE_TOP_NORMAL, true); // 初始化水管
        assertTrue(pipe.isVisible()); // 初始時應該可見

        // 模擬水管移動超出屏幕
        Bird bird = new Bird(); // 創建一個 Bird 對象，模擬傳入
        while (pipe.getX() >= -Pipe.PIPE_HEAD_WIDTH) {
            pipe.draw(null, bird); // 通過 draw() 方法間接調用 movement()
        }

        assertFalse(pipe.isVisible()); // 離開屏幕後應該不可見
    }


    @Test
    void testVisibilityWithDraw() {
        Pipe pipe = new Pipe();
        pipe.setAttribute(Constant.FRAME_WIDTH, 0, 100, Pipe.TYPE_TOP_NORMAL, true); // 初始化水管

        // 模擬水管移動到屏幕外部
        while (pipe.getX() >= -Pipe.PIPE_HEAD_WIDTH) {
            pipe.draw(null, new Bird()); // 通過 draw 方法觸發 movement
        }

        assertFalse(pipe.isVisible()); // 離開屏幕後應該不可見
    }
}

