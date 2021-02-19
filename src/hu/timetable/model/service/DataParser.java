package hu.timetable.model.service;

import hu.timetable.model.domain.Direction;
import hu.timetable.model.domain.Time;
import hu.timetable.model.domain.TimeTable;

import java.util.List;
import java.util.stream.Collectors;

public class DataParser {

    public List<TimeTable> parse(List<String> lines) {
        return lines.stream()
                .map(this::createTimeTable)
                .collect(Collectors.toList());
    }

    public Time parseTime(String text) {
        String[] items = text.split(" ");
        int hour = parseInt(items[0]);
        int minute = parseInt(items[1]);
        return new Time(hour, minute);
    }

    private TimeTable createTimeTable(String line) {
        String[] items = line.split("\t");
        int trainId = parseInt(items[0]);
        int stationId = parseInt(items[1]);
        Time time = parseTime(String.join(" ", items[2], items[3]));
        Direction direction = Direction.getDirectionById(items[4]);
        return new TimeTable(trainId, stationId, time, direction);
    }

    private int parseInt(String text) {
        return Integer.parseInt(text);
    }

}
