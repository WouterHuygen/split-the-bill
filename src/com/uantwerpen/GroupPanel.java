package com.uantwerpen;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.GroupMember;

public class GroupPanel {
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
    private JTable memberList;
    public JFrame groupFrame;

    public ArrayList<GroupMember> newGroupMembers;

    private static String[]  columnNames = {"Name", "Email", "Saldo"};
    public GroupMember arno = new GroupMember();
    public GroupMember test = new GroupMember();

    public ArrayList<GroupMember> memberSamples = new ArrayList<GroupMember>();


    private int numberOfMembers = 0;
    public JPanel createGroupPanel;

    public void main(String[] args) {
        JFrame groupFrame = new JFrame("createGroupPanel");
        groupFrame.setContentPane(new GroupPanel().createGroupPanel);

        groupFrame.setDefaultCloseOperation(groupFrame.HIDE_ON_CLOSE);
        groupFrame.pack();
        groupFrame.setVisible(true);
    }

    public GroupPanel() {
        ArrayList<GroupMember> newMemberList = new ArrayList<>();

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        memberList = new JTable(tableModel);
        Object[] data = {memberNameTb.getText().trim(), memberEmailTb.getText().trim(), groupNameTb.getText().trim()};
        tableModel.addRow(data);

        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GroupMember newMember = new GroupMember();
                newMember.Name = memberNameTb.getText().trim();
                newMember.Email = memberEmailTb.getText().trim();
                newMember.Group = groupNameTb.getText().trim();

                if (newMember.Name != null & newMember.Email != null) {
                    //memberTa.append(newMember + "\n");
                    tableModel.addRow(data);
                    newMemberList.add(newMember);

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
                DbWriter dbWriter = new DbWriter();
                for (int i = 0; i < newMemberList.size(); i++) {
                    GroupMember userToAdd = newMemberList.get(i);
                    dbWriter.InsertMember(userToAdd.Name, userToAdd.Email, userToAdd.Group);
                }
            }
        });
    }

    public void NewScreen(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    GroupPanel nw = new GroupPanel();
                    JFrame groupFrame = new JFrame("GroupFrame");
                    groupFrame.setContentPane(nw.createGroupPanel);
                    groupFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    groupFrame.pack();
                    groupFrame.setVisible(true);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}


