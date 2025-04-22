package test.model.impl;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private final Runnable onStop;
    private final int delay;
    private final int duration;

    private Timer timer;
    private int currentTime;
    private boolean running;

    public GameTimer(final Runnable onStop, final int delay, final int duration) {
        this.onStop = onStop;
        this.delay = delay;
        this.duration = duration;
        this.currentTime = duration;
        this.running = false;
    }

    public void start() {
        if (!this.running) {
            this.timer = new Timer();
            this.timer.schedule(
                new TimerTask() {
                    public void run() {
                        currentTime--;
                        if (currentTime < 0) {
                            onStop.run();
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
        this.currentTime = this.duration;
        this.running = false;
        this.timer.cancel();
    }

    public boolean isRunning() {
        return this.running;
    }
}
