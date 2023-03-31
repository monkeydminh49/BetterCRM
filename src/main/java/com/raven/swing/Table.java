package com.raven.swing;

import com.raven.model.StatusType;
import com.raven.swing.scrollbar.ScrollBarCustom;
import com.raven.swing.table.Action;
import com.raven.swing.table.ModelAction;
//import com.raven.swing.table.ModelProfile;
//import com.raven.swing.table.Profile;
import com.raven.swing.table.TableCellAction;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class Table extends JTable {

    public Table() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                TableHeader header = new TableHeader(o + "");
                if (i1 == 4) {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean focus, int row, int column) {
//                if (i1 != 4) {
//                    Component com = super.getTableCellRendererComponent(jtable, o, selected, focus, i, i1);
//                    com.setBackground(Color.WHITE);
//                    setBorder(noFocusBorder);
//                    if (selected) {
//                        com.setForeground(new Color(15, 89, 140));
//                    } else {
//                        com.setForeground(new Color(102, 102, 102));
//                    }
//                    return com;
//                } else {
//                    StatusType type = (StatusType) o;
//                    CellStatus cell = new CellStatus(type);
//                    return cell;
//                }
                if (o instanceof StatusType) {
                    StatusType type = (StatusType) o;
                    CellStatus cell = new CellStatus(type);
                    if (selected) {
                        cell.setBackground(new Color(239, 244, 255));
                    } else {
                        cell.setBackground(Color.WHITE);
                    }
                    return cell;

                } else if (o instanceof ModelAction) {
                    ModelAction data = (ModelAction) o;
                    Action cell = new Action();
                    cell.initAction(data, row);
                    if (selected) {
                        cell.setBackground(new Color(239, 244, 255));
                    } else {
                        cell.setBackground(Color.WHITE);
                    }
                    return cell;
                } else {
                    Component com = super.getTableCellRendererComponent(jtable, o, selected, focus, row, column);
                    setBorder(noFocusBorder);
                    com.setForeground(new Color(102, 102, 102));
                    if (selected) {
                        com.setBackground(new Color(239, 244, 255));
                    } else {
                        com.setBackground(Color.WHITE);
                    }
                    return com;
                }
                
            }
        });


    }

    @Override
    public TableCellEditor getCellEditor(int row, int col) {
        if (col == 5) {
            return new TableCellAction();
        } else {
            return super.getCellEditor(row, col);
        }
    }

    public void addRow(Object[] row) {
        DefaultTableModel mod = (DefaultTableModel) getModel();
        mod.addRow(row);
    }

    public void fixTable(JScrollPane scroll) {
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBar(new ScrollBarCustom());
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        scroll.setBorder(new EmptyBorder(5, 10, 5, 10));
    }
    
    public void setUpdateIcon(ImageIcon img){
    }
}
