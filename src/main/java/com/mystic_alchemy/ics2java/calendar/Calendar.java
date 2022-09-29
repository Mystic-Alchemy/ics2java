package com.mystic_alchemy.ics2java.calendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an iCal calendar with its events.
 * Doesn't handle weekdays on its own.
 *
 * @author PilleniusMC for Mystic-Alchemy
 * @since 0.1
 * @version 0.3
 */
public class Calendar {
    ArrayList<Event> events = new ArrayList<>();

    /**
     * Constructs an empty Calendar.
     */
    public Calendar() {
        super();
    }

    /**
     * Constructs a calendar with initial events.
     * @param events the initial events
     */
    public Calendar(Event... events) {
        this();
        this.events.addAll(List.of(events));
    }

    /**
     * Gets all events of this calendar
     * @return the events of this calendar
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Sets the events for this calendar
     * @param events the events
     */
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    /**
     * Adds an event to this calendar
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        events.add(event);
    }
}
