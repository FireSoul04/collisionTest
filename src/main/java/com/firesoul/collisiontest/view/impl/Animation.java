package com.firesoul.collisiontest.view.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

import javax.swing.Timer;

public abstract class Animation {

    private final Timer timer;

    public Animation(final int delay, final Runnable repeat, final Runnable onStop, final Supplier<Boolean> stopCondition) {
        this.timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (stopCondition.get()) {
                    ((Timer) e.getSource()).stop();
                    onStop.run();
                } else {
                    repeat.run();
                }
            }
        });
    }

    public void start() {
        this.timer.start();
    }

    public boolean isRunning() {
        return this.timer.isRunning();
    }
}
