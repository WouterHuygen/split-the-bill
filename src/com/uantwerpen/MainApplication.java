package com.uantwerpen;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplication {
    private JButton testButton;
    private JPanel panelMain;

    public MainApplication() {
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "Hello, this is a test message!");
            }
        });
    }

    public static void main (String[] args){
        JFrame frame = new JFrame("MainApplication");
        frame.setContentPane(new MainApplication().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
