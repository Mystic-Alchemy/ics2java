package com.mystic_alchemy.ics2java.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an iCal calendar with its events.
 * Doesn't handle weekdays on its own.
 *
 * @author PilleniusMC for Mystic-Alchemy
 * @since 0.1
 * @version 0.4
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

    /**
     * Gets events for a given date.
     * @param date the date for which Events should be gotten
     * @return an array of events, happening on the passed date
     */
    public Event[] getEventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event e : this.events) {
            LocalDate eventStart = e.getDtStart();
            if (date.isEqual(eventStart)) {
                events.add(e);
            } else if (e.getFreq() != null && eventStart.isBefore(date)) {
                final LocalDate eStAdaptedToYear = eventStart.withYear(date.getYear());
                switch (e.getFreq()) {
                    case YEARLY -> {
                        if (eStAdaptedToYear.isEqual(date)) {
                            events.add(e);
                        }
                    }
                    case MONTHLY -> {
                        if (eStAdaptedToYear.isEqual(date) &&
                        eStAdaptedToYear.withMonth(date.getMonthValue()).isEqual(date)) {
                            events.add(e);
                        }
                    }
                    case DAILY -> {
                        if (eventStart.isBefore(date)) {
                            events.add(e);
                        }
                    }
                    case WEEKLY -> {
                        if (eventStart.isBefore(date)) {
                            LocalDate weekly = eventStart;
                            do {
                                if (weekly.isEqual(date)) {
                                    events.add(e);
                                    break;
                                }
                                weekly = weekly.plusWeeks(1);
                            } while (weekly.isBefore(date));
                        }
                    }
                }
            }
        }
        return events.toArray(new Event[0]);
    }
}
