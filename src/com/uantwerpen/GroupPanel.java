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
    private JPanel titlePanel;
    private JPanel memberPanel;
    private JPanel generalPanel;
    private JButton createGroupBtn;
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
    private JLabel emailWarningLbl;
    private JLabel nameWarningLbl;
    private JButton buttonBack;

    public GroupPanel() {
        tableModel = (DefaultTableModel) memberListTbl.getModel();
        buttonDeletePaymentGroup.setVisible(false);
        buttonBack.setVisible(false);
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

        Object[] headerRow = new Object[4];
        headerRow[0]="Ids";
        headerRow[1]="<html><b>Name</b></html>";
        headerRow[2]="<html><b>Email</b></html>";
        headerRow[3]="<html><b>Balance (€)</b></html>";
        tableModel.addRow(headerRow);

        memberListTbl.removeColumn(memberListTbl.getColumnModel().getColumn(0));
        memberListTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        addMemberBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String memberEmail = memberEmailTb.getText();
                String memberName = memberNameTb.getText();
                nameWarningLbl.setVisible(false);
                emailWarningLbl.setVisible(false);
                if (memberName.isBlank()){
                    nameWarningLbl.setVisible(true);
                }
                else if(memberEmail.isBlank()){
                    emailWarningLbl.setVisible(true);
                }
                else if (!groupIdLbl.getText().isBlank()) {
                    GroupMember newMember = new GroupMember(memberName.trim(), memberEmail.trim(), Integer.parseInt(groupIdLbl.getText()), 0);
                    newMember.group = groupNameTb.getText().trim();

                    dbWriter.InsertMember(newMember);
                    GroupMember addedMember = dbWriter.GetGroupMemberByGroupIdAndEmail(Integer.parseInt(groupIdLbl.getText()), memberEmail);
                    memberNameTb.setText(null);
                    memberEmailTb.setText(null);

                    Object[] row = new Object[4];
                        row[0] = addedMember.memberId;
                        row[1] = newMember.name;
                        row[2] = newMember.email;
                        row[3] = newMember.balance;

                        tableModel.addRow(row);
                }
                else{
                    GroupMember newMember = new GroupMember(memberName.trim(), memberEmail.trim(), 0, 0);
                    newMember.group = groupNameTb.getText().trim();
                    memberList.add(newMember);
                    //dbWriter.InsertMember(newMember);
                    //GroupMember addedMember = dbWriter.GetGroupMemberByGroupIdAndEmail(Integer.parseInt(groupIdLbl.getText()), memberEmail);
                    memberNameTb.setText(null);
                    memberEmailTb.setText(null);

                    Object[] row = new Object[4];
                    row[0] = 0;
                    row[1] = newMember.name;
                    row[2] = newMember.email;
                    row[3] = newMember.balance;

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
                    groupNameTb.setText(null);
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
                new MenuPanel().DisplayGroups();
                PanelController.getInstance().makeMainPanel();
            }
        });

        memberListTbl.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try{
                    int updatedMemberId = (int) memberListTbl.getModel().getValueAt(memberListTbl.getSelectedRow(), 0);

                    for (GroupMember oldMember: memberList) {
                        if (oldMember.memberId == updatedMemberId){
                            oldMember.name = (String)memberListTbl.getModel().getValueAt(memberListTbl.getSelectedRow(), 1);
                            oldMember.email = (String)memberListTbl.getModel().getValueAt(memberListTbl.getSelectedRow(), 2);
                        }
                    }
                    updateMemberBtn.setVisible(true);
            }catch(Exception ex){}}
        });

        updateMemberBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dbWriter.UpdateGroupMembers(memberList);
                updateMemberBtn.setVisible(false);
            }
        });

        buttonDeletePaymentGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dbWriter.DeletePaymentGroup(Integer.parseInt(groupIdLbl.getText()));

                PanelController.getInstance().makeMainPanel();
            }
        });
//Was voor member te verwijderen
 /*       memberListTbl.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting() && memberListTbl.getSelectedRow() != 0) {
                    try {
                        int memberId = (int)memberListTbl.getModel().getValueAt(memberListTbl.getSelectedRow(), 0);
                        String memberName = (String)memberListTbl.getModel().getValueAt(memberListTbl.getSelectedRow(), 1);
                        deleteMemberBtn.setText("Member (" + memberName + ")");
                    }catch (Exception e){
                        System.out.println("error = " + e);
                    }
                }
            }
        });*/
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().makeGroupOverviewPanel(PanelController.getInstance().getCurrentGroupId());
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


    public JPanel ManagePaymentGroup(int groupId){
        Font fTitle = new Font(Font.SERIF, Font.BOLD, 36);
        String groupName = dbWriter.GetPaymentGroupById(groupId);
        buttonDeletePaymentGroup.setText("Delete " + groupName);
        groupNameTb.setVisible(false);
        groupNameLabel.setVisible(false);
        buttonDeletePaymentGroup.setVisible(true);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    groupNameTb.setEditable(false);
                    groupNameTb.setBackground(Color.white);
                    groupNameTb.setText(groupName);
                    groupIdLbl.setText(String.valueOf(groupId));
                    titleLabel.setText(groupName);
                    titleLabel.setFont(fTitle);
                    memberList = dbWriter.GetMembersByGroupId(groupId);
                    createGroupBtn.setVisible(false);
                    buttonBack.setVisible(true);

                    Object[] row = new Object[4];
                    for (int i=0; i < memberList.size(); i++){
                        row[0] = memberList.get(i).memberId;
                        row[1] = memberList.get(i).name;
                        row[2] = memberList.get(i).email;
                        row[3] = memberList.get(i).balance;

                        tableModel.addRow(row);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return createGroupPanel;
    }
}


