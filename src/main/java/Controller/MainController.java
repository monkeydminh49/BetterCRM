package Controller;

import Model.ClassRoom;

import java.io.IOException;
import java.util.List;

public class MainController {
    private static MainController instance;

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public List<ClassRoom> getClassRoomList() {
        return classRoomList;
    }

    private List<ClassRoom> classRoomList;

    private MainController() {
        try {
            classRoomList = IOSystem.getInstance().read("src/Files/classRoomList.dat");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
