package com.comp2042.model;

/**
 - Immutable input descriptor sent from the UI layer to the game logic.
 - Specifies what type of movement was requested and whether it came
   from the user or from the game loop thread.
 */


public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventSource getEventSource() {
        return eventSource;
    }
}
