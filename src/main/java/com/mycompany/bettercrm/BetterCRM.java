package com.mycompany.bettercrm;

import Viewer.GUI;
import Viewer.GUIV2;

import java.io.IOException;
import java.net.URISyntaxException;

public class BetterCRM {
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
//        Request.getInstance().run();
//        new GUIV2().setVisible(true);
        new GUI().setVisible(true);
    }
}