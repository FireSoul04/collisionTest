package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.model.api.Event;

public interface EventManager {

    void addEvent(String name, Event e);

    boolean getEvent(String name);
}
