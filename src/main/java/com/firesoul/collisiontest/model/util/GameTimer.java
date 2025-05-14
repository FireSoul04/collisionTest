package com.firesoul.collisiontest.model.util;

import com.firesoul.collisiontest.model.api.Level;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    @FunctionalInterface
    public interface TimerEndAction {

        void get();
    }

    @FunctionalInterface
    public interface TimerTickAction {

        void tick(long remainingTime, long duration);
    }

    private final TimerEndAction onStop;
    private final TimerTickAction onTick;
    private final int delay;
    private final int duration;

    private Timer timer;
    private long remainingTime;
    private boolean running;
    private boolean pause;

    public GameTimer(final TimerEndAction onStop, final TimerTickAction onTick, final int delay, final int duration, final Level world) {
        this.onStop = onStop;
        this.onTick = onTick;
        this.delay = delay;
        this.duration = duration;
        this.remainingTime = duration;
        this.running = false;
        this.pause = false;
        world.addTimer(this);
    }

    public GameTimer(final TimerEndAction onStop, final TimerTickAction onTick, final int duration, final Level world) {
        this(onStop, onTick, 0, duration, world);
    }

    public GameTimer(final TimerEndAction onStop, final int duration, final Level world) {
        this(onStop, (r, d) -> {}, 0, duration, world);
    }

    public GameTimer(final int duration, final Level world) {
        this(() -> {}, duration, world);
    }

    public void start() {
        if (!this.running) {
            this.timer = new Timer();
            this.timer.schedule(
                new TimerTask() {
                public void run() {
                    if (!pause) {
                        onTick.tick(remainingTime, duration);
                        remainingTime--;
                    }
                    if (remainingTime <= 0) {
                        onTick.tick(remainingTime, duration);
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
