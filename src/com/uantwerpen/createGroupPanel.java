package com.uantwerpen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowEvent;

import com.uantwerpen.Objects.GroupMember;

public class createGroupPanel {
    private JLabel titleLabel;
    private JTextField groupNameTb;
    private JLabel groupNameLabel;
    private JRadioButton apartmentRadioButton;
    private JRadioButton houseRadioButton;
    private JRadioButton tripRadioButton;
    private JRadioButton otherRadioButton;
    private JPanel titlePanel;
    private JPanel generalPanel;
    private JPanel memberPanel;
    private JButton createGroupBtn;
    private JLabel categoryLabel;
    private JButton addMemberButton;
    private JTextField memberNameTb;
    private JTextField memberEmailTb;
    private JTextArea memberTa;
    private JFrame createGroupFrame;


    private int numberOfMembers = 0;
    public JPanel createGroupPanel;


    public createGroupPanel() {
        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String memberName = memberNameTb.getText().trim();
                String memberEmail = memberEmailTb.getText().trim();

                if (numberOfMembers == 0){
                    memberTa.setText(null);
                }
                if (!memberName.isBlank()) {
                    memberTa.append(memberName + "\n");
                    numberOfMembers++;
                    memberNameTb.setText(null);
                    memberEmailTb.setText(null);
                }
            }
        });

        groupNameTb.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                String defaultText = "Enter a group";
                if (groupNameTb.getText().contains(defaultText)){
                    groupNameTb.setText(null);
                }
            }
        });

        createGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    public static void main(String[] args) {
/*        JRadioButton apartmentRadioButton = new JRadioButton();
        JRadioButton houseRadioButton = new JRadioButton();
        JRadioButton tripRadioButton = new JRadioButton();
        JRadioButton otherRadioButton = new JRadioButton();
        ButtonGroup bg = new ButtonGroup();

        bg.add(apartmentRadioButton, houseRadioButton, tripRadioButton, otherRadioButton);*/

        JFrame createGroupFrame = new JFrame("createGroupPanel");
        createGroupFrame.setContentPane(new createGroupPanel().createGroupPanel);
        createGroupFrame.setDefaultCloseOperation(createGroupFrame.EXIT_ON_CLOSE);
        createGroupFrame.pack();
        createGroupFrame.setVisible(true);
    }

    public static void NewScreen(){

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createGroupPanel nw = new createGroupPanel();
                    nw.createGroupFrame.setVisible(true);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}


