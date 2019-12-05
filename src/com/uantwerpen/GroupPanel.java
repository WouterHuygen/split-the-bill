package com.uantwerpen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
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
    private JPanel memberPanel;
    private JPanel generalPanel;
    private JButton createGroupBtn;
    private JLabel categoryLabel;
    private JButton addMemberBtn;
    private JTextField memberNameTb;
    private JTextField memberEmailTb;
    private JTable memberListTbl;
    private JPanel memberListPanel;

    public JPanel getCreateGroupPanel() {
        return createGroupPanel;
    }

    private JPanel createGroupPanel;

    public DefaultTableModel tableModel;
    private static String[]  columnNames = {"MemberId","Name", "Email", "Saldo"};

    public ArrayList<GroupMember> memberList = new ArrayList<GroupMember>();

    private DbWriter dbWriter = new DbWriter();

    private JLabel groupIdLbl;
    private JButton updateMemberBtn;
    private JButton buttonDeletePaymentGroup;

    public GroupPanel() {
        tableModel = (DefaultTableModel) memberListTbl.getModel();

        this.tableModel.setColumnIdentifiers(columnNames);

        this.memberListTbl.setShowVerticalLines(false);
        this.memberListTbl.setRowHeight(32);

        TableColumnModel tcm = memberListTbl.getColumnModel();

        tcm.getColumn(0).setMaxWidth(0);
        tcm.getColumn(0).setWidth(0);
        tcm.getColumn(1).setPreferredWidth(250);
        tcm.getColumn(2).setPreferredWidth(350);
        tcm.getColumn(3).setPreferredWidth(150);
        //tcm.removeColumn(tcm.getColumn(0));

        addMemberBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (String.valueOf(groupIdLbl.getText()).isBlank() | String.valueOf(groupIdLbl.getText()).isEmpty()){
                    GroupMember newMember = new GroupMember(memberNameTb.getText().trim(), memberEmailTb.getText().trim(), 0, 0);
                    memberNameTb.setText(null);
                    memberEmailTb.setText(null);
                    memberList.add(newMember);
                    Object[] row = new Object[5];

                    row[0] = newMember.MemberId;
                    row[1] = newMember.Name;
                    row[2] = newMember.Email;
                    row[3] = newMember.Saldo;
                    row[4] = "DEL";

                    tableModel.addRow(row);
                }
                else if (memberNameTb.getText() != null & memberEmailTb.getText() != null) {
                    GroupMember newMember = new GroupMember(memberNameTb.getText().trim(), memberEmailTb.getText().trim(), Integer.parseInt(groupIdLbl.getText()), 0);
                    newMember.Group = groupNameTb.getText().trim();

                    dbWriter.InsertMember(newMember);
                    memberNameTb.setText(null);
                    memberEmailTb.setText(null);

                    Object[] row = new Object[5];
                        row[0] = 0;
                        row[1] = newMember.Name;
                        row[2] = newMember.Email;
                        row[3] = newMember.Saldo;
                        row[4] = "DEL";

                        tableModel.addRow(row);
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
                }
                /** Hiermee navigeer je naar een panel met ID 1, in dezelfde frame**/
                PanelController.getInstance().cl.show(PanelController.getInstance().getPanelAlt(), "1");
            }
        });
        memberListTbl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {

                int updatedMemberId = Integer.parseInt(memberListTbl.getModel().getValueAt(memberListTbl.getSelectedRow(), 0).toString());
                //boolean isSettled = (boolean) paymentgroupsTbl.getModel().getValueAt(paymentgroupsTbl.getSelectedRow(), 2);

                //GroupMember updatedMember = dbWriter.GetGroupMemberByMemberId(updatedMemberId);

                for (GroupMember oldMember: memberList) {
                    if (oldMember.MemberId == updatedMemberId){
                        oldMember.Name = (String)memberListTbl.getValueAt(memberListTbl.getSelectedRow(), 1);
                        oldMember.Email = (String)memberListTbl.getValueAt(memberListTbl.getSelectedRow(), 2);
                    }
                }
                updateMemberBtn.setVisible(true);
            }
        });
        updateMemberBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dbWriter.UpdateGroupMembers(memberList);
            }
        });
        buttonDeletePaymentGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dbWriter.DeletePaymentGroup(Integer.parseInt(groupIdLbl.getText()));

                /** Hiermee navigeer je naar een panel met ID 1, in dezelfde frame **/
                PanelController.getInstance().cl.show(PanelController.getInstance().getPanelAlt(), "1");
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

                    Object[] headerRow = new Object[4];
                    headerRow[0]="Ids";
                    headerRow[1]="<html><b>Name</b></html>";
                    headerRow[2]="<html><b>Email</b></html>";
                    headerRow[3]="<html><b>Saldo (€)</b></html>";
                    tableModel.addRow(headerRow);

                    Object[] row = new Object[4];
                    for (int i=0; i < memberList.size(); i++){
                        row[0] = memberList.get(i).getMemberId();
                        row[1] = memberList.get(i).getName();
                        row[2] = memberList.get(i).getEmail();
                        row[3] = memberList.get(i).getSaldo();
                        row[4] = "DEL";

                        tableModel.addRow(row);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}


