package com.firesoul.collisiontest.model.impl.gameobjects;

import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Bar;

import java.util.Optional;

public class LifeBar extends GameObjectImpl {

    private final Bar bar;

    public LifeBar(final Level world, final Bar sprite) {
        super(Vector2.zero(), false, world, Optional.empty(), Optional.of(sprite));
        this.bar = sprite;
    }

    @Override
    public void update(final double deltaTime) {
        this.setPosition(this.getWorld().getCamera().getPosition().add(Vector2.one()));
    }

    public void setCurrentValue(final int value) {
        this.bar.setCurrentValue(value);
    }
}
