package Viewer.MainPanel;

import javax.swing.*;
import java.awt.*;

public class TestPanel extends JPanel {
    public TestPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JButton button = new JButton("1");
        button.addActionListener(e -> {
            System.out.println("1");
        });
        add(button);
        JButton button2 = new JButton("2");
        button2.addActionListener(e -> {
            System.out.println("2");
        });
        add(button2);
        JButton button3 = new JButton("3");
        button3.addActionListener(e -> {
            System.out.println("3");
        });
        add(button3);
        JButton button4 = new JButton("4");
        button4.addActionListener(e -> {
            System.out.println("4");
        });
        add(button4);
        JButton button5 = new JButton("5");
        button5.addActionListener(e -> {
            System.out.println("5");
        });
        add(button5);
        JButton button6 = new JButton("6");
        button6.addActionListener(e -> {
            System.out.println("6");
        });
        add(button6);
        JButton button7 = new JButton("7");
        button7.addActionListener(e -> {
            System.out.println("7");
        });
        add(button7);
        JButton button8 = new JButton("8");
        button8.addActionListener(e -> {
            System.out.println("8");
        });
        add(button8);
        JButton button9 = new JButton("9");
        button9.addActionListener(e -> {
            System.out.println("9");
        });
        add(button9);
        JButton button10 = new JButton("10");
        button10.addActionListener(e -> {
            System.out.println("10");
        });
        add(button10);
        JButton button11 = new JButton("11");
        button11.addActionListener(e -> {
            System.out.println("11");
        });
        add(button11);
        JButton button12 = new JButton("12");
        button12.addActionListener(e -> {
            System.out.println("12");
        });
        add(button12);
        JButton button13 = new JButton("13");
        button13.addActionListener(e -> {
            System.out.println("13");
        });
        add(button13);
        JButton button14 = new JButton("14");
        button14.addActionListener(e -> {
            System.out.println("14");
        });
        add(button14);
        JButton button15 = new JButton("15");
        button15.addActionListener(e -> {
            System.out.println("15");
        });
        add(button15);
        JButton button16 = new JButton("16");
        button16.addActionListener(e -> {
            System.out.println("16");
    });
        setBackground(Color.blue);
//        setBounds(0, 0, 200, 200);

    }



}
