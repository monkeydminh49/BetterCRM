package Model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimeOFWeek {
    public TimeOFWeek() {

    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    private DayOfWeek dayOfWeek;
    private LocalTime time;

    public TimeOFWeek(DayOfWeek dayOfWeek, LocalTime time) {
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }


}
