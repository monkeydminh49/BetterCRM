/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import Controller.IOSystem;
import Model.ClassRoom;
import Model.Lesson;
import Model.TA;
import Viewer.GUIV1.GUI;
import com.mycompany.bettercrm.BetterCRM;
import com.raven.model.StatusType;
import com.raven.swing.ScrollBar;
import com.raven.swing.table.EventAction;
import com.raven.swing.table.ModelAction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RAVEN
 */
public class Form_1 extends javax.swing.JPanel {

    private ClassRoom current;
    public Form_1(){};
    public Form_1(ClassRoom current) {
        initComponents();
        this.current = current;
        initTableData();
        
    }
    
    private void initTableData() {
         classNameLabel.setText(current.getClassName());
        
        //  add row table
        spTable1.setVerticalScrollBar(new ScrollBar());
        spTable1.getVerticalScrollBar().setBackground(Color.WHITE);
        spTable1.getViewport().setBackground(Color.WHITE);
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        spTable1.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        String classTAsText = "TA:";
        for (TA ta: current.getListTA()){
            classTAsText += " " + ta.getName();
        }
        classTAs.setText(classTAsText);
        
        
        List<Lesson> lessonList = current.getLessonList();

        for (int i = 0; i < lessonList.size(); i++) {
            Lesson lesson = lessonList.get(i);
            String emailStatus = lesson.getEmailStatus();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate lessonDate = lesson.getDate();
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

            table.addRow(new Object[]{i+1, lesson.getLessonName(),lesson.getDate().format(dateFormatter),"", type});
        }
    }
        
    private void updateClass(ClassRoom current){
        SwingWorker sw1 = new SwingWorker() {
            @Override
            protected String doInBackground() throws Exception {
                System.out.println("Updating " + current.getClassName());
                btnReload.setIcon(new ImageIcon(getClass().getResource("/loading1.gif")));
                current.updateClassInformation();
//                publish(getProgress());

                return "Finish update " + current.getClassName();
            }
            @Override 
            protected void process(List chunks)
            {
                Object val = chunks.get(chunks.size() - 1);
  
                System.out.println(val);
            }
            @Override
            protected void done(){
                btnReload.setIcon(new ImageIcon(getClass().getResource("/reload.png")));
                try {
                    System.out.println(get());
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                
                for (int i = 0; i < current.getLessonList().size(); i++){
                    Lesson lesson = current.getLessonList().get(i);
                    String emailStatus = lesson.getEmailStatus();
                    LocalDate lessonDate = lesson.getDate();
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
                    model.setValueAt(type, i, 4);
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    model.setValueAt(lessonDate.format(dateFormatter), i, 2);
//                    System.out.println((i+1) + " " + lessonDate.format(dateFormatter) + " " + emailStatus);
                }
            }
            
        };
        sw1.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panel = new javax.swing.JLayeredPane();
        panelBorder2 = new com.raven.swing.PanelBorder();
        jLabel8 = new javax.swing.JLabel();
        spTable1 = new javax.swing.JScrollPane();
        table = new com.raven.swing.Table();
        btnReload = new com.raven.swing.Button();
        classNameLabel = new javax.swing.JLabel();
        classTAs = new javax.swing.JLabel();

        jLabel3.setText("jLabel1");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("MD-TE-EL1-2201");
        jLabel2.setToolTipText("");

        jLabel5.setText("jLabel1");

        jLabel4.setText("TA");

        jLabel1.setBackground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("Teacher");

        panel.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        panelBorder2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(127, 127, 127));
        jLabel8.setText("Lesson Content");

        spTable1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        spTable1.setToolTipText("");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Lesson", "Date", "Teacher", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        spTable1.setViewportView(table);

        btnReload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reload.png"))); // NOI18N
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder2Layout = new javax.swing.GroupLayout(panelBorder2);
        panelBorder2.setLayout(panelBorder2Layout);
        panelBorder2Layout.setHorizontalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder2Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                        .addGap(492, 492, 492)
                        .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBorder2Layout.createSequentialGroup()
                        .addComponent(spTable1)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );
        panelBorder2Layout.setVerticalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnReload, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        classNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        classNameLabel.setForeground(new java.awt.Color(102, 102, 102));
        classNameLabel.setText("MD-TE-EL1-2201");

        classTAs.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        classTAs.setForeground(new java.awt.Color(102, 102, 102));
        classTAs.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        classTAs.setText("TA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(classNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(classTAs, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classTAs, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelBorder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadActionPerformed
        // TODO add your handling code here:
        
        updateClass(current);
        
    }//GEN-LAST:event_btnReloadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.swing.Button btnReload;
    private javax.swing.JLabel classNameLabel;
    private javax.swing.JLabel classTAs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLayeredPane panel;
    private com.raven.swing.PanelBorder panelBorder2;
    private javax.swing.JScrollPane spTable1;
    private com.raven.swing.Table table;
    // End of variables declaration//GEN-END:variables
}
