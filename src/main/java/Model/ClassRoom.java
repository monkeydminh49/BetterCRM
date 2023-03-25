package Model;

import Controller.RequestAPI;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ClassRoom implements Serializable , Comparable<ClassRoom> {
    private String id;
    private String className;
    private List<TA> listTA;
    private List<TimeOFWeek> listStartTime;
    private List<Lesson> lessonList;

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }
    private LocalDate startDate;
    private LocalDate endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
    private Lesson latestLesson = null;

    public ClassRoom(String id, String name, List<TA> listTA, LocalDate startDate, LocalDate endDate, List<TimeOFWeek> listStartTime, List<Lesson> lessonList, List<Student> listStudent) {
        this.id = id;
        this.className = name;
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
                ", classCode='" + className + '\'' +
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
        System.out.println(className);
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
        updateLatestLesson();
        return latestLesson;
    }
    
    private void updateLatestLesson(){
//        updateClassInformation();     
        LocalDate today = LocalDate.now();
        for (Lesson lesson : lessonList){
            if ((lesson.getDate().isBefore(today)|| lesson.getDate().isEqual(today) )&& (latestLesson == null || lesson.getDate().isAfter(latestLesson.getDate()))){
                latestLesson = lesson;
            }
        }
    }
    
    public void updateClassInformation(){
        ClassRoom another = RequestAPI.getInstance().getClassRoomInformation(id);
        this.lessonList = another.getLessonList();
        this.listStudent = another.getListStudent();
    }
    
}
