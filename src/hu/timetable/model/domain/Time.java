package hu.timetable.model.domain;

public class Time implements Comparable<Time> {

    private final int hour;
    private final int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getDifference(Time other) {
        return this.toMinute() - other.toMinute();
    }

    private Integer toMinute() {
        return hour * 60 + minute;
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }

    @Override
    public int compareTo(Time o) {
        return toMinute().compareTo(o.toMinute());
    }
}
