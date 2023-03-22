package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ClassRoom implements Serializable , Comparable<ClassRoom> {
    private String id;
    private String classCode;
    private List<TA> listTA;
    private List<TimeOFWeek> listStartTime;
    private List<Lesson> listLesson;
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

    public ClassRoom(String id, String name, List<TA> listTA, List<TimeOFWeek> listStartTime, List<Student> listStudent) {
        this.id = id;
        this.classCode = name;
        this.listTA = listTA;
        this.listStartTime = listStartTime;
        this.listStudent = listStudent;
    }

    public ClassRoom() {

    }


    @Override
    public int compareTo(ClassRoom o) {
        return 0;
    }
}
