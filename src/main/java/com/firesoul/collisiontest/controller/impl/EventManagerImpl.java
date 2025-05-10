package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.controller.api.Action;
import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.controller.api.Event;

import java.util.HashMap;
import java.util.Map;

public class EventManagerImpl<T> implements EventManager<T> {

	private final Map<T, Event> events = new HashMap<>();
	private final Map<T, Action> actions = new HashMap<>();

	@Override
	public void addEvent(final T key, final Event e) {
		this.events.put(key, e);
	}

	@Override
	public boolean getEvent(final T key) {
		return this.events.get(key).check();
	}

	@Override
	public void attachActionOnEvent(final T key, final Action action) {
		this.actions.put(key, action);
	}

	@Override
	public void update() {
		this.events.forEach((name, evt) -> {
			if (this.actions.containsKey(name) && evt.check()) {
				this.actions.get(name).exectute();
			}
		});
	}
}
