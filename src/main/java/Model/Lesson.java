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


    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    private String lessonNumber;
    private String lessonId;
    private String lessonName;
    private LocalDate date;
    private LocalTime time;
    private String emailStatus;

    public Lesson(String lessonNumber, String lessonId, String lessonName, LocalDate date, LocalTime time, String emailStatus) {
        this.lessonNumber = lessonNumber;
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.date = date;
        this.time = time;
        this.emailStatus = emailStatus;
    }

    public Lesson() {
    }

}
