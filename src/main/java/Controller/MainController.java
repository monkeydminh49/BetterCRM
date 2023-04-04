package Controller;

import Model.ClassRoom;
import Model.Student;
import Model.TA;
import Model.Teacher;
import com.raven.datechooser.DateBetween;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainController {
    private static MainController instance;

    public String getProjectPath() {
        return projectPath;
    }

    private String projectPath = System.getProperty("user.dir");
//    private final String projectPath = "D:/ProPTIT/Java/ProjectJava/TestRequest";

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public List<ClassRoom> getClassRoomList() {
        if (classRoomList == null){
            try {

                classRoomList = IOSystem.getInstance().read(MainController.getInstance().getProjectPath()+ "/Files/classRoomList.dat");
            } catch (IOException | ClassNotFoundException e) {
                classRoomList = new ArrayList<>();
                throw new RuntimeException(e);
            }
        }
        return classRoomList;
    }

    private List<ClassRoom> classRoomList;
    private List<String> classIdList;

    public List<TA> getTaList() {
        if (taList == null) {
            try {
                taList = IOSystem.getInstance().read(MainController.getInstance().getProjectPath()+ "/Files/TAList.dat");
            } catch (IOException | ClassNotFoundException e) {
                taList = new ArrayList<>();
                throw new RuntimeException(e);
            }
        }
        return taList;
    }

    public void setTaList(List<TA> taList) {
        this.taList = taList;
    }

    public List<Teacher> getTeacherList() {
        if (teacherList == null) {
            try {
                teacherList = IOSystem.getInstance().read(MainController.getInstance().getProjectPath()+ "/Files/teacherList.dat");
            } catch (IOException | ClassNotFoundException e) {
                teacherList = new ArrayList<>();
                throw new RuntimeException(e);
            }
        }
        return teacherList;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    public List<Student> getStudentList() {
        if (studentList == null) {
            try {
                studentList = IOSystem.getInstance().read(MainController.getInstance().getProjectPath()+ "/Files/studentList.dat");
            } catch (IOException | ClassNotFoundException e) {
                studentList = new ArrayList<>();
                throw new RuntimeException(e);
            }
        }
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    private List <TA> taList;
    private List <Teacher> teacherList;
    private List<Student> studentList;

    public List<String> getClassIdList() {
        if (classIdList == null) {
            try {
                classIdList = IOSystem.getInstance().read(MainController.getInstance().getProjectPath()+ "/Files/classIdList.dat");
            } catch (IOException | ClassNotFoundException e) {
                classIdList = new ArrayList<>();
                throw new RuntimeException(e);
            }
        }
        return classIdList;
    }

    public void setClassIdList(List<String> classIdList) {
        this.classIdList = classIdList;
    }
    
    private DateBetween dateBetween;

    public DateBetween getDateBetween() {
        return dateBetween;
    }

    public void setDateBetween(DateBetween dateBetween) {
        this.dateBetween = dateBetween;
    }

    private MainController() {
        projectPath = projectPath.replaceAll("\\\\", "/");
        LocalDate ldate = LocalDate.now().minusDays(6);
        Instant instant = Instant.from(ldate.atStartOfDay(ZoneId.of("GMT")));
        dateBetween = new DateBetween(Date.from(instant), new Date());
    }


}
