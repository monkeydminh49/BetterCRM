package Viewer.GUI.MainPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LessonStatusPanel  extends JPanel{
    private final String [] title = {"No.","Class Code","Lesson","Date","Status"};
    public static DefaultTableModel model;
    public JTable lessonStatusTable;
    public JPanel panel;
    public LessonStatusPanel() {
        setLayout(new BorderLayout());
        lessonStatusTable = new JTable();
        lessonStatusTable.setBounds(30, 40, 200, 300);
        lessonStatusTable.setRowHeight(30);
        model = new DefaultTableModel(title, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lessonStatusTable.setModel(model);
        JScrollPane sp = new JScrollPane(lessonStatusTable);
        add(sp);

        update();
    }

    public void update(){
        model.setRowCount(0);
        for (int i = 0; i < 10; i++) {
            model.addRow(new Object[]{i,"CSE111","Lesson "+i,"2020-01-01","Done"});
        }
    }
}
