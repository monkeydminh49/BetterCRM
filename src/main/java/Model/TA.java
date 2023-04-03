package Model;

import java.io.Serializable;
import java.util.List;

public class TA implements Serializable {
    private String name;
    private String phoneNumber;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public List<ClassRoom> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassRoom> classList) {
        this.classList = classList;
    }

    private List<ClassRoom> classList;
    public String getName() {
        return name;
    }

    public TA(String name, String phoneNumber, String email, String id) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.id = id;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

}
