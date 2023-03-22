import Controller.Request;
import Viewer.GUI.GUI;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
        Request.getInstance().run();
        GUI.getInstance().run();
    }
}