package com.mystic_alchemy.ics2java.calendar;

import com.mystic_alchemy.ics2java.enums.RecurrenceFrequency;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * @author PilleniusMC for Mystic-Alchemy
 * @since 1.0.0
 * @version 1.0.0
 */
public class Event {
    private final String UID;
    private String[] location;
    private String summary;
    private String description;
    private LocalDate dtStart;
    private RecurrenceFrequency freq;

    public Event(String uid) {
        UID = uid;
    }

    public String getUID() {
        return UID;
    }

    public String[] getLocation() {
        return location;
    }

    public void setLocation(String[] location) {
        this.location = location;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDtStart() {
        return dtStart;
    }

    public void setDtStart(LocalDate dtStart) {
        this.dtStart = dtStart;
    }

    public RecurrenceFrequency getFreq() {
        return freq;
    }

    public void setFreq(RecurrenceFrequency freq) {
        this.freq = freq;
    }

    @Override
    public String toString() {
        String ts = "Event: " + getSummary() + "; " + getDtStart().toString() + "; " + Arrays.toString(getLocation());
        if (getFreq() != null) {
            ts = ts + "; " + getFreq();
        }
        return ts;
    }
}
