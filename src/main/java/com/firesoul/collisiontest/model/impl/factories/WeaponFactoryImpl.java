package com.firesoul.collisiontest.model.impl.factories;

import com.firesoul.collisiontest.controller.api.DrawableLoader;
import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.api.factories.WeaponFactory;
import com.firesoul.collisiontest.model.impl.gameobjects.bars.AttachedBar;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Gun;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Sword;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.model.api.Drawable;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class WeaponFactoryImpl implements WeaponFactory {

    private final DrawableLoader dl;
    private final Level world;

    public WeaponFactoryImpl(final DrawableLoader dl, final Level world) {
        this.dl = dl;
        this.world = world;
    }

    @Override
    public Weapon sword(final Player holder) {
        final Map<String, Drawable> sprites = Map.of(
            "idle", this.dl.loadSpriteFromSystem("sword"),
            "swing", this.dl.loadSpriteFromSystem("sword_swing")
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
        final AttachedBar reloadBar = new AttachedBar(
            g -> g.getSprite()
                .map(t -> new Vector2(0.0, -t.getHeight() * 0.75))
                .orElse(Vector2.zero()), this.world,
            this.dl.loadDynamicBar(20, 5, Color.WHITE.getRGB()), 1, true
        );
        reloadBar.attachedGameObject(holder);
        final Drawable sprite = this.dl.loadSpriteFromSystem("gun");
        final Vector2 offset = new Vector2(12.0, 0.0);
        final Vector2 projectileOffset = new Vector2(sprite.getWidth() * 1.2, -2.5);
        return new Gun(holder, reloadBar, offset, projectileOffset, Optional.of(sprite),
            this.world, new GameObjectFactoryImpl(this.dl, this.world), 6);
    }
}
