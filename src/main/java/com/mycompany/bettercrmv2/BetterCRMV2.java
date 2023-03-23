package com.mycompany.bettercrmv2;

import Viewer.GUIV2;

import java.io.IOException;
import java.net.URISyntaxException;

public class BetterCRMV2 {
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
//        Request.getInstance().run();
        new GUIV2().setVisible(true);
    }
}