package com.firesoul.collisiontest.controller.impl.loader;

import com.firesoul.collisiontest.controller.api.loader.LevelLoader;
import com.firesoul.collisiontest.controller.impl.GameCore;
import com.firesoul.collisiontest.model.api.Level;
import com.firesoul.collisiontest.model.api.factories.GameObjectFactory;
import com.firesoul.collisiontest.model.api.factories.WeaponFactory;
import com.firesoul.collisiontest.model.impl.TileBasedLevel;
import com.firesoul.collisiontest.model.impl.factories.WeaponFactoryImpl;
import com.firesoul.collisiontest.model.impl.gameobjects.Player;
import com.firesoul.collisiontest.model.util.Vector2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TileBasedLevelLoader implements LevelLoader {

	private record Coordinates(int x, int y) {}

	private static final String MAP_PATH = "src/main/resources/map/map.txt";

	private final GameCore controller;

	public TileBasedLevelLoader(final GameCore controller) {
		this.controller = controller;
	}

	@Override
	public Level readLevel() {
		try {
			final List<String> lines = Files.readAllLines(Paths.get(MAP_PATH));
			final Map<Coordinates, Character> gameObjects = new HashMap<>();
			int width = 0;
			int height = 0;
			int y = 0;
			for (final String line : lines) {
				int x = 0;
				for (final char c : line.toCharArray()) {
					gameObjects.put(new Coordinates(x, y), c);
					x += TileBasedLevel.TILE_SIZE;
				}
				y += TileBasedLevel.TILE_SIZE;
				width = Math.max(width, x);
				height = Math.max(height, y);
			}
			return this.levelFromText(width, height, gameObjects);
		} catch (Exception e) {
			System.out.println("Cannot read level: " + e);
		}
		return new TileBasedLevel(this.controller);
	}

	private Level levelFromText(final int width, final int height, final Map<Coordinates, Character> gameObjects) {
		final Level level = new TileBasedLevel(width, height, this.controller);
		final GameObjectFactory gf = level.getGameObjectFactory();
		final WeaponFactory wf = new WeaponFactoryImpl(this.controller.getDrawableLoader(), level);
		gameObjects.forEach((pos, ch) -> {
			final Vector2 position = new Vector2(pos.x(), pos.y());
			Optional<Player> player = Optional.empty();
			switch (ch) {
				case 'P' -> player = Optional.of(gf.player(position, controller.getEventManager()));
				case '#' -> gf.block(position);
				case 'g' -> gf.groundEnemy(position);
				case 'f' -> gf.flyingEnemy(position, 10.0, 0.03);
			}
			player.ifPresent(t -> t.equip(wf.gun(t)));
			player.ifPresent(t -> t.equip(wf.sword(t)));
		});
		return level;
	}
}
