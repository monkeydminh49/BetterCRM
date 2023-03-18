package Model;

import java.io.Serializable;
import java.util.List;

public class ClassRoom implements Serializable {
    private String id;
    private String classCode;
    private List<TA> listTA;
    private List<TimeOFWeek> listStartTime;

    public ClassRoom(String id, String name, List<TA> listTA, List<TimeOFWeek> listStartTime) {
        this.id = id;
        this.classCode = name;
        this.listTA = listTA;
        this.listStartTime = listStartTime;
    }

    public ClassRoom() {

    }
}
