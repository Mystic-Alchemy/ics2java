package com.mystic_alchemy.ics2java;

import com.mystic_alchemy.ics2java.calendar.Calendar;
import com.mystic_alchemy.ics2java.calendar.Event;
import com.mystic_alchemy.ics2java.enums.RecurrenceFrequency;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Parses an iCal ics file into a {@link com.mystic_alchemy.ics2java.calendar.Calendar}.
 * The resulting calendar can include none, one or many {@link Event Events}.
 *
 * <p>
 * The iCal file (type ics) has to be correctly formed, as malformed iCal files will result in a not correctly
 * parsed calendar.
 * </p>
 *
 * @author PilleniusMC for Mystic-Alchemy
 * @version 0.3
 * @since 0.1
 */
public class CalendarParser {
    private static final DateTimeFormatter I_CAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Reads all VEVENTs from a passed iCalendar file and strips the BEGIN and END tags from them.
     * It doesn't edit the events themselves though, they just get pushed into a formatted string.
     * <p>
     * Simple file checks are done at the start of the method, so that only files of the type ics in iCalendar format are
     * actually read, to prevent malformed output.
     * <p>
     * It converts an event of the format <br>
     * <p><code>
     * BEGIN:VEVENT <br>
     * UID:affe69deadbeef <br>
     * DTSTAMP:20010101T120000Z <br>
     * LOCATION: On the world <br>
     * DESCRIPTION:A long description of what the even entails. Including possible further info. <br>
     * SUMMARY: Short descriptor <br>
     * RRULE:FREQ=YEARLY <br>
     * DTSTART;VALUE=DATE:20010101 <br>
     * END:VEVENT
     * </code><p>
     * to the format
     * <p><code>
     * UID:affe69deadbeef|DTSTAMP:20010101T120000Z|LOCATION: On the world|DESCRIPTION:A long description of what the even entails. Including possible further info.|
     * SUMMARY: Short descriptor|RRULE:FREQ=YEARLY|DTSTART;VALUE=DATE:20010101
     * </code>
     * </p>
     *
     * @param icsPath Path to the iCalendar file (type .ics)
     * @return an array of the content of all detected VEVENT elements
     * @throws IllegalArgumentException when a non-existent file or a file of the wrong type is passed
     */
    public static @NotNull ArrayList<String> readEventStringsFromICS(Path icsPath) {
        ArrayList<String> eventStrings = new ArrayList<>();
        String pa = icsPath.toString();
        if (Files.notExists(icsPath)) {
            throw new IllegalArgumentException("Specified path is not an existing file");
        } else {
            try {
                if (!(FilenameUtils.getExtension(pa).equals("ics") && Files.probeContentType(icsPath).equals("text/calendar"))) {
                    throw new IllegalArgumentException("Specified path has to lead to an ics type file");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (Scanner scan = new Scanner(new FileReader(icsPath.toFile()))) {
            StringBuilder builder = new StringBuilder();
            boolean isEvent = false;
            while (scan.hasNext()) {
                String line = scan.nextLine();

                if (line.equals("END:VEVENT")) {
                    isEvent = false;
                    eventStrings.add(builder.substring(0, builder.length() - 1));
                }

                if (isEvent) {
                    builder.append(line).append('|');
                }

                if (line.equals("BEGIN:VEVENT")) {
                    isEvent = true;
                    builder = new StringBuilder();
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return eventStrings;
    }

    /**
     * Parses the VEVENT strings, which are extracted from an iCalendar file by {@link #readEventStringsFromICS(Path)}.
     * <p>
     * This only parses the UID, DESCRIPTION, SUMMARY, RRULE, LOCATION and DTSTART from a VEVENT.
     * The method is also pretty dumb due to the need to limit complexity.
     *
     * @param eventString the content of an iCalendar even as a String with | as its delimiter
     * @return the parsed event
     */
    public static Event parseEventString(String eventString) {
        int uidIndex = eventString.lastIndexOf("UID:");
        String uid = eventString.substring(uidIndex, eventString.indexOf('|', uidIndex));
        Event event = new Event(uid);
        String[] parts = eventString.split("\\|");
        for (String part : parts) {
            String[] pair = part.split("[:;=]");
            switch (pair[0]) {
                case "DESCRIPTION" -> event.setDescription(pair[1]);
                case "SUMMARY" -> event.setSummary(pair[1]);
                case "RRULE" -> event.setFreq(RecurrenceFrequency.valueOf(pair[2]));
                case "LOCATION" -> event.setLocation(pair[1].split("\\\\,"));
                case "DTSTART" -> {
                    String dateString = pair[3];
                    event.setDtStart(LocalDate.parse(dateString, I_CAL_DATE_FORMAT));
                }
            }
        }
        return event;
    }

    /**
     * Parses a whole ICS calendar from the start to finish.
     * <p>
     * First it reads the ICS file, then parses the events.
     *
     * @param icsPath Path to the iCalendar file (type .ics)
     * @return A calendar with parsed events
     */
    public static Calendar parseCalendar(Path icsPath) {
        ArrayList<String> eventStrings = readEventStringsFromICS(icsPath);
        Calendar calendar = new Calendar();
        for (String eS: eventStrings) {
            Event et = parseEventString(eS);
            calendar.addEvent(et);
        }
        return calendar;
    }
}

