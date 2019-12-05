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
import java.util.ArrayList;

public class MenuPanel {

    private JPanel menuPanel;
    private JTable paymentGroupsTbl;
    private JButton buttonTransaction;
    private JButton buttonMakeGroup;
    private JButton buttonGroups;
    private JTable tablePaymentGroups;

    private JPanel testPanel = new JPanel();

    public JPanel getMenuPanel(){ return menuPanel; }

    public MenuPanel() {


        buttonTransaction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                TransactionFrame transactionFrame = new TransactionFrame();
            }
        });
        buttonMakeGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DisplayGroups(tablePaymentGroups);
            }
        });
        buttonGroups.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().cl.show(PanelController.getInstance().getPanelAlt(), "2");
            }
        });

        tablePaymentGroups.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting() && tablePaymentGroups.getSelectedRow() != 0) {
                    try {
                        int groupId = (int) tablePaymentGroups.getValueAt(tablePaymentGroups.getSelectedRow(), 0);
                        boolean isSettled = (boolean) tablePaymentGroups.getModel().getValueAt(tablePaymentGroups.getSelectedRow(), 2);
                        if (!isSettled){
                            GroupPanel gp = new GroupPanel();
                            gp.OpenPaymentGroup(groupId);
                        }else {
                            JOptionPane.showMessageDialog(null, "This group has already been settled");
                        }
                    }catch (Exception e){
                        System.out.println("error = " + e);
                    }
                }
            }
        });
    }

    public void DisplayGroups(JTable mainTable){
        DbWriter app = new DbWriter();

        ArrayList<PaymentGroup> list = app.GetAllPaymentGroupsB();
        DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
        model.addColumn("GroupId");
        model.addColumn("GroupName");
        model.addColumn("IsSettled");
        Object[] headerRow = new Object[2];
        headerRow[0]="<html><b>GroupId</b></html>";
        headerRow[1]="<html><b>Paymentgroup name</b></html>";
        model.addRow(headerRow);

        Object[] row = new Object[3];
        for (int i=0; i<list.size(); i++){
            row[0]=list.get(i).getPaymentGroupId();
            row[1]=list.get(i).getPaymentGroupName();
            row[2]=list.get(i).isSettled();
            model.addRow(row);
        }

        TableColumnModel tcm = mainTable.getColumnModel();
        tcm.removeColumn(tcm.getColumn(2));

        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mainTable.getColumnModel().getColumn(0).setMaxWidth(100);
    }
}
