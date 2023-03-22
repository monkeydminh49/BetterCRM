package Viewer.GUI.MainPanel;

import Controller.IOSystem;
import Controller.Request;
import Model.ClassRoom;
import Model.Lesson;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class LessonStatusPanel  extends JPanel{
    private final String [] title = {"No.","Class Code","Lesson","Date","Status", "Button"};
    public static DefaultTableModel model;
    public JTable lessonStatusTable;

    public JButton button;
    public LessonStatusPanel() {
        setLayout(new BorderLayout());
        lessonStatusTable = new JTable();
        lessonStatusTable.setBounds(30, 40, 200, 300);
        lessonStatusTable.setRowHeight(30);
        model = new DefaultTableModel(title, 0){
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
        };
        lessonStatusTable.setModel(model);
        JScrollPane sp = new JScrollPane(lessonStatusTable);
        add(sp);

        update();
    }

    public void update(){
//        button = new JButton("Update");
//        button.addActionListener(e -> {
//            System.out.println("Update button");
//        });

        lessonStatusTable.getColumn("Button").setCellRenderer(new ButtonRenderer());
        ButtonEditor b = new ButtonEditor(new JCheckBox());
        b.setActionListener(e -> {
            System.out.println("Update button");
        });
        lessonStatusTable.getColumn("Button").setCellEditor(b);
        model.setRowCount(0);
        List<ClassRoom> classRoomList = null;
        try {
            classRoomList = IOSystem.getInstance().read(Request.getInstance().getFilesPath() + "classRoomList.dat");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < classRoomList.size(); i++) {
            ClassRoom classRoom = classRoomList.get(i);
            Lesson latestLesson = classRoom.getLatestLesson();
            model.addRow(new Object[]{i+1, classRoom.getClassCode(), latestLesson.getLessonName(),latestLesson.getDate().toString(), latestLesson.getEmailStatus(), "Update"});
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
     * @version 1.0 11/09/98
     */

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Update button");
                }
            });
        }

        public void setActionListener(ActionListener actionListener){
            button.addActionListener(actionListener);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                //
                //
                JOptionPane.showMessageDialog(button, label + ": Ouch!");
                // System.out.println(label + ": Ouch!");
            }
            isPushed = false;
            return new String(label);
        }
}}

class JTableButtonRenderer implements TableCellRenderer {
    private TableCellRenderer defaultRenderer;
    public JTableButtonRenderer(TableCellRenderer renderer) {
        defaultRenderer = renderer;
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof Component)
            return (Component)value;
        return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
class JTableButtonModel extends AbstractTableModel {
    private Object[][] rows = {{"Button1", new JButton("Button1")},{"Button2", new JButton("Button2")},{"Button3", new JButton("Button3")}, {"Button4", new JButton("Button4")}};
    private String[] columns = {"Count", "Buttons"};
    public String getColumnName(int column) {
        return columns[column];
    }
    public int getRowCount() {
        return rows.length;
    }
    public int getColumnCount() {
        return columns.length;
    }
    public Object getValueAt(int row, int column) {
        return rows[row][column];
    }
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
}
