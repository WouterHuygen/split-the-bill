package com.uantwerpen;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplication {
    private JButton makeGrpBtn;
    private JPanel mainPanel;
    private JLabel titleLabel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainApplication");
        frame.setContentPane(new MainApplication().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public MainApplication() {
        makeGrpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createGroupPanel ns = new createGroupPanel();
                JFrame createGroupFrame = new JFrame("createGroupPanel");
                createGroupFrame.setContentPane(new createGroupPanel().createGroupPanel);
                createGroupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                createGroupFrame.pack();
                createGroupFrame.setVisible(true);
            }
        });
    }
}
