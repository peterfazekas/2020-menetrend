package hu.timetable.controller;

import hu.timetable.model.domain.Time;
import hu.timetable.model.domain.TimeTable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TrainService {

    private final List<TimeTable> timeTables;

    public TrainService(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    /**
     * 2. feladat: Írja a képernyőre a fájlban tárolt vonatok és
     * állomások darabszámát – a kezdő és végállomást is beleértve!
     */
    public String countTrainsAndStations() {
        return String.format("Az állomások száma: %d\nA vonatok száma: %d",
                countFields(TimeTable::getStationId),
                countFields(TimeTable::getTrainId));
    }

    private long countFields(Function<TimeTable, Integer> field) {
        return timeTables.stream()
                .map(field)
                .distinct()
                .count();
    }

    /**
     * 3. feladat: Határozza meg, hogy melyik állomáson állt legtöbbet vonat!
     * Adja meg a vonat és az állomás azonosítóját, valamint az állás idejét!
     * Ha több ilyen volt, elég csak az egyiket megadnia.
     */
    public String getLongestWaitTime() {
        return createTrainsWaitMap().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(this::createLongestWait)
                .get();
    }

    private String createLongestWait(Map.Entry<TimeTable, Integer> entry) {
        return String.format("A(z) %d. vonat a(z) %d. állomáson %d percet állt.",
                entry.getKey().getTrainId(),
                entry.getKey().getStationId(),
                entry.getValue());
    }

    private Map<TimeTable, Integer> createTrainsWaitMap() {
        return timeTables.stream()
                .filter(TimeTable::isArrive)
                .collect(Collectors.toMap(Function.identity(), this::calcWaitTime));
    }

    private Integer calcWaitTime(TimeTable timeTable) {
        return getDepartTime(timeTable)
                .map(departTime -> departTime.getDifference(timeTable.getTime()))
                .orElse(0);
    }

    private Optional<Time> getDepartTime(TimeTable timeTable) {
        return timeTables.stream()
                .filter(i -> i.isSameTrainAndStation(timeTable))
                .filter(TimeTable::isDepart)
                .map(TimeTable::getTime)
                .findAny();
    }

    /**
     * 5. feladat: Ezen a vonalon az előírt menetidő 2 óra 22 perc.
     * Írja a képernyőre, hogy a beolvasott azonosítójú vonat hány
     * perccel tért el ettől! Például:
     * - „A(z) 5. vonat útja 2 perccel rövidebb volt az előírtnál.”,
     * - „A(z) 5. vonat útja pontosan az előírt ideig tartott.”
     * - „A(z) 5. vonat útja 3 perccel hosszabb volt az előírtnál.”
     */
    public String getTimeDifference(int trainId) {
        String answer;
        Time depart = getTime(trainId, Comparator.naturalOrder());
        Time arrive = getTime(trainId, Comparator.reverseOrder());
        int diff = arrive.getDifference(depart) - 142;
        if (diff == 0) {
            answer = String.format(
                    "A(z) %d. vonat útja pontosan az előírt ideig tartott.",
                    trainId);
        } else if (diff > 0) {
            answer = String.format(
                    "A(z) %d. vonat útja %d perccel hosszabb volt az előírtnál.",
                    trainId, diff);
        } else {
            answer = String.format(
                    "A(z) %d. vonat útja %d perccel rövidebb volt az előírtnál.",
                    trainId, diff);
        }
        return answer;
    }

    private Time getTime(int trainId, Comparator<Time> comparator) {
        return timeTables.stream()
                .filter(i -> i.isSameTrain(trainId))
                .map(TimeTable::getTime)
                .min(comparator)
                .get();
    }

    /**
     * 6. feladat: rja a haladX.txt fájlba, hogy a beolvasott azonosítójú
     * vonat melyik állomásra mikor érkezett!
     * A fájlnévben az X helyére a beolvasott vonatazonosító kerüljön!
     */
    public List<String> getArrivalTimeToStations(int trainId) {
        return timeTables.stream()
                .filter(i -> i.isSameTrain(trainId))
                .filter(i -> i.isArrive())
                .map(i -> String.format("%d. állomás: %s", i.getStationId(), i.getTime()))
                .collect(Collectors.toList());
    }

    /**
     * 7. feladat: Adja meg, hogy a beolvasott időpontban úton lévő,
     * azaz a már elindult, de a végállomást még el nem érő vonatok
     * közül melyik hol tartott!
     */
    public String getTrainDetails(Time time) {
        return collectTrainDetails(time).stream()
                .map(this::printDetail)
                .collect(Collectors.joining("\n"));
    }

    private String printDetail(TimeTable timeTable) {
        int trainId = timeTable.getTrainId();
        int stationId = timeTable.getStationId();
        if (timeTable.isDepart()) {
            return String.format("A(z) %d. vonat a %d. állomáson állt.",
                    trainId, stationId);
        } else {
            return String.format("A(z) %d. vonat a %d. és a %d. állomás között járt.",
                    trainId, stationId - 1, stationId);
        }
    }

    private List<TimeTable> collectTrainDetails(Time time) {
        return timeTables.stream()
                .mapToInt(TimeTable::getTrainId)
                .distinct()
                .mapToObj(i -> getPositionByTime(i, time))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

    }

    private Optional<TimeTable> getPositionByTime(int trainId, Time time) {
        List<TimeTable> trainTimeTable = getTrainTimeTable(trainId);
        if (isTrainDepart(trainTimeTable, time)) {
            return trainTimeTable.stream()
                    .filter(i -> (time.getDifference(i.getTime())) < 0)
                    .min(Comparator.comparing(TimeTable::getTime));
        } else {
            return Optional.empty();
        }
    }

    private boolean isTrainDepart(List<TimeTable> trainTimeTable, Time time) {
        return trainTimeTable.get(0).getTime().getDifference(time) < 0;
    }

    private List<TimeTable> getTrainTimeTable(int trainId) {
        return timeTables.stream()
                .filter(i -> i.isSameTrain(trainId))
                .collect(Collectors.toList());
    }
}
