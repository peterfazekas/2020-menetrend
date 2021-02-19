package hu.timetable.model.domain;

public class TimeTable {

    private final int trainId;
    private final int stationId;
    private final Time time;
    private final Direction direction;

    public TimeTable(int trainId, int stationId, Time time, Direction direction) {
        this.trainId = trainId;
        this.stationId = stationId;
        this.time = time;
        this.direction = direction;
    }

    public int getTrainId() {
        return trainId;
    }

    public int getStationId() {
        return stationId;
    }

    public Time getTime() {
        return time;
    }

    public boolean isArrive() {
        return direction == Direction.ARRIVE;
    }

    public boolean isDepart() {
        return direction == Direction.DEPART;
    }

    public boolean isSameTrain(int trainId) {
        return this.trainId == trainId;
    }

    public boolean isSameTrainAndStation(TimeTable other) {
        return trainId == other.getTrainId() &&
                stationId == other.getStationId();
    }
}
