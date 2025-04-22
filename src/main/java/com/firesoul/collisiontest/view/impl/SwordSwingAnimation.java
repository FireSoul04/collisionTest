package com.firesoul.collisiontest.view.impl;

import java.util.function.Supplier;

public class SwordSwingAnimation extends Animation {

    public SwordSwingAnimation(final int delay, final Runnable repeat, final Runnable onStop, final Supplier<Boolean> stopCondition) {
        super(
            delay,
            repeat,
            onStop,
            stopCondition
        );
    }
}
