package com.mycompany.bettercrm;

import Controller.RequestAPI;
import Viewer.GUI;

import java.io.IOException;
import java.net.URISyntaxException;

public class BetterCRM {
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
//        RequestAPI.getInstance().run();
//        new GUIV2().setVisible(true);
        new GUI().setVisible(true);
    }
}