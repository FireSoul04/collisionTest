package com.firesoul.collisiontest.model.impl.gameobjects;

import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Bar;

import java.util.Optional;

public class GameBar extends GameObjectImpl {

    private final Vector2 position;
    private final Bar bar;
    private final double maxValue;
    private final boolean inverse;

    public GameBar(final Vector2 position, final Level world, final Bar sprite, final double maxValue, final boolean inverse) {
        super(position, false, world, Optional.empty(), Optional.of(sprite));
        this.position = position;
        this.bar = sprite;
        this.maxValue = maxValue;
        this.inverse = inverse;
    }

    public GameBar(final Vector2 position, final Level world, final Bar sprite, final double maxValue) {
        this(position, world, sprite, maxValue, false);
    }

    @Override
    public void update(final double deltaTime) {
        this.setPosition(this.getWorld().getCamera().getPosition().add(new Vector2(
            this.getWorld().getCamera().getWidth(),
            this.getWorld().getCamera().getHeight()
        ).divide(2.0)));
        this.bar.translate(position);
    }

    public void setCurrentValue(final long value) {
        final double perc = (value / this.maxValue);
        this.bar.setCurrentPercentage(inverse ? maxValue - perc : perc);
    }

    public void setCurrentPercentage(final double percentage) {
        this.bar.setCurrentPercentage(inverse ? maxValue - percentage : percentage);
    }
}
