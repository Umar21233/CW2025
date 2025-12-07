package com.comp2042.model;

/**
 - Immutable input descriptor sent from the UI layer to the game logic.
 - Specifies what type of movement was requested and whether it came
   from the user or from the game loop thread.
 */


public final class MoveEvent {
    /** The type of movement event (e.g., DOWN, LEFT, ROTATE). */
    private final EventType eventType;
    /** The source from which this event originated (e.g., USER, THREAD). */
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent.
     *
     * @param eventType The type of movement that occurred.
     * @param eventSource The source of the event (user or game thread).
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Returns the type of movement event.
     *
     * @return The {@code EventType} of this event.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Returns the source of the event.
     *
     * @return The {@code EventSource} of this event.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
