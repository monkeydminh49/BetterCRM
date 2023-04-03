package Controller;

import Model.ClassRoom;
import com.raven.datechooser.DateBetween;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
    
    private DateBetween dateBetween;

    public DateBetween getDateBetween() {
        return dateBetween;
    }

    public void setDateBetween(DateBetween dateBetween) {
        this.dateBetween = dateBetween;
    }

    private MainController() {
        try {
            classRoomList = IOSystem.getInstance().read("src/Files/classRoomList.dat");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        LocalDate ldate = LocalDate.now().minusDays(6);
        Instant instant = Instant.from(ldate.atStartOfDay(ZoneId.of("GMT")));
        dateBetween = new DateBetween(Date.from(instant), new Date());
    }


}
