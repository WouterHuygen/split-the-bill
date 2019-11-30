package com.uantwerpen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.GroupMember;
import com.uantwerpen.Objects.PaymentGroup;
import javafx.scene.Group;

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

    public DefaultTableModel tabelModel;
    private static String[]  columnNames = {"Name", "Email", "Saldo"};

    public ArrayList<GroupMember> memberList = new ArrayList<GroupMember>();

    private DbWriter dbWriter = new DbWriter();

    private JLabel groupIdLbl;

    public GroupPanel() {
        tabelModel = (DefaultTableModel) memberListTbl.getModel();

        groupFrame = new JFrame("GroupPanel");
        groupFrame.setContentPane(this.createGroupPanel);
        groupFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        groupFrame.pack();
        groupFrame.setVisible(true);

        this.tabelModel.setColumnIdentifiers(columnNames);

        this.memberListTbl.setShowVerticalLines(false);
        this.memberListTbl.setRowHeight(32);
        this.memberListTbl.getColumnModel().getColumn(0).setPreferredWidth(250);
        this.memberListTbl.getColumnModel().getColumn(1).setPreferredWidth(350);
        this.memberListTbl.getColumnModel().getColumn(2).setPreferredWidth(150);


        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (String.valueOf(groupIdLbl.getText()).isBlank() | String.valueOf(groupIdLbl.getText()).isEmpty()){
                    GroupMember newMember = new GroupMember(memberNameTb.getText().trim(), memberEmailTb.getText().trim(), 0, 0);
                    memberNameTb.setText(null);
                    memberEmailTb.setText(null);
                    memberList.add(newMember);
                    Object[] row = new Object[3];

                    row[0] = newMember.Name;
                    row[1] = newMember.Email;
                    row[2] = newMember.Saldo;

                    tabelModel.addRow(row);
                }
                else if (memberNameTb.getText() != null & memberEmailTb.getText() != null) {
                    GroupMember newMember = new GroupMember(memberNameTb.getText().trim(), memberEmailTb.getText().trim(), Integer.parseInt(groupIdLbl.getText()), 0);
                    newMember.Group = groupNameTb.getText().trim();

                    dbWriter.InsertMember(newMember);
                    memberNameTb.setText(null);
                    memberEmailTb.setText(null);

                    Object[] row = new Object[3];

                        row[0] = newMember.Name;
                        row[1] = newMember.Email;
                        row[2] = newMember.Saldo;

                        tabelModel.addRow(row);
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
                dbWriter.InsertGroup(groupNameTb.getText().trim());
                groupIdLbl.setText(String.valueOf(dbWriter.GetPaymentGroupIdByName(groupNameTb.getText().trim())));

                for (int i = 0; i < memberList.size(); i++) {
                    GroupMember memberToAdd = memberList.get(i);
                    memberToAdd.setGroupId(Integer.parseInt(groupIdLbl.getText()));
                    dbWriter.InsertMember(memberToAdd);
                    System.out.println("actionEvent = adding member " + memberToAdd.Name + "To " + memberToAdd.GroupId);
                    groupFrame.setVisible(false);
                }

                new MainApplication().main(null);
            }
        });
    }

    public void NewScreen(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void OpenPaymentGroup(int groupId){
        Font fTitle = new Font(Font.SERIF, Font.BOLD, 36);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    groupNameTb.setEditable(false);
                    groupNameTb.setBackground(Color.white);
                    String groupName = dbWriter.GetPaymentGroupById(groupId);
                    groupNameTb.setText(groupName);
                    groupIdLbl.setText(String.valueOf(groupId));
                    titleLabel.setText(groupName);
                    titleLabel.setFont(fTitle);
                    memberList = dbWriter.GetMembersByGroupId(groupId);
                    createGroupBtn.setVisible(false);

                    Object[] headerRow = new Object[3];
                    headerRow[0]="<html><b>Name</b></html>";
                    headerRow[1]="<html><b>Email</b></html>";
                    headerRow[2]="<html><b>Saldo (€)</b></html>";
                    tabelModel.addRow(headerRow);

                    Object[] row = new Object[3];
                    for (int i=0; i < memberList.size(); i++){
                        row[0] = memberList.get(i).getName();
                        row[1] = memberList.get(i).getEmail();
                        row[2] = memberList.get(i).getSaldo();

                        tabelModel.addRow(row);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}


