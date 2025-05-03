package com.firesoul.collisiontest.model.impl;

import com.firesoul.collisiontest.controller.impl.Controller;
import com.firesoul.collisiontest.model.api.Collider;
import com.firesoul.collisiontest.model.api.GameObject;
import com.firesoul.collisiontest.model.api.Weapon;
import com.firesoul.collisiontest.model.api.WeaponFactory;
import com.firesoul.collisiontest.model.util.Vector2;
import com.firesoul.collisiontest.view.api.Drawable;
import com.firesoul.collisiontest.view.api.Renderer;
import com.firesoul.collisiontest.view.impl.SwingSprite;

import java.util.Map;
import java.util.Optional;

public class WeaponFactoryImpl implements WeaponFactory {

    private final Renderer renderer;

    public WeaponFactoryImpl(final Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Weapon sword(final Player holder) {
        final Map<String, Drawable> sprites = Map.of(
                "idle", new SwingSprite("sword1", Vector2.zero(), 0.0, this.renderer),
                "swing", new SwingSprite("sword_swing", Vector2.zero(), 0.0, this.renderer)
        );
        final Vector2 offset = new Vector2(35.0, -20.0);
        final Vector2 spriteOffset = new Vector2(45.0, -10.0);
        final Collider collider = new MeshCollider(Controller.regularPolygon(4), 16.0, Math.PI/2);
        final Weapon sword = new Sword(holder, offset, spriteOffset, 0.0, collider, sprites);
        collider.attachGameObject(sword);
        return sword;
    }

    @Override
    public Weapon gun(final Player holder, final GameCollisions world) {
        final SwingSprite sprite = new SwingSprite("gun", Vector2.zero(), 0.0, this.renderer);
        final Vector2 offset = new Vector2(25.0, 0.0);
        final Vector2 projectileOffset = new Vector2(sprite.getWidth() * 2.5, -5.0);
        return new Gun(holder, offset, projectileOffset, 0.0, Optional.of(sprite), world, 6);
    }
}
