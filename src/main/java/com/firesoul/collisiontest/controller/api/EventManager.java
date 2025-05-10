package com.firesoul.collisiontest.controller.api;

public interface EventManager<T> {

    void addEvent(T key, Event event);

    boolean getEvent(T key);

    void attachActionOnEvent(T key, Action action);

    void update();
}
