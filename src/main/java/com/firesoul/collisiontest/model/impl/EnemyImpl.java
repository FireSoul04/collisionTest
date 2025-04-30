package com.firesoul.collisiontest.model.impl;

import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Enemy;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class EnemyImpl extends EntityImpl implements Enemy {

    private Runnable behavior;

    public EnemyImpl(final Vector2 position, final double orientation, final boolean dynamic, final Optional<Collider> collider,
            final Optional<Drawable> sprite, final int iframes, final int life, final Runnable behavior) {
        super(position, orientation, dynamic, collider, sprite, iframes, life);
        this.behavior = behavior;
    }

    @Override
    public void behave() {
        behavior.run();
    }
}
