package hu.timetable.model.service;

import hu.timetable.model.domain.Time;

import java.util.Scanner;

public class Console {

    private final Scanner scanner;
    private final DataParser dataParser;

    public Console(Scanner scanner, DataParser dataParser) {
        this.scanner = scanner;
        this.dataParser = dataParser;
    }

    public Time readTime() {
        return dataParser.parseTime(scanner.nextLine());
    }

    public int readInt() {
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}
