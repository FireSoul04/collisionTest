package com.firesoul.collisiontest.model.impl.factories;

import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.api.factories.WeaponFactory;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.AttachedBar;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.GameBar;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Gun;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Sword;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.api.DrawableFactory;

import java.awt.Color;
import java.util.Map;
import java.util.Optional;

public class WeaponFactoryImpl implements WeaponFactory {

    private final DrawableFactory df;
    private final Level world;

    public WeaponFactoryImpl(final Level world) {
        this.df = world.getDrawableFactory();
        this.world = world;
    }

    @Override
    public Weapon sword(final Player holder) {
        final Map<String, Drawable> sprites = Map.of(
            "idle", this.df.spriteByName("sword", Vector2.zero()),
            "swing", this.df.spriteByName("sword_swing", Vector2.zero())
        );
        final Vector2 offset = new Vector2(22.0, -12.0);
        final Vector2 spriteOffset = new Vector2(27.0, -5.0);
        final Vector2 size = new Vector2(15.0, 15.0);
        final Collider collider = new BoxCollider(Vector2.zero(), size.x(), size.y());
        final Weapon sword = new Sword(holder, offset, spriteOffset, this.world, collider, sprites);
        collider.setPosition(sword.getPosition());
        return sword;
    }

    @Override
    public Weapon gun(final Player holder) {
        final GameBar reloadBar = new AttachedBar(
            holder, g -> g.getSprite()
                .map(t -> new Vector2(0.0, -t.getHeight() * 0.75))
                .orElse(Vector2.zero()), this.world,
            this.df.invisibleDynamicBar(20, 10, Color.WHITE.getRGB()), 1, true
        );
        final Drawable sprite = this.df.spriteByName("gun", Vector2.zero());
        final Vector2 offset = new Vector2(12.0, 0.0);
        final Vector2 projectileOffset = new Vector2(sprite.getWidth() * 1.2, -2.5);
        this.world.instanciate(reloadBar);
        return new Gun(holder, reloadBar, offset, projectileOffset, Optional.of(sprite), this.world, 6);
    }
}
