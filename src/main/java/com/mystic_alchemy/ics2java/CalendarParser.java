package com.mystic_alchemy.ics2java;

import com.mystic_alchemy.ics2java.calendar.Event;
import com.mystic_alchemy.ics2java.enums.RecurrenceFrequency;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

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
        for (int i = 0; i < parts.length; i++) {
            String[] pair = parts[i].split("[:;=]");
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

