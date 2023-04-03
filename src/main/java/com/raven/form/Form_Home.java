package com.raven.form;

import Controller.IOSystem;
import Controller.MainController;
import Model.ClassRoom;
import Model.Lesson;
import Viewer.GUIV1.GUI;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.mycompany.bettercrm.BetterCRM;
import com.raven.datechooser.DateBetween;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.listener.DateChooserAction;
import com.raven.datechooser.listener.DateChooserAdapter;
import com.raven.model.Model_Card;
import com.raven.model.StatusType;
import com.raven.swing.ScrollBar;
import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;
import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class Form_Home extends javax.swing.JPanel {

    private List<ClassRoom> classRoomList;
    private DateChooser chDate = new DateChooser();
    private EventAction eventAction = null;
    private int sortOrientation = -1;
    
    public Form_Home() {
        initComponents();
        
        this.classRoomList = new ArrayList<>();
        chDate.setLabelCalendar(labelCalendar);
        chDate.setDateSelectionMode(DateChooser.DateSelectionMode.BETWEEN_DATE_SELECTED);
        chDate.setSelectedDateBetween(MainController.getInstance().getDateBetween(), true);
        chDate.addActionDateChooserListener(new DateChooserAdapter(){
            @Override
            public void dateBetweenChanged(DateBetween dateBetween, DateChooserAction action){
                MainController.getInstance().setDateBetween(dateBetween);
                updateTableByDate(dateBetween);
            }
        });
        initTableData();
    }
    
    private void initTableData() {
        eventAction = new EventAction() {
            @Override
            public void delete(int row) {
                if (table.isEditing()){
                    table.getCellEditor().stopCellEditing();
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String className = (String) model.getValueAt(row, 1);
                String classId = "";
                
                for (ClassRoom current : classRoomList){
                    if (current.getClassName().equals(className)){
                        System.out.println("Delete " + current.getClassName());
                        classId = current.getId();
                        
                        MainController.getInstance().getClassIdList().remove(classId);
                        classRoomList.remove(current);
                        break;
                    }
                }
                model.removeRow(row);
            }

            @Override
            public void update(int row) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String className = (String) model.getValueAt(row, 1);
                
                for (ClassRoom current : classRoomList){
                    if (current.getClassName().equals(className)){
                        System.out.println("Updating " + current.getClassName());
                        current.updateClassInformation();
                        break;
                    }
                }
             
//                updateClass(current);
            }

            @Override
            public void detail(int row) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String className = (String) model.getValueAt(row, 1);
                
                for (ClassRoom current : classRoomList){
                    if (current.getClassName().equals(className)){
                        BetterCRM.main.setForm(new Form_1(current));
                        break;
                    }
                }
                
            }

            @Override
            public void doneAction(int row) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String className = (String) model.getValueAt(row, 1);
                System.out.println("Finish update " + className);
                updateTableRowContent(row);
            }

        };
        
        //  add row table
        spTable.setVerticalScrollBar(new ScrollBar());
        spTable.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);


        classRoomList = MainController.getInstance().getClassRoomList();
        classRoomList.sort((ClassRoom o1, ClassRoom o2) -> sortOrientation*o1.getLatestLesson().getDate().compareTo(o2.getLatestLesson().getDate()));
        updateTableByDate(MainController.getInstance().getDateBetween());

