# ics2java - A tiny library to read iCal files into Java
iCal files in the ics format are pretty common, but there's not many libraries for it on Java, and the ones, that do 
exist are pretty large. 
This library here is supposed to be as simple as possible, sadly that also doesn't make it as flexible as other
libraries.

## Features
- Reading of standard ics files
- Handling vevent elements
- Handling recurrence frequency rules

## What it can't/won't do
- Complex recurrence
- Non standard implementations
- Reading of malformed calenders
- Handling of weekdays internally