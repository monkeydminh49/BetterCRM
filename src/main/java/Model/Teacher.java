package Model;

import java.io.Serializable;
import java.util.List;

public class Teacher implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<ClassRoom> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassRoom> classList) {
        this.classList = classList;
    }

    private String email;
    private String phoneNumber;


    private List<ClassRoom> classList;

    public Teacher(String name, String phoneNumber, String email, String id) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Teacher() {
        name = "";
        id = "";
        email = "";
        phoneNumber = "";
    }

    public Teacher(String name) {
        this.name = name;
        id = "";
        email = "";
        phoneNumber = "";
    }

}
