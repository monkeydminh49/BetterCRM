import Controller.Request;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Request.getInstance().run();
        for (String s : Request.getInstance().getClassIdList()) {
            System.out.println(s);
        }
    }
}