package com.firesoul.collisiontest.model.impl;

import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Entity;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class EntityImpl extends GameObjectImpl implements Entity {

    // Life logic
    private final GameTimer iframes;
    private boolean invincible;
    private int life;

    public EntityImpl(final Vector2 position, final double orientation, final boolean dynamic, final Optional<Collider> collider,
        final Optional<Drawable> sprite, final int iframes, final int life
    ) {
        super(position, orientation, dynamic, collider, sprite);
        this.iframes = new GameTimer(() -> this.invincible = false, 0, iframes);
        this.invincible = false;
        this.life = life;
    }

    @Override
    public void takeDamage(final int amount) {
        if (!this.invincible) {
            this.life = this.life - amount;
            this.invincible = true;
            this.iframes.start();

            if (this.life <= 0) {
                this.onDestroy();
                this.destroy();
            }
        }
    }

    @Override
    public void onDestroy() {
        
    }

    @Override
    public boolean isInvincible() {
        return this.invincible;
    }
}
