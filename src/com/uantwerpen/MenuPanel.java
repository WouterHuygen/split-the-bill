package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.PaymentGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MenuPanel {

    private JPanel menuPanel;
    private JTable paymentGroupsTbl;
    private JButton buttonTransaction;
    private JButton buttonMakeGroup;
    private JButton buttonGroups;

    private JPanel testPanel = new JPanel();

    public JPanel getMenuPanel(){ return menuPanel; }

    public MenuPanel() {


        buttonTransaction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().cl.show(PanelController.getInstance().getPanelAlt(), "3");
            }
        });
        buttonMakeGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DisplayGroups();
            }
        });
        buttonGroups.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().cl.show(PanelController.getInstance().getPanelAlt(), "2");
            }
        });
    }

    private void DisplayGroups(){
        System.out.println("displaying groups");

        DbWriter app = new DbWriter();

        ArrayList<PaymentGroup> list = app.GetAllPaymentGroupsB();
        DefaultTableModel model = (DefaultTableModel) paymentGroupsTbl.getModel();
        model.addColumn("<html><b>GroupId</b></html>");
        model.addColumn("<html><b>Group Name</b></html>");
        Object[] headerRow = new Object[2];
        headerRow[0]="<html><b>GroupId</b></html>";
        headerRow[1]="<html><b>Paymentgroup name</b></html>";
        model.addRow(headerRow);

        Object[] row = new Object[2];
        for (int i=0; i<list.size(); i++){
            row[0]=list.get(i).getPaymentGroupId();
            row[1]=list.get(i).getPaymentGroupName();
            model.addRow(row);
        }

        paymentGroupsTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        paymentGroupsTbl.getColumnModel().getColumn(0).setMaxWidth(100);
    }
}
