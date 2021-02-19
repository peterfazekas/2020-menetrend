package hu.timetable;

import hu.timetable.controller.TrainService;
import hu.timetable.model.domain.Time;
import hu.timetable.model.service.*;

import java.util.Scanner;

public class App {

    private final TrainService service;
    private final Console console;
    private final FileWriter fileWriter;

    private App() {
        fileWriter = new FileWriter();
        DataParser dataParser = new DataParser();
        console = new Console(new Scanner(System.in), dataParser);
        DataApi dataApi = new DataApi(new FileReader(), dataParser);
        service = new TrainService(dataApi.getData("vonat.txt"));
    }

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        System.out.println("2. feladat");
        System.out.println(service.countTrainsAndStations());
        System.out.println("3. feladat");
        System.out.println(service.getLongestWaitTime());
        System.out.println("4. feladat");
        System.out.print("Adja meg egy vonat azonosítóját! ");
        int trainId = console.readInt();
        System.out.print("Adjon meg egy időpontot (óra perc)! ");
        Time time = console.readTime();
        System.out.println(service.getTimeDifference(trainId));
        fileWriter.writeAll(trainId, service.getArrivalTimeToStations(trainId));
        System.out.println("7. feladat");
        System.out.println(service.getTrainDetails(time));
    }
}
