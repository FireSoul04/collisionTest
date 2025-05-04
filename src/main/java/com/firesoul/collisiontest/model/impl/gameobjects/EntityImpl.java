package com.firesoul.collisiontest.model.impl.gameobjects;

import java.util.Optional;

import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Entity;
import com.firesoul.collisiontest.model.util.GameTimer;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;

public class EntityImpl extends GameObjectImpl implements Entity {

    // Life logic
    private final GameTimer iframes;
    private boolean invincible;
    private int life;

    public EntityImpl(final Vector2 position, final boolean dynamic, final Level world,
          final Optional<Collider> collider, final Optional<Drawable> sprite, final int iframes, final int life
    ) {
        super(position, dynamic, world, collider, sprite);
        this.iframes = new GameTimer(() -> this.invincible = false, iframes);
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
    public boolean isInvincible() {
        return this.invincible;
    }
}
