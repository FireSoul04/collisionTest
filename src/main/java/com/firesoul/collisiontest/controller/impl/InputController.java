package com.firesoul.collisiontest.controller.impl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InputController {

    public interface Event {
        boolean check();
    }

    private static class Key {
        private final int keyCode;
        private boolean alreadyPressed = false;

        private Key(final int keyCode) {
            this.keyCode = keyCode;
        }

        public void setAlreadyPressed(final boolean alreadyPressed) {
            this.alreadyPressed = alreadyPressed;
        }

        public boolean isAlreadyPressed() {
            return this.alreadyPressed;
        }

        @Override
        public int hashCode() {
            return this.keyCode;
        }
    }
    
    private final Map<Integer, Key> keys = new HashMap<>();
    private final Set<Key> keysPressed = new HashSet<>();
    private final Map<String, Event> events = new HashMap<>();
    private final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(final KeyEvent e) {
        }
    
        @Override
        public void keyPressed(final KeyEvent e) {
            keysPressed.add(keys.get(e.getKeyCode()));
        }
    
        @Override
        public void keyReleased(final KeyEvent e) {
            keysPressed.remove(keys.get(e.getKeyCode()));
        }
    };

    /**
     * Create a controller for key pressed.
     */
    public InputController() {
        for (int i = KeyEvent.VK_SPACE; i < 127; i++) {
            this.keys.put(i, new Key(i));
        }
    }

    public void addEvent(final String name, final Event e) {
        this.events.put(name, e);
    }

    public boolean getEvent(final String name) {
        return this.events.get(name).check();
    }

    public boolean isKeyPressed(final int keyCode) {
        return this.keysPressed.contains(this.keys.get(keyCode));
    }

    public boolean isKeyPressedOnce(final int keyCode) {
        final Key key = this.keys.get(keyCode);
        if (!key.isAlreadyPressed() && this.isKeyPressed(keyCode)) {
            key.setAlreadyPressed(true);
            return true;
        } else if (!this.isKeyPressed(keyCode)) {
            key.setAlreadyPressed(false);
        }
        return false;
    }
    
    public KeyListener getKeyListener() {
        return this.keyListener;
    }
}
