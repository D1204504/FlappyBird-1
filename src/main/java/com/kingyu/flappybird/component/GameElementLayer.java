package com.kingyu.flappybird.component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.kingyu.flappybird.util.Constant;
import com.kingyu.flappybird.util.GameUtil;

public class GameElementLayer {
    private final List<Pipe> pipes;

    public GameElementLayer() {
        pipes = new ArrayList<>();
    }

    public static final int VERTICAL_INTERVAL = Constant.FRAME_HEIGHT / 5;
    public static final int HORIZONTAL_INTERVAL = Constant.FRAME_HEIGHT >> 2;
    public static final int MIN_HEIGHT = Constant.FRAME_HEIGHT >> 3;
    public static final int MAX_HEIGHT = ((Constant.FRAME_HEIGHT) >> 3) * 5;
    public static final int FULL_PIPE = (Constant.FRAME_WIDTH / (Pipe.PIPE_HEAD_WIDTH + HORIZONTAL_INTERVAL) + 2) * 2;

    public void draw(Graphics g, Bird bird) {
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            if (pipe.isVisible()) {
                pipe.draw(g, bird);
            } else {
                Pipe remove = pipes.remove(i);
                PipePool.giveBack(remove);
                i--;
            }
        }
        // Ensure the number of pipes does not exceed FULL_PIPE
        while (pipes.size() > FULL_PIPE) {
            Pipe excessPipe = pipes.remove(0);
            PipePool.giveBack(excessPipe);
        }

        isCollideBird(bird);
        pipeBornLogic(bird);
    }

    private void pipeBornLogic(Bird bird) {
        System.out.println("pipeBornLogic invoked. pipes.size=" + pipes.size() + ", bird.isDead=" + bird.isDead());
        if (bird.isDead()) return;

        if (pipes.isEmpty()) {
            generateInitialPipes();
        } else {
            Pipe lastPipe = pipes.get(pipes.size() - 1);
            int currentDistance = lastPipe.getX() - bird.getBirdX() + Bird.BIRD_WIDTH / 2;
            final int SCORE_DISTANCE = Pipe.PIPE_WIDTH * 2 + HORIZONTAL_INTERVAL;

            if (lastPipe.isInFrame()) {
                if (pipes.size() >= FULL_PIPE - 2
                        && currentDistance <= SCORE_DISTANCE + Pipe.PIPE_WIDTH * 3 / 2) {
                    ScoreCounter.getInstance().score(bird);
                }
                addPipeLogic(lastPipe);
            }
        }
    }

    private void generateInitialPipes() {
        try {
            int topHeight = GameUtil.getRandomNumber(MIN_HEIGHT, MAX_HEIGHT + 1);
            Pipe top = PipePool.get("Pipe");
            top.setAttribute(Constant.FRAME_WIDTH, -Constant.TOP_PIPE_LENGTHENING,
                    topHeight + Constant.TOP_PIPE_LENGTHENING, Pipe.TYPE_TOP_NORMAL, true);

            Pipe bottom = PipePool.get("Pipe");
            bottom.setAttribute(Constant.FRAME_WIDTH, topHeight + VERTICAL_INTERVAL,
                    Constant.FRAME_HEIGHT - topHeight - VERTICAL_INTERVAL, Pipe.TYPE_BOTTOM_NORMAL, true);

            pipes.add(top);
            pipes.add(bottom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addPipeLogic(Pipe lastPipe) {
        try {
            int currentScore = (int) ScoreCounter.getInstance().getCurrentScore() + 1;
            if (GameUtil.isInProbability(currentScore, 20)) {
                if (GameUtil.isInProbability(1, 4)) {
                    System.out.println("Adding moving hover pipe");
                    addMovingHoverPipe(lastPipe);
                } else {
                    System.out.println("Adding moving normal pipe");
                    addMovingNormalPipe(lastPipe);
                }
            } else {
                if (GameUtil.isInProbability(1, 2)) {
                    System.out.println("Adding normal pipe");
                    addNormalPipe(lastPipe);
                } else {
                    System.out.println("Adding hover pipe");
                    addHoverPipe(lastPipe);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addNormalPipe(Pipe lastPipe) {
        createPipe(lastPipe, Pipe.TYPE_TOP_NORMAL, Pipe.TYPE_BOTTOM_NORMAL);
    }

    private void addHoverPipe(Pipe lastPipe) {
        createPipe(lastPipe, Pipe.TYPE_HOVER_NORMAL, Pipe.TYPE_HOVER_NORMAL);
    }

    private void addMovingNormalPipe(Pipe lastPipe) {
        createPipe(lastPipe, Pipe.TYPE_TOP_HARD, Pipe.TYPE_BOTTOM_HARD);
    }

    private void addMovingHoverPipe(Pipe lastPipe) {
        createPipe(lastPipe, Pipe.TYPE_HOVER_HARD, Pipe.TYPE_HOVER_HARD);
    }

    private void createPipe(Pipe lastPipe, int topType, int bottomType) {
        int topHeight = GameUtil.getRandomNumber(MIN_HEIGHT, MAX_HEIGHT + 1);
        int x = lastPipe.getX() + HORIZONTAL_INTERVAL;

        Pipe top = PipePool.get("Pipe");
        top.setAttribute(x, -Constant.TOP_PIPE_LENGTHENING, topHeight + Constant.TOP_PIPE_LENGTHENING, topType, true);

        Pipe bottom = PipePool.get("Pipe");
        bottom.setAttribute(x, topHeight + VERTICAL_INTERVAL, Constant.FRAME_HEIGHT - topHeight - VERTICAL_INTERVAL, bottomType, true);

        pipes.add(top);
        pipes.add(bottom);
    }

    public void isCollideBird(Bird bird) {
        if (bird == null || bird.isDead()) return;

        for (Pipe pipe : pipes) {
            Rectangle pipeRect = pipe.getPipeRect();
            Rectangle birdRect = bird.getBirdCollisionRect();

            if (pipeRect != null && birdRect != null && pipeRect.intersects(birdRect)) {
                bird.deadBirdFall();
                return;
            }
        }
    }

    public void reset() {
        for (Pipe pipe : pipes) {
            if (pipe != null) {
                PipePool.giveBack(pipe);
            }
        }
        pipes.clear();
    }

    public Collection<Pipe> getPipes() {
        return pipes;
    }
}

