package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.PaymentGroup;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

public class MenuPanel {

    private JPanel menuPanel;
    private JButton buttonTransactions;
    private JButton buttonMakeGroup;
    private JButton buttonGroups;
    private JTable tablePaymentGroups;
    private JButton buttonRefresh;

    DefaultTableModel model;

    private JPanel testPanel = new JPanel();

    public JPanel getMenuPanel(){ return menuPanel; }

    public MenuPanel() {
        //PanelController panelController = new PanelController();
        model = (DefaultTableModel) tablePaymentGroups.getModel();

        DisplayGroups();

        buttonTransactions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().cl.show(PanelController.getInstance().getPanelAlt(), "4");
            }
        });
        buttonMakeGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().makeCreateGroupPanel();
            }
        });
        buttonGroups.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().makeGroupPanel((int) tablePaymentGroups.getModel().getValueAt(tablePaymentGroups.getSelectedRow(), 0));
            }
        });

        tablePaymentGroups.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                //tablePaymentGroups.setSelectionBackground(Color.gray);
                if(!event.getValueIsAdjusting() && tablePaymentGroups.getSelectedRow() != 0) {
                    try {
                        int groupId = (int) tablePaymentGroups.getModel().getValueAt(tablePaymentGroups.getSelectedRow(), 0);
                        boolean isSettled = (boolean) tablePaymentGroups.getModel().getValueAt(tablePaymentGroups.getSelectedRow(), 2);
                        if (!isSettled){
                            GroupPanel gp = new GroupPanel();
                            gp.OpenPaymentGroup(groupId);
                            PanelController.getInstance().makeGroupPanel(groupId);
                        }else {
                            JOptionPane.showMessageDialog(null, "This group has already been settled");
                        }
                    }catch (Exception e){
                        System.out.println("error = " + e);
                    }
                }
            }
        });

        buttonRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("test");
                model.setNumRows(0);
                model.setColumnCount(0);
                DisplayGroups();
            }
        });
        tablePaymentGroups.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }
        });
    }

    public void DisplayGroups(){
        DbWriter app = new DbWriter();

        ArrayList<PaymentGroup> list = app.GetAllPaymentGroupsB();
        model.addColumn("GroupId");
        model.addColumn("GroupName");
        model.addColumn("IsSettled");
        Object[] headerRow = new Object[2];
        //headerRow[0]="<html><b>GroupId</b></html>";
        headerRow[0]="<html><b>Group ID</b></html>";
        headerRow[1]="<html><b>Paymentgroup name</b></html>";
        model.addRow(headerRow);

        Object[] row = new Object[3];
        for (int i=0; i<list.size(); i++){
            row[0]=list.get(i).getPaymentGroupId();
            row[1]=list.get(i).getPaymentGroupName();
            row[2]=list.get(i).isSettled();
            model.addRow(row);
        }

        TableColumnModel tcm = tablePaymentGroups.getColumnModel();
        tcm.removeColumn(tcm.getColumn(2));
        tcm.removeColumn(tcm.getColumn(0));

        tablePaymentGroups.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tablePaymentGroups.getColumnModel().getColumn(0).setMaxWidth(100);
    }
}
