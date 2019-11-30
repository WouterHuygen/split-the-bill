package com.uantwerpen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.GroupMember;
import com.uantwerpen.Objects.PaymentGroup;

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
    private JTable memberListTbl;
    public JFrame groupFrame;
    private JPanel createGroupPanel;


    private static String[]  columnNames = {"Name", "Email", "Saldo"};

    public ArrayList<GroupMember> memberSamples = new ArrayList<GroupMember>();

    private JLabel groupIdLbl;

    public void main(String[] args) {
        JFrame groupFrame = new JFrame("createGroupPanel");
        groupFrame.setContentPane(new GroupPanel().createGroupPanel);

        groupFrame.setDefaultCloseOperation(groupFrame.HIDE_ON_CLOSE);
        groupFrame.pack();
        groupFrame.setVisible(true);
    }

    public GroupPanel() {
        //model = (DefaultTableModel)memberListTbl.getModel();
        DbWriter dbWriter = new DbWriter();

        /*ArrayList<GroupMember> newMemberList = new ArrayList<>();

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        memberListTbl = new JTable(tableModel);
        Object[] data = {memberNameTb.getText().trim(), memberEmailTb.getText().trim(), groupNameTb.getText().trim()};
        tableModel.addRow(data);*/

        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GroupMember newMember = new GroupMember();
                newMember.Name = memberNameTb.getText().trim();
                newMember.Email = memberEmailTb.getText().trim();
                newMember.Group = groupNameTb.getText().trim();
                newMember.setGroupId(Integer.parseInt(groupIdLbl.getText()));

                if (newMember.Name != null & newMember.Email != null) {
                    dbWriter.InsertMember(newMember);

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
                    //groupNameTb.setText(null);
                }
            }
        });

        createGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DbWriter dbWriter = new DbWriter();
                  for (int i = 0; i < memberSamples.size(); i++) {
                    GroupMember userToAdd = memberSamples.get(i);
                    //dbWriter.InsertMember();
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

    public void OpenPaymentGroup(int groupId){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    /*DbWriter dbWriter = new DbWriter();
                    GroupPanel nw = new GroupPanel();
                    JFrame groupFrame = new JFrame("GroupPanel");
                    groupFrame.setContentPane(nw.createGroupPanel);
                    groupFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    groupFrame.pack();
                    groupFrame.setVisible(true);

                    nw.groupNameTb.setText(dbWriter.GetPaymentGroupById(groupId));
                    nw.groupIdLbl.setText(String.valueOf(groupId));

                    memberSamples = dbWriter.GetMembersByGroupId(groupId);
                    DefaultTableModel model = (DefaultTableModel) nw.memberListTbl.getModel();

                    model.setColumnIdentifiers(columnNames);

                    Object[] headerRow = new Object[3];
                    headerRow[0]="<html><b>Name</b></html>";
                    headerRow[1]="<html><b>Email</b></html>";
                    headerRow[2]="<html><b>Saldo</b></html>";
                    model.addRow(headerRow);*/

                    Object[] row = new Object[3];
                    for (int i=0; i<memberSamples.size(); i++){
                        row[0]=memberSamples.get(i).getName();
                        row[1]=memberSamples.get(i).getEmail();
                        row[2]="€ " + memberSamples.get(i).getSaldo();

                        model.addRow(row);
                    }

                    nw.memberListTbl.setShowVerticalLines(false);
                    nw.memberListTbl.setRowHeight(32);
                    nw.memberListTbl.getColumnModel().getColumn(1).setPreferredWidth(250);
                    nw.groupNameTb.setEditable(false);
                    nw.groupNameTb.setBackground(Color.white);
                    nw.titleLabel.setVisible(false);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}


