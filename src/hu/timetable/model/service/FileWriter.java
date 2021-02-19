package hu.timetable.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileWriter {

    private static final String FILENAME_PATTERN = "halad%d.txt" ;

    public void writeAll(int trainId, List<String> lines) {
        try {
            Files.write(Path.of(getFilename(trainId)), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilename(int trainId) {
        return String.format(FILENAME_PATTERN, trainId);
    }
}
