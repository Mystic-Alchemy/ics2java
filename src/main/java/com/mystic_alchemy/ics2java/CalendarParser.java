package com.mystic_alchemy.ics2java;

import com.mystic_alchemy.ics2java.calendar.Event;
import com.mystic_alchemy.ics2java.enums.RecurrenceFrequency;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Parses an iCal ics file into a {@link com.mystic_alchemy.ics2java.calendar.Calendar}.
 * The resulting calendar can include none, one or many {@link Event Events}.
 *
 * <p>
 *     The iCal file (type ics) has to be correctly formed, as malformed iCal files will result in a not correctly
 *     parsed calendar.
 * </p>
 *
 * @author PilleniusMC for Mystic-Alchemy
 * @version 1.0.0
 * @since 1.0.0
 */
public class CalendarParser {
    private static final DateTimeFormatter I_CAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static ArrayList<String> readEventStringsFromICS(String icsPath) {
        ArrayList<String> eventStrings = new ArrayList<>();
        try (Scanner scan = new Scanner(new FileReader(icsPath))) {
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

    public static Event parseEventString(String eventString) {
        String uid = eventString.substring(eventString.lastIndexOf("UID:"));
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
}

