package hu.timetable.model.domain;

import java.util.stream.Stream;

public enum Direction {
    ARRIVE("E"),
    DEPART("I");

    private final String id;

    Direction(String id) {
        this.id = id;
    }

    public static Direction getDirectionById(String id) {
        return Stream.of(Direction.values())
                .filter(i -> i.getId().equals(id))
                .findAny()
                .get();
    }

    public String getId() {
        return id;
    }
}
