package com.kingyu.flappybird.app;

import com.kingyu.flappybird.component.GameElementLayer;
import com.kingyu.flappybird.component.Bird;
import com.kingyu.flappybird.component.GameBackground;
import com.kingyu.flappybird.component.GameForeground;
import com.kingyu.flappybird.component.WelcomeAnimation;
import com.kingyu.flappybird.util.ExitManager;
import static com.kingyu.flappybird.util.Constant.*;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * 游戏主体，管理游戏的组件和窗口绘制
 *
 * @author Kingyu
 */
public class Game extends Frame {
    private static final long serialVersionUID = 1L; // 保持版本的兼容性

    private static int gameState; // 游戏状态
    public static final int GAME_READY = 0; // 游戏未开始
    public static final int GAME_START = 1; // 游戏开始
    public static final int STATE_OVER = 2; // 游戏结束

    private GameBackground background; // 游戏背景对象
    private GameForeground foreground; // 游戏前景对象
    private Bird bird; // 小鸟对象
    private GameElementLayer gameElement; // 游戏元素对象
    private WelcomeAnimation welcomeAnimation; // 游戏未开始时对象
    private boolean silentMode; // 静默模式（测试时使用）

    // 在构造器中初始化
    public Game(boolean silentMode) {
        this.silentMode = silentMode;
        if (!silentMode) {
            initFrame(); // 初始化游戏窗口
            setVisible(true); // 窗口默认为不可见，设置为可见
        }
        initGame(); // 初始化游戏对象
    }

    // 初始化游戏窗口
    private void initFrame() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT); // 设置窗口大小
        setTitle(GAME_TITLE); // 设置窗口标题
        setLocation(FRAME_X, FRAME_Y); // 窗口初始位置
        setResizable(false); // 设置窗口大小不可变
        // 添加关闭窗口事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ExitManager.exit(0); // 使用 ExitManager
            }
        });

        addKeyListener(new BirdKeyListener()); // 添加按键监听
    }

    // 用于接收按键事件的对象的内部类
    class BirdKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();
            switch (gameState) {
                case GAME_READY:
                    if (keycode == KeyEvent.VK_SPACE) {
                        bird.birdFlap();
                        bird.birdFall();
                        setGameState(GAME_START);
                    }
                    break;
                case GAME_START:
                    if (keycode == KeyEvent.VK_SPACE) {
                        bird.birdFlap();
                        bird.birdFall();
                    }
                    break;
                case STATE_OVER:
                    if (keycode == KeyEvent.VK_SPACE) {
                        resetGame();
                    }
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            int keycode = e.getKeyChar();
            if (keycode == KeyEvent.VK_SPACE) {
                bird.keyReleased();
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    // 初始化游戏中的各个对象
   void initGame() {
        background = new GameBackground();
        gameElement = new GameElementLayer();
        foreground = new GameForeground();
        welcomeAnimation = new WelcomeAnimation();
        bird = new Bird();
        setGameState(GAME_READY);

        if (!silentMode) {
            new Thread(() -> {
                while (true) {
                    repaint();
                    try {
                        Thread.sleep(FPS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // 重置游戏
    public void resetGame() {
        setGameState(GAME_READY);
        gameElement.reset();
        bird.reset();
    }

    private final BufferedImage bufImg = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

    public void update(Graphics g) {
        Graphics bufG = bufImg.getGraphics();
        background.draw(bufG, bird);
        foreground.draw(bufG, bird);
        if (gameState == GAME_READY) {
            welcomeAnimation.draw(bufG);
        } else {
            gameElement.draw(bufG, bird);
        }
        bird.draw(bufG);
        g.drawImage(bufImg, 0, 0, null);
    }

    public static void setGameState(int gameState) {
        if (gameState != GAME_READY && gameState != GAME_START && gameState != STATE_OVER) {
            throw new IllegalStateException("Invalid game state: " + gameState);
        }
        Game.gameState = gameState;
    }

    public static int getGameState() {
        return gameState;
    }
}
