package com.firesoul.collisiontest.model.impl.factories;

import com.firesoul.collisiontest.model.api.physics.Collider;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.gameobjects.Weapon;
import com.firesoul.collisiontest.model.api.WeaponFactory;
import com.firesoul.collisiontest.model.impl.gameobjects.GameBar;
import com.firesoul.collisiontest.model.impl.physics.colliders.BoxCollider;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Gun;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.impl.gameobjects.weapons.Sword;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.impl.SwingBar;
import com.firesoul.collisiontest.view.impl.SwingSprite;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class WeaponFactoryImpl implements WeaponFactory {

    private final Level world;

    public WeaponFactoryImpl(final Level world) {
        this.world = world;
    }

    @Override
    public Weapon sword(final Player holder) {
        final Map<String, Drawable> sprites = Map.of(
            "idle", new SwingSprite("sword", Vector2.zero()),
            "swing", new SwingSprite("sword_swing", Vector2.zero())
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
        final GameBar reloadBar = new GameBar(new Vector2(1.0, 20.0), this.world,
            new SwingBar(new Vector2(1.0, 20.0), 20, 10, Color.WHITE, true),
            1, true
        );
        final SwingSprite sprite = new SwingSprite("gun", Vector2.zero());
        final Vector2 offset = new Vector2(12.0, 0.0);
        final Vector2 projectileOffset = new Vector2(sprite.getWidth() * 1.2, -2.5);
        this.world.instanciate(reloadBar);
        return new Gun(holder, reloadBar, offset, projectileOffset, Optional.of(sprite), this.world, 6);
    }
}
