package com.firesoul.collisiontest.model.util;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    @FunctionalInterface
    public interface TimerAction {

        void get();
    }

    private final TimerAction onStop;
    private final int delay;
    private final int duration;

    private Timer timer;
    private int remainingTime;
    private boolean running;
    private boolean pause;

    public GameTimer(final TimerAction onStop, final int delay, final int duration) {
        this.onStop = onStop;
        this.delay = delay;
        this.duration = duration;
        this.remainingTime = duration;
        this.running = false;
        this.pause = false;
    }

    public GameTimer(final TimerAction onStop, final int duration) {
        this(onStop, 0, duration);
    }

    public GameTimer(final int duration) {
        this(() -> {}, duration);
    }

    public void start() {
        if (!this.running) {
            this.timer = new Timer();
            this.timer.schedule(
                new TimerTask() {
                    public void run() {
                        if (!pause) {
                            remainingTime--;
                        }
                        if (remainingTime <= 0) {
                            onStop.get();
                            stop();
                        }
                    }
                },
                this.delay,
                1
            );
            this.running = true;
        }
    }

    public void stop() {
        this.remainingTime = this.duration;
        this.running = false;
        this.timer.cancel();
    }

    public void pause() {
        this.pause = true;
    }

    public void unPause() {
        this.pause = false;
    }

    public boolean isRunning() {
        return this.running;
    }
}
