package Viewer.GUI.SidePanel;

import Viewer.GUI.GUI;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {

    private static SidePanel instance = null;
    public static SidePanel getInstance(){
        if (instance == null) {
            instance = new SidePanel();
        }
        return instance;
    }

    public SidePanel() {
        setBackground(Color.green);
    }

}
