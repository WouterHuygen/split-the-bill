package com.uantwerpen;

import com.uantwerpen.Controllers.DbWriter;
import com.uantwerpen.Controllers.PaymentGroupController;
import com.uantwerpen.Models.PaymentGroup;

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
    private JButton buttonMakeGroup;
    private JTable tablePaymentGroups;
    private JButton buttonRefresh;

    PaymentGroupController pgc = new PaymentGroupController();
    DefaultTableModel model;

    public JPanel getMenuPanel(){ return menuPanel; }

    public MenuPanel() {
        model = (DefaultTableModel) tablePaymentGroups.getModel();

        DisplayGroups();

        buttonMakeGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().makeCreateGroupPanel();
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
                            PanelController.getInstance().makeGroupOverviewPanel(groupId);
                        }else {
                            JOptionPane.showMessageDialog(null, "This group has already been settled and you will not be able to make any changes to this group.");
                            PanelController.getInstance().makeGroupOverviewPanel(groupId);
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
        ArrayList<PaymentGroup> list = pgc.GetAllPaymentGroupsB();
        model.addColumn("GroupId");
        model.addColumn("GroupName");
        model.addColumn("IsSettled");
        Object[] headerRow = new Object[2];
        //headerRow[0]="<html><b>GroupId</b></html>";
        headerRow[0]="<html><b>Group ID</b></html>";
        headerRow[1]="<html><b>Paymentgroup name</b></html>";
        model.addRow(headerRow);

        if (list != null){
            Object[] row = new Object[3];
            for (int i=0; i<list.size(); i++){
                row[0]=list.get(i).getPaymentGroupId();
                row[1]=list.get(i).getPaymentGroupName();
                row[2]=list.get(i).isSettled();
                model.addRow(row);
            }
        }


        TableColumnModel tcm = tablePaymentGroups.getColumnModel();
        tcm.removeColumn(tcm.getColumn(2));
        tcm.removeColumn(tcm.getColumn(0));

        tablePaymentGroups.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tablePaymentGroups.getColumnModel().getColumn(0).setMaxWidth(100);
    }
}
