package com.mystic_alchemy.ics2java;

import java.util.ArrayList;

public class Sandbox {
    public static void main(String[] args) {
        ArrayList<String> strings = CalendarParser.readEventStringsFromICS("\\\\fi-maut-dc\\home\\kumitz_philipp\\.profile\\Downloads\\Feiertage Deutschland.ics");
        for (String s : strings)  {
            System.out.println(CalendarParser.parseEventString(s));
        }
    }


}
