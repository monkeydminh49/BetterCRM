package Viewer.GUI.MainPanel;

import Viewer.GUI.GUI;
import Viewer.GUI.SidePanel.SidePanel;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private static MainPanel instance = null;
    public static MainPanel getInstance(){
        if (instance == null) {
            instance = new MainPanel();
        }
        return instance;
    }

    public MainPanel() {
//        setBackground(Color.cyan);
    }

}
