package com.firesoul.collisiontest.controller.impl;

import com.firesoul.collisiontest.controller.api.EventManager;
import com.firesoul.collisiontest.model.api.Event;

import java.util.HashMap;
import java.util.Map;

public class EventManagerImpl implements EventManager {

	private final Map<String, Event> events = new HashMap<>();

	@Override
	public void addEvent(final String name, final Event e) {
		this.events.put(name, e);
	}

	@Override
	public boolean getEvent(final String name) {
		return this.events.get(name).check();
	}
}
