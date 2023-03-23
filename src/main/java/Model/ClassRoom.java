package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ClassRoom implements Serializable , Comparable<ClassRoom> {
    private String id;
    private String classCode;
    private List<TA> listTA;
    private List<TimeOFWeek> listStartTime;
    private List<Lesson> lessonList;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<TA> getListTA() {
        return listTA;
    }

    public void setListTA(List<TA> listTA) {
        this.listTA = listTA;
    }

    public List<TimeOFWeek> getListStartTime() {
        return listStartTime;
    }

    public void setListStartTime(List<TimeOFWeek> listStartTime) {
        this.listStartTime = listStartTime;
    }

    public List<Student> getListStudent() {
        return listStudent;
    }

    public void setListStudent(List<Student> listStudent) {
        this.listStudent = listStudent;
    }

    private List<Student> listStudent;

    public ClassRoom(String id, String name, List<TA> listTA, LocalDate startDate, LocalDate endDate, List<TimeOFWeek> listStartTime, List<Lesson> lessonList, List<Student> listStudent) {
        this.id = id;
        this.classCode = name;
        this.listTA = listTA;
        this.endDate = endDate;
        this.listStartTime = listStartTime;
        this.startDate = startDate;
        this.lessonList = lessonList;
        this.listStudent = listStudent;
    }

    public ClassRoom() {

    }

    @Override
    public String toString() {
        return "ClassRoom{" +
                "id='" + id + '\'' +
                ", classCode='" + classCode + '\'' +
                ", listTA=" + listTA +
                ", listStartTime=" + listStartTime +
                ", lessonList=" + lessonList +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", listStudent=" + listStudent +
                '}';
    }

    @Override
    public int compareTo(ClassRoom o) {
        return 0;
    }

    public void display(){
        System.out.println(id);
        System.out.println(classCode);
        System.out.println(startDate.toString());
        System.out.println(endDate.toString());
        for (TA ta : listTA){
            System.out.println(ta.getName());
        }
        for (TimeOFWeek time : listStartTime){
            System.out.println(time.getDayOfWeek() + " - " + time.getTime());
        }
        for (Lesson lesson : lessonList){
            System.out.println(lesson.getLessonNumber() + " - " + lesson.getLessonId() + " - " + lesson.getLessonName() + " - " + lesson.getDate() + " - " + lesson.getTime() + " - " + lesson.getEmailStatus());
        }
        for (Student student : listStudent){
            System.out.println(student.getName());
        }
    }

    public Lesson getLatestLesson(){
        Lesson latestLesson = null;
        LocalDate today = LocalDate.now();
        for (Lesson lesson : lessonList){
            if ((lesson.getDate().isBefore(today)|| lesson.getDate().isEqual(today) )&& (latestLesson == null || lesson.getDate().isAfter(latestLesson.getDate()))){
                latestLesson = lesson;
            }
        }
        return latestLesson;
    }
}
