/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author MinhDunk
 */
public class ActionButton extends JButton implements Cloneable {
    
    private boolean mousePress;
    
    public ActionButton(){
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(3, 3, 3, 3));
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent me){
                mousePress = true;
            }
            
            @Override
            public void mouseReleased(MouseEvent me){
                mousePress = false;
            }
        });
    }
    
    public ActionButton clone() throws CloneNotSupportedException{
        return (ActionButton) super.clone();
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        
        if (mousePress){
            g2.setColor(new Color(105, 164, 210));
        } else {
            g2.setColor(new Color(189, 189, 186));
        }
        
        g2.fill(new Rectangle(width, height));
        g2.dispose();
        super.paintComponent(g);
    }
    
    
}
