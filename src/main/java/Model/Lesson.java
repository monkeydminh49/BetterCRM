package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Lesson {
    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    private String lessonNumber;
    private String lessonId;
    private LocalDate date;
    private LocalTime time;

    public Lesson(String lessonNumber, String lessonId, LocalDate date, LocalTime time) {
        this.lessonNumber = lessonNumber;
        this.lessonId = lessonId;
        this.date = date;
        this.time = time;
    }

    public Lesson() {
    }

}
