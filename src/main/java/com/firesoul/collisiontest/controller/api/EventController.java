package com.firesoul.collisiontest.controller.api;

import com.firesoul.collisiontest.model.api.Event;

public interface EventController {

    void addEvent(String name, Event e);

    boolean getEvent(String name);

    void resetEvents();
}
