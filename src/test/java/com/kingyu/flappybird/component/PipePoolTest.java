package com.kingyu.flappybird.component;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
class PipePoolTest {
    @BeforeEach
    void resetPool() {
        // Clear and reinitialize the pipe pool
        PipePool.clear();
        PipePool.initialize();
    }
    @Test
    void testPoolInitialization() {
        int initialPoolSize = PipePool.MAX_PIPE_COUNT;
        for (int i = 0; i < initialPoolSize; i++) {
            Pipe pipe = PipePool.get("Pipe");
            assertNotNull(pipe, "應該能從池中獲取對象");
        }
        assertNull(PipePool.get("Pipe"), "超過初始化數量後應返回 null");
    }
    @Test
    void testClear() {
        // Test that the pool clears the items correctly
        PipePool.clear();
        assertNull(PipePool.get("Pipe"), "Pool should be empty after clear");
    }
    @Test
    void testGiveBackPipe() {
        Pipe pipe = PipePool.get("Pipe");
        assertNotNull(pipe, "應該能從池中獲取對象");

        pipe.setVisible(false); // Simulate the pipe being used
        assertFalse(pipe.isVisible(), "水管應該是不可見的");

        PipePool.giveBack(pipe); // Return it to the pool

        assertTrue(pipe.isVisible(), "歸還後的對象應設置為可見");

        Pipe reusedPipe = PipePool.get("Pipe");
        assertSame(pipe, reusedPipe, "歸還的對象應再次被重複利用");
    }


    @Test
    void testExtremePipeCount() {
        for (int i = 0; i < PipePool.MAX_PIPE_COUNT; i++) {
            Pipe pipe = PipePool.get("Pipe");
            assertNotNull(pipe, "應該能從池中獲取對象");
        }

        Pipe extraPipe = PipePool.get("Pipe");
        assertNull(extraPipe, "超過容量後應返回 null");
    }


    @Test
    void testPipePool() {
        Pipe pipe = PipePool.get("Pipe");
        assertNotNull(pipe, "應該能從池中獲取對象");

        pipe.setVisible(false); // 模擬對象被使用後狀態變更
        PipePool.giveBack(pipe);

        assertTrue(pipe.isVisible(), "歸還後的對象應設置為可見");

        Pipe reusedPipe = PipePool.get("Pipe");
        assertSame(pipe, reusedPipe, "歸還的對象應再次被重複利用");
    }
    @Test
    void testGetAndGiveBackMovingPipe() {
        // Test that MovingPipe works correctly in the pool
        MovingPipe movingPipe = (MovingPipe) PipePool.get("MovingPipe");
        assertNotNull(movingPipe, "Should be able to get a MovingPipe from the pool");

        movingPipe.setVisible(false); // Simulate the moving pipe being used
        assertFalse(movingPipe.isVisible(), "MovingPipe should be invisible when used");

        PipePool.giveBack(movingPipe); // Return it to the pool
        assertTrue(movingPipe.isVisible(), "Returned MovingPipe should be set to visible");

        MovingPipe reusedMovingPipe = (MovingPipe) PipePool.get("MovingPipe");
        assertSame(movingPipe, reusedMovingPipe, "Returned MovingPipe should be reused");
    }

    @Test
    void testGiveBackMovingPipe() {
        // Test returning a MovingPipe to the pool
        MovingPipe movingPipe = (MovingPipe) PipePool.get("MovingPipe");
        assertNotNull(movingPipe, "Should be able to get a MovingPipe from the pool");

        movingPipe.setVisible(false); // Simulate the moving pipe being used
        assertFalse(movingPipe.isVisible(), "MovingPipe should be invisible when used");

        PipePool.giveBack(movingPipe); // Return it to the pool

        assertTrue(movingPipe.isVisible(), "Returned MovingPipe should be set to visible");

        MovingPipe reusedMovingPipe = (MovingPipe) PipePool.get("MovingPipe");
        assertSame(movingPipe, reusedMovingPipe, "Returned MovingPipe should be reused");
    }
    @Test
    void testPipePoolOverflow() {
        for (int i = 0; i < PipePool.MAX_PIPE_COUNT; i++) {
            Pipe pipe = PipePool.get("Pipe");
            assertNotNull(pipe, "應該能從池中獲取對象");
        }
        assertNull(PipePool.get("Pipe"), "超過最大容量後應返回 null");
    }

}
