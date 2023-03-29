package com.mycompany.bettercrm;

import Controller.RequestAPI;
import com.raven.main.Main;

import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.SwingUtilities;

public class BetterCRM {
    public static Main main = new Main();
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
        RequestAPI.getInstance().run();
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
////                new GUI().setVisible(true);
////                main.setVisible(true);
////                new Main().setVisible(true);
//            }
//        });
    }
}