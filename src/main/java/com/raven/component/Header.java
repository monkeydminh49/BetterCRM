package com.raven.component;

import Controller.MainController;
import Model.ClassRoom;
import com.mycompany.bettercrm.BetterCRM;
import com.raven.form.Form_1;
import com.raven.swing.search.DataSearch;
import com.raven.swing.search.EventClick;
import com.raven.swing.search.PanelSearch;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

public class Header extends javax.swing.JPanel {
    
    private JPopupMenu menu;
    private PanelSearch search;

    public Header() {
        initComponents();
        setOpaque(false);
        menu = new JPopupMenu();
        search = new PanelSearch();
        menu.setBorder(BorderFactory.createLineBorder(new Color(164, 164, 164)));
        menu.add(search);
        menu.setFocusable(false);
        search.addEventClick(new EventClick() {
            @Override
            public void itemClick(DataSearch data) {
                menu.setVisible(false);
                searchText1.setText(data.getText());
                
                boolean isContains = false;
                for (String d : dataStory){
                    if (d.toLowerCase().equals(data.getText().toLowerCase())){
                        isContains = true;
                        break;
                    }
                }
                if (!isContains){
                    dataStory.add(data.getText());
                }
                
                for (ClassRoom current : classRoomList){
                    String d = current.getClassName();
                    if (d.toLowerCase().equals(data.getText().toLowerCase())) {
                        BetterCRM.main.setForm(new Form_1(current));
                        break;
                    }
                }
                
//                BetterCRM.main.setForm(new Form_1(current));
                System.out.println("Click Item : " + data.getText());
            }

            @Override
            public void itemRemove(Component com, DataSearch data) {
                search.remove(com);
                removeHistory(data.getText());
                menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
                if (search.getItemSize() == 0) {
                    menu.setVisible(false);
                }
                System.out.println("Remove Item : " + data.getText());
            }
        });
        classRoomList = MainController.getInstance().getClassRoomList();
    }
    
    private List<ClassRoom> classRoomList = new ArrayList();
    
    private List<DataSearch> search(String search) {
        int limitData = 7;
        List<DataSearch> list = new ArrayList<>();
        
        for (ClassRoom current  : classRoomList) {
            String d = current.getClassName();
            if (d.toLowerCase().contains(search.toLowerCase())) {
                boolean story = isStory(d);
                if (story) {
                    list.add(0, new DataSearch(d, story));
                    //  add or insert to first record
                } else {
                    list.add(new DataSearch(d, story));
                    //  add to last record
                }
                if (list.size() == limitData) {
                    break;
                }
            }
        }
        return list;
    }

    List<String> dataStory = new ArrayList();

    private void removeHistory(String text) {
        for (int i = 0; i < dataStory.size(); i++) {
            String d = dataStory.get(i);
            if (d.toLowerCase().equals(text.toLowerCase())) {
                dataStory.remove(i);
                break;
            }
        }
    }

    private boolean isStory(String text) {
        for (String d : dataStory) {
            if (d.toLowerCase().equals(text.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        searchText1 = new com.raven.swing.SearchText();

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/menu.png"))); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(0, 45));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png"))); // NOI18N

        searchText1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchText1MouseClicked(evt);
            }
        });
        searchText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchText1ActionPerformed(evt);
            }
        });
        searchText1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchText1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchText1, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                .addGap(46, 46, 46))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(searchText1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchText1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_searchText1ActionPerformed

    private void searchText1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchText1MouseClicked
        // TODO add your handling code here:
        if (search.getItemSize() > 0) {
            menu.show(searchText1, 0, searchText1.getHeight());
        }
    }//GEN-LAST:event_searchText1MouseClicked

    private void searchText1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchText1KeyReleased
        // TODO add your handling code here:
        String text = searchText1.getText().trim().toLowerCase();
        search.setData(search(text));
        if (search.getItemSize() > 0) {
            //  * 2 top and bot border
            menu.show(searchText1, 0, searchText1.getHeight());
            menu.setPopupSize(menu.getWidth(), (search.getItemSize() * 35) + 2);
        } else {
            menu.setVisible(false);
        }
    }//GEN-LAST:event_searchText1KeyReleased

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(0, 0, 25, getHeight());
        g2.fillRect(getWidth() - 25, getHeight() - 25, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private com.raven.swing.SearchText searchText1;
    // End of variables declaration//GEN-END:variables
}
