package com.mystic_alchemy.ics2java.calendar;

import com.mystic_alchemy.ics2java.enums.RecurrenceFrequency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Represents iCal VEVENT type entries with their UID, location, summary, description, start and optionally the
 * recurrence frequency.
 *
 * @author PilleniusMC for Mystic-Alchemy
 * @version 0.3
 * @since 0.1
 */
public class Event {
    private final String UID;
    private String[] location;
    private String summary;
    private String description;
    private LocalDate dtStart;
    private RecurrenceFrequency freq;

    /**
     * Generates an event with a give UID
     * @param uid The UID of the event
     */
    public Event(String uid) {
        UID = uid;
    }

    /**
     * Gets the UID for this event
     * @return the UID
     */
    @NotNull
    public String getUID() {
        return UID;
    }

    /**
     * Gets the array of locations for this event
     * @return Array with locations or null
     */
    @Nullable
    public String[] getLocation() {
        return location;
    }

    /**
     * Sets the locations for this event
     * @param location array of locations or null if no location is set
     */
    public void setLocation(@Nullable String[] location) {
        this.location = location;
    }

    /**
     * Gets the summary for this event
     * @return the summary, a short descriptor of the event, e.g. "Alex's Birthday"
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary of this event
     * @param summary a short descriptor for the event
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }


    /**
     * Gets the description of this event.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this event.
     * <p>
     * The description can include additional info for this event and should extend the summary.
     *
     * @param description a longer description for this event
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the start date for this event. If no recurrence is set, then this is also when the event is happening.
     * @return the start date or null
     */
    public LocalDate getDtStart() {
        return dtStart;
    }

    /**
     * Sets the start date for this event. If no recurrence is set, then this is also when the event is happening.
     * @param dtStart the start date
     */
    public void setDtStart(@NotNull LocalDate dtStart) {
        this.dtStart = dtStart;
    }

    /**
     * Gets the frequency of recurrence for this event. If this event is not recurring, then null would be returned.
     * @return recurrence frequency or null
     */
    @Nullable
    public RecurrenceFrequency getFreq() {
        return freq;
    }

    /**
     * Sets this event to be recurrent or not, including the frequency of recurrence.
     * <code>null</code> would be equal to no recurrence, whereas any set value would imply recurrence.
     * @param freq the recurrence frequency or null
     * @see RecurrenceFrequency
     */
    public void setFreq(@Nullable RecurrenceFrequency freq) {
        this.freq = freq;
    }

    /**
     * Generates a string for this event, which is made up of the {@link #getSummary() summary}, the
     * {@link #getDtStart() start date} in ISO 8601 format, the {@link #getLocation() location(-s)}, and if applicable
     * the {@link #getFreq() frequency} of recurrence.
     * @return a string representing this object
     */
    @Override
    public String toString() {
        String ts = "Event: " + getSummary() + "; " + getDtStart().toString() + "; " + Arrays.toString(getLocation());
        if (getFreq() != null) {
            ts = ts + "; " + getFreq();
        }
        return ts;
    }
}
