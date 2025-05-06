package com.firesoul.collisiontest.model.impl.gameobjects.bars;

import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.impl.gameobjects.GameObjectImpl;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Bar;

import java.util.Optional;

public abstract class GameBar extends GameObjectImpl {

    private final Bar bar;
    private final double maxValue;
    private final boolean inverse;

    public GameBar(final Vector2 position, final Level world, final Bar sprite, final double maxValue, final boolean inverse) {
        super(position, false, world, Optional.empty(), Optional.of(sprite));
        this.bar = sprite;
        this.maxValue = maxValue;
        this.inverse = inverse;
    }

    public void setCurrentValue(final long value) {
        this.setCurrentPercentage(value / this.maxValue);
    }

    public void setCurrentPercentage(final double percentage) {
        this.bar.setCurrentPercentage(this.inverse ? this.maxValue - percentage : percentage);
    }

    protected Bar getBar() {
        return this.bar;
    }
}
