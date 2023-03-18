package Model;

import java.io.Serializable;
import java.util.List;

public class TA implements Serializable {
    private String name;
    private String phoneNumber;
    private String email;
    private List<ClassRoom> classList;
    public String getName() {
        return name;
    }

    public TA(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

}
