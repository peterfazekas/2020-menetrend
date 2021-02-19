package hu.timetable.model.service;

import hu.timetable.model.domain.TimeTable;

import java.util.List;

public class DataApi {

    private final FileReader fileReader;
    private final DataParser dataParser;

    public DataApi(FileReader fileReader, DataParser dataParser) {
        this.fileReader = fileReader;
        this.dataParser = dataParser;
    }

    public List<TimeTable> getData(String filename) {
        return dataParser.parse(fileReader.read(filename));
    }
}
