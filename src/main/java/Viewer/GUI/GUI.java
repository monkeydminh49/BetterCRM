package Viewer.GUI;

import Viewer.GUI.MainPanel.MainPanel;
import Viewer.GUI.SidePanel.SidePanel;

import javax.swing.*;

public class GUI extends JFrame {
    private static GUI instance = null;
    public static GUI getInstance(){
        if (instance == null) {
            instance = new GUI();
        }
        return instance;
    }

    private final int x = 200;
    private final int y = 125;
    private final int width = 1152;
    private final int height = 648;

    private GUI(){
        setBounds(x, y, width, height);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new GUI();
        GUI.getInstance().add(SidePanel.getInstance());
        GUI.getInstance().add(MainPanel.getInstance());
        SidePanel.getInstance().setBounds(0, 0, 200, GUI.getInstance().getHeight());
        MainPanel.getInstance().setBounds(SidePanel.getInstance().getWidth(), 0,
                GUI.getInstance().getWidth() - SidePanel.getInstance().getWidth(), GUI.getInstance().getHeight());
    }
}
