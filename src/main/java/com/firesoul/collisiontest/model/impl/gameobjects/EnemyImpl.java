package com.firesoul.collisiontest.model.impl.gameobjects;

import java.util.Map;
import java.util.Optional;

import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Enemy;
import com.firesoul.collisiontest.model.api.physics.RigidBody;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.AttachedBar;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.GameBar;
import com.firesoul.collisiontest.model.impl.physics.EnhancedRigidBody;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.model.api.Drawable;

public class EnemyImpl extends EntityImpl implements Enemy {

    public interface EnemyBehavior {

        void behave(Enemy enemy);
    }

    private final RigidBody body;
    private final EnemyBehavior behavior;
    private final GameBar lifeBar;
    private final Map<String, Drawable> sprites;

    public EnemyImpl(final Vector2 position, final boolean dynamic, final Level world,
                     final Optional<Collider> collider, final Map<String, Drawable> sprites,
                     final int iframes, final int life, final Vector2 gravity, final EnemyBehavior behavior,
                     final GameBar lifeBar
    ) {
        super(position, dynamic, world, collider, Optional.of(sprites.get("idle")), iframes, life);
        this.body = new EnhancedRigidBody(new Vector2(1.0, 0.0), gravity, Vector2.zero());
        this.sprites = sprites;
        this.behavior = behavior;
        this.lifeBar = lifeBar;
        lifeBar.getSprite().ifPresent(t -> t.setVisible(true));
        if (this.lifeBar instanceof AttachedBar lb) {
            lb.attachedGameObject(this);
        }
    }

    @Override
    public void onDestroy() {
        this.lifeBar.destroy();
    }

    @Override
    public Vector2 getVelocity() {
        return this.body.getVelocity();
    }

    @Override
    public void setVelocity(final Vector2 velocity) {
        this.body.setVelocity(velocity);
    }

    @Override
    public void update(final double deltaTime) {
        this.move(this.getVelocity().multiply(deltaTime));
        this.body.update();

        this.sprites.forEach((k, v) -> v.translate(this.getPosition()));
        this.lifeBar.setCurrentValue(this.getLife());
        this.behavior.behave(this);
        if (this.isInvincible()) {
            this.setSprite(this.sprites.get("damage"));
        } else {
            this.setSprite(this.sprites.get("idle"));
        }
    }
}