//        for (int i = 0; i < classRoomList.size(); i++) {
//            ClassRoom current = classRoomList.get(i);
//            Lesson latestLesson = current.getLatestLesson();
//            String emailStatus = latestLesson.getEmailStatus();
//            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            LocalDate lessonDate = latestLesson.getDate();
//            LocalTime todayTime = LocalTime.now();
//            StatusType type = StatusType.YES;
//
//            if (emailStatus.equals("Yes")){
//                type = StatusType.YES;
//            } else if (emailStatus.equals("No")){
//                if ((LocalDate.now().isAfter(lessonDate) && LocalTime.now().isAfter(LocalTime.of(17, 0))) || LocalDate.now().isAfter(lessonDate.plusDays(1))){
//                    type = StatusType.OVERDUE;
//                } else {
//                    type = StatusType.NO;
//                }
//            }
//
//            table.addRow(new Object[]{i+1, current.getClassName(), latestLesson.getLessonName(),latestLesson.getDate().format(dateFormatter), type, new ModelAction(current, eventAction)});
//        }

    }

    
    public void updateTableRowContent(int row){
        ClassRoom current = classRoomList.get(row);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (table.getSelectedRowCount() == 1){
            Lesson latestLesson = current.getLatestLesson();
            String emailStatus = latestLesson.getEmailStatus();
            LocalDate lessonDate = latestLesson.getDate();
            LocalTime todayTime = LocalTime.now();
            StatusType type = StatusType.YES;

            if (emailStatus.equals("Yes")){
                type = StatusType.YES;
            } else if (emailStatus.equals("No")){
                if ((LocalDate.now().isAfter(lessonDate) && LocalTime.now().isAfter(LocalTime.of(17, 0))) || LocalDate.now().isAfter(lessonDate.plusDays(1))){
                    type = StatusType.OVERDUE;
                } else {
                    type = StatusType.NO;
                }
            }
            model.setValueAt(type, row, 4);
//        System.out.println(current.getLatestLesson().getEmailStatus());
        }
    }
 
    public void updateTableByDate(DateBetween dateBetween){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dateFrom = df.format(dateBetween.getFromDate());
        String toDate = df.format(dateBetween.getToDate());
        
        LocalDate start = LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate end = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        
        for (ClassRoom current : classRoomList){
            Lesson latestLesson = current.getLatestLesson();
            LocalDate currentDate = latestLesson.getDate();
            if (currentDate.compareTo(start) >= 0 && currentDate.compareTo(end) <= 0){
                String emailStatus = latestLesson.getEmailStatus();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate lessonDate = latestLesson.getDate();
                LocalTime todayTime = LocalTime.now();
                StatusType type = StatusType.YES;

                if (emailStatus.equals("Yes")){
                    type = StatusType.YES;
                } else if (emailStatus.equals("No")){
                    if ((LocalDate.now().isAfter(lessonDate) && LocalTime.now().isAfter(LocalTime.of(17, 0))) || LocalDate.now().isAfter(lessonDate.plusDays(1))){
                        type = StatusType.OVERDUE;
                    } else {
                        type = StatusType.NO;
                    }
                }
                
                table.addRow(new Object[]{table.getRowCount()+1, current.getClassName(), latestLesson.getLessonName(),latestLesson.getDate().format(dateFormatter), type, new ModelAction(current, eventAction)});
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        card3 = new com.raven.component.Card();
        card1 = new com.raven.component.Card();
        card2 = new com.raven.component.Card();
        panel = new javax.swing.JLayeredPane();
        panelBorder1 = new com.raven.swing.PanelBorder();
        jLabel1 = new javax.swing.JLabel();
        spTable = new javax.swing.JScrollPane();
        table = new com.raven.swing.Table();
        labelCalendar = new javax.swing.JLabel();
        button1 = new com.raven.swing.Button();

        card3.setColor1(new java.awt.Color(241, 208, 62));
        card3.setColor2(new java.awt.Color(211, 184, 61));

        card1.setColor1(new java.awt.Color(142, 142, 250));
        card1.setColor2(new java.awt.Color(123, 123, 245));

        card2.setColor1(new java.awt.Color(186, 123, 247));
        card2.setColor2(new java.awt.Color(167, 94, 236));

        panel.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(127, 127, 127));
        jLabel1.setText("Lesson Status");

        spTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        spTable.setToolTipText("");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Class Name", "Latest Lesson", "Date", "Status", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        spTable.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(0).setMaxWidth(55);
            table.getColumnModel().getColumn(1).setMinWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
        }

        labelCalendar.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        labelCalendar.setForeground(new java.awt.Color(127, 127, 127));
        labelCalendar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calendar.png"))); // NOI18N
        labelCalendar.setText("Calendar");

        button1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/descendant.png"))); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(spTable, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(labelCalendar)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(spTable, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(19, 19, 19))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        sortOrientation*=-1;
        classRoomList.sort((ClassRoom o1, ClassRoom o2) -> sortOrientation*o1.getLatestLesson().getDate().compareTo(o2.getLatestLesson().getDate()));
        updateTableByDate(MainController.getInstance().getDateBetween());
    }//GEN-LAST:event_button1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.swing.Button button1;
    private com.raven.component.Card card1;
    private com.raven.component.Card card2;
    private com.raven.component.Card card3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelCalendar;
    private javax.swing.JLayeredPane panel;
    private com.raven.swing.PanelBorder panelBorder1;
    private javax.swing.JScrollPane spTable;
    private com.raven.swing.Table table;
    // End of variables declaration//GEN-END:variables


}
