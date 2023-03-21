package Viewer.GUI.MainPanel;

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

    public CardLayout card;
    public Container container;

    public MainPanel() {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        card = new CardLayout();
        container = new Container();
        container.setLayout(card);
        container.add("LESSON_STATUS_TABLE",new LessonStatusPanel());
        container.add("TEST_PANEL",new TestPanel());
        add(container, BorderLayout.CENTER);
    }

    public void show(){
        card.show(container, "TEST_PANEL");
    }

}
