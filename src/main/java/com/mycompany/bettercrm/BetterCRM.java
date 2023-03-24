package com.mycompany.bettercrm;

import Controller.RequestAPI;
import Viewer.GUI;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class BetterCRM {
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
//        RequestAPI.getInstance().run();
        new GUI().setVisible(true);

    }
}